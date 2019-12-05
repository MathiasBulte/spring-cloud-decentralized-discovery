package xyz.bulte.decentralizeddiscovery.upkeep.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import xyz.bulte.decentralizeddiscovery.discovery.client.DecentralizedDiscoveryClient;
import xyz.bulte.decentralizeddiscovery.discovery.event.DeregisterServiceEvent;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Service
@AllArgsConstructor
public class PingService {

    private DecentralizedDiscoveryClient discoveryClient;
    private ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedDelay = 5000)
    public void pingAllInstances() {
        List<ServiceInstance> instances = discoveryClient.getInstances();

        Predicate<Tuple2<ServiceInstance, Optional<ResponseEntity>>> noResponseReceived = response -> response.v2.isEmpty();
        Predicate<Tuple2<ServiceInstance, Optional<ResponseEntity>>> notStatus200 = response -> response.v2.get().getStatusCode().is2xxSuccessful();

        instances.parallelStream()
                .map(this::pingInstance)
                .filter(noResponseReceived.or(notStatus200))
                .map(Tuple2::v1)
                .forEach(this::deregister);
    }

    private void deregister(ServiceInstance serviceInstance) {
        eventPublisher.publishEvent(DeregisterServiceEvent.of(this, serviceInstance));
    }

    private Tuple2<ServiceInstance, Optional<ResponseEntity>> pingInstance(ServiceInstance serviceInstance) {
        log.debug("Pinging instance {} of service {}", serviceInstance.getInstanceId(), serviceInstance.getServiceId());
        var restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(serviceInstance.getUri() + "/ping", String.class);
            return Tuple.tuple(serviceInstance, Optional.of(response));
        } catch (RestClientException e) {
            return Tuple.tuple(serviceInstance, Optional.empty());
        }
    }
}

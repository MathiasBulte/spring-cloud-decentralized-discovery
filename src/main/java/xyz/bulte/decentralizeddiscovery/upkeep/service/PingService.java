package xyz.bulte.decentralizeddiscovery.upkeep.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import xyz.bulte.decentralizeddiscovery.discovery.client.DecentralizedDiscoveryClient;
import xyz.bulte.decentralizeddiscovery.discovery.dto.ServiceInstanceResponseEntity;
import xyz.bulte.decentralizeddiscovery.discovery.event.DeregisterServiceEvent;

import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Service
@AllArgsConstructor
public class PingService {

    private DecentralizedDiscoveryClient discoveryClient;
    private ApplicationEventPublisher eventPublisher;

    @Qualifier("nonLoadBalanced")
    private WebClient webClient;

    @Scheduled(fixedDelay = 5000)
    public void pingAllInstances() {
        List<ServiceInstance> instances = discoveryClient.getInstances();

        log.debug("Pinging all instances {}", instances);

        Flux.fromIterable(instances)
                .parallel()
                .flatMap(this::pingInstance)
                .filter(noResponseReceived().or(notStatus200()))
                .map(ServiceInstanceResponseEntity::getServiceInstance)
                .subscribe(this::deregister);
    }

    private Predicate<ServiceInstanceResponseEntity> noResponseReceived() {
        return response -> response.getResponseEntity().isEmpty();
    }

    private Predicate<ServiceInstanceResponseEntity> notStatus200() {
        return Predicate.not((ServiceInstanceResponseEntity response) ->
                response.getResponseEntity().get().getStatusCode().is2xxSuccessful());
    }

    private void deregister(ServiceInstance serviceInstance) {
        eventPublisher.publishEvent(DeregisterServiceEvent.of(this, serviceInstance));
    }

    private Mono<ServiceInstanceResponseEntity> pingInstance(ServiceInstance serviceInstance) {
        log.trace("Pinging instance {} of service {}", serviceInstance.getInstanceId(), serviceInstance.getServiceId());

        return webClient.get()
                .uri(serviceInstance.getUri() + "/ping")
                .exchange()
                .flatMap(clientResponse -> clientResponse.toEntity(String.class))
                .map(response -> ServiceInstanceResponseEntity.of(serviceInstance, response))
                .onErrorReturn(ServiceInstanceResponseEntity.emptyResponse(serviceInstance));
    }
}

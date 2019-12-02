package xyz.bulte.decentralizeddiscovery.discovery.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import xyz.bulte.decentralizeddiscovery.discovery.DecentralizedServiceInstance;
import xyz.bulte.decentralizeddiscovery.discovery.LocalInstance;
import xyz.bulte.decentralizeddiscovery.discovery.dto.DiscoveryRequest;
import xyz.bulte.decentralizeddiscovery.discovery.event.NewServiceRegisteredEvent;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistryService {
    private final ApplicationEventPublisher eventPublisher;
    private final LocalInstanceService localInstanceService;

    private RestTemplate restTemplate = new RestTemplate();

    private Multimap<String, DecentralizedServiceInstance> serviceInstances = Multimaps.synchronizedSetMultimap(HashMultimap.create());

    public Collection<ServiceInstance> getServiceInstances(String serviceId) {
        return Collections.unmodifiableCollection(serviceInstances.get(serviceId));
    }

    public Collection<ServiceInstance> getServiceInstances() {
        return Collections.unmodifiableCollection(serviceInstances.values());
    }

    public void register(DecentralizedServiceInstance serviceInstance) {
        log.debug("Registering serviceInstance {}", serviceInstance);

        // the underlying implementation of the map already
        // enforces this, but I want to trigger something when
        // a new service is registered
        if (!serviceInstances.containsEntry(serviceInstance.getServiceId(), serviceInstance)) {
            serviceInstances.put(serviceInstance.getServiceId(), serviceInstance);

            eventPublisher.publishEvent(NewServiceRegisteredEvent.of(this, serviceInstance));
        }
    }

    public void deregister(ServiceInstance serviceInstance) {
        serviceInstances.remove(serviceInstance.getServiceId(), serviceInstance);
    }

    public void registerWith(ServiceInstance serviceInstance) {
        LocalInstance localInstance = localInstanceService.getLocalInstance();
        DiscoveryRequest discoveryRequest = new DiscoveryRequest(
                localInstance.getInstanceId(),
                localInstance.getServiceId(),
                localInstance.getSecured(),
                localInstance.getPortNumber()
        );

        log.info("Registering with instance {} with request {}", serviceInstance, discoveryRequest);
        restTemplate.postForEntity(serviceInstance.getUri() + "/discovery/register", discoveryRequest, Void.class);
    }
}

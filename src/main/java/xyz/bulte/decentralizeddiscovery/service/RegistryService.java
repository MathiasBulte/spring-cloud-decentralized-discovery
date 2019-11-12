package xyz.bulte.decentralizeddiscovery.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import xyz.bulte.decentralizeddiscovery.DecentralizedServiceInstance;
import xyz.bulte.decentralizeddiscovery.LocalInstance;
import xyz.bulte.decentralizeddiscovery.dto.DiscoveryRequest;
import xyz.bulte.decentralizeddiscovery.event.NewServiceRegisteredEvent;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistryService {
    private final ApplicationEventPublisher eventPublisher;
    private final RestTemplate restTemplate;
    private final LocalInstanceService localInstanceService;

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

    public void registerWith(ServiceInstance serviceInstance) {
        LocalInstance localInstance = localInstanceService.getLocalInstance();
        DiscoveryRequest discoveryRequest = new DiscoveryRequest(
                localInstance.getInstanceId(),
                localInstance.getServiceId(),
                localInstance.getSecured(),
                localInstance.getPortNumber()
        );

        restTemplate.postForEntity(serviceInstance.getUri() + "/discovery/register", discoveryRequest, Void.class);
    }
}

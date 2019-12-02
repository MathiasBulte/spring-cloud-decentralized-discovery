package xyz.bulte.decentralizeddiscovery.discovery.client;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import xyz.bulte.decentralizeddiscovery.discovery.service.RegistryService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class DecentralizedDiscoveryClient implements DiscoveryClient {

    private RegistryService registryService;


    @Override
    public String description() {
        return null;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        return Lists.newArrayList(registryService.getServiceInstances(serviceId));
    }

    public List<ServiceInstance> getInstances() {
        return Lists.newArrayList(registryService.getServiceInstances());
    }

    @Override
    public List<String> getServices() {
        return registryService.getServiceInstances()
                .stream()
                .map(ServiceInstance::getServiceId)
                .collect(Collectors.toList());
    }

    @PostConstruct
    public void start() {
        log.info("Started Decentralized Discovery Client");
    }
}

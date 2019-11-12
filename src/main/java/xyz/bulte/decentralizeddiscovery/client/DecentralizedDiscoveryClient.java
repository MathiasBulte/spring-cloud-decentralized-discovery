package xyz.bulte.decentralizeddiscovery.client;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import xyz.bulte.decentralizeddiscovery.service.RegistryService;

import javax.annotation.PostConstruct;
import java.util.List;

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
        return null;
    }

    @Override
    public List<String> getServices() {
        return null;
    }

    @PostConstruct
    public void start() {
        log.info("Started Decentralized Discovery Client");
    }
}

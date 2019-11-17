package xyz.bulte.decentralizeddiscovery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Slf4j
public class RefreshableDiscoveryClientServiceInstanceListSupplier implements ServiceInstanceListSupplier {

    private final DiscoveryClient discoveryClient;
    private final String serviceId;
    private Flux<ServiceInstance> serviceInstances;

    public RefreshableDiscoveryClientServiceInstanceListSupplier(DiscoveryClient discoveryClient, String serviceId) {
        this.discoveryClient = discoveryClient;
        this.serviceId = serviceId;
        populateServiceInstances();
    }

    private void populateServiceInstances() {
        this.serviceInstances = Flux.defer(() -> {
            return Flux.fromIterable(discoveryClient.getInstances(this.serviceId));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public Flux<List<ServiceInstance>> get() {
        return this.serviceInstances.collectList().flux();
    }

    public void refresh() {
        populateServiceInstances();
    }
}

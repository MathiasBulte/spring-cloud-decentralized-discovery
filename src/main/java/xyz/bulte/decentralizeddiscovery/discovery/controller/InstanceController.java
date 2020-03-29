package xyz.bulte.decentralizeddiscovery.discovery.controller;

import lombok.AllArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import xyz.bulte.decentralizeddiscovery.discovery.service.RegistryService;

@RestController
@RequestMapping("instances")
@AllArgsConstructor
public class InstanceController {

    private RegistryService registryService;

    @GetMapping
    public Flux<ServiceInstance> getInstances() {
        return Flux.fromIterable(registryService.getServiceInstances());
    }

}

package xyz.bulte.decentralizeddiscovery.discovery.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import xyz.bulte.decentralizeddiscovery.discovery.DecentralizedServiceInstance;
import xyz.bulte.decentralizeddiscovery.discovery.dto.DiscoveryRequest;
import xyz.bulte.decentralizeddiscovery.discovery.service.RegistryService;

@RestController
@RequestMapping("discovery")
@AllArgsConstructor
public class DiscoveryController {

    private RegistryService registryService;

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody DiscoveryRequest discoveryRequest, ServerHttpRequest serverHttpRequest) {
        DecentralizedServiceInstance decentralizedServiceInstance = new DecentralizedServiceInstance(
                discoveryRequest.getInstanceId(),
                discoveryRequest.getServiceId(),
                serverHttpRequest.getRemoteAddress().getAddress().getHostAddress(),
                discoveryRequest.getPort(),
                discoveryRequest.isSecured()
        );

        registryService.register(decentralizedServiceInstance);
    }
}

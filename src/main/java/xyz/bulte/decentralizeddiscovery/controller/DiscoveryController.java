package xyz.bulte.decentralizeddiscovery.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import xyz.bulte.decentralizeddiscovery.DecentralizedServiceInstance;
import xyz.bulte.decentralizeddiscovery.dto.DiscoveryRequest;
import xyz.bulte.decentralizeddiscovery.service.RegistryService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("discovery")
@AllArgsConstructor
public class DiscoveryController {

    private RegistryService registryService;

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody DiscoveryRequest discoveryRequest, HttpServletRequest servletRequest) {
        DecentralizedServiceInstance decentralizedServiceInstance = new DecentralizedServiceInstance(
                discoveryRequest.getInstanceId(),
                discoveryRequest.getServiceId(),
                servletRequest.getRemoteHost(),
                discoveryRequest.getPort(),
                discoveryRequest.isSecured()
        );

        registryService.register(decentralizedServiceInstance);
    }
}

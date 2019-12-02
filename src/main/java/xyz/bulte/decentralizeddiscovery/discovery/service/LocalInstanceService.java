package xyz.bulte.decentralizeddiscovery.discovery.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import xyz.bulte.decentralizeddiscovery.discovery.LocalInstance;

import java.util.UUID;


/**
 * Why is this a service and not just a LocalService defined by @Bean?
 * The actual running server port is defined at runtime.
 * For example: when setting the server.port property to 0,
 * a random port will be used
 */
@Service
public class LocalInstanceService {

    // TODO: find a better way to start broadcasting after we created the localInstance, event?
    // this will get rid of circular dependency & @Lazy
    private final DiscoveryService discoveryService;

    @Getter
    private LocalInstance localInstance;
    private String serviceId;

    public LocalInstanceService(@Lazy DiscoveryService discoveryService, @Value("${spring.application.name}") String serviceId) {
        this.discoveryService = discoveryService;
        this.serviceId = serviceId;
    }

    @EventListener
    public void listenForInitializedEvent(ServletWebServerInitializedEvent event) {
        int portNumber = event.getApplicationContext().getWebServer().getPort();

        localInstance = LocalInstance.of(UUID.randomUUID().toString(), serviceId, false, portNumber);

        // this is the caveat of using the actual running port instead of just
        // using ${server.port}
        discoveryService.publicAnnouncement();
    }
}

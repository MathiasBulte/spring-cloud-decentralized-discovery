package xyz.bulte.decentralizeddiscovery.discovery.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
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

    @Getter
    private LocalInstance localInstance;
    private String serviceId;

    public LocalInstanceService(@Value("${spring.application.name}") String serviceId) {
        this.serviceId = serviceId;
    }

    @EventListener
    public void listenForInitializedEvent(WebServerInitializedEvent event) {
        int portNumber = event.getApplicationContext().getWebServer().getPort();

        localInstance = LocalInstance.of(UUID.randomUUID().toString(), serviceId, false, portNumber);
    }
}

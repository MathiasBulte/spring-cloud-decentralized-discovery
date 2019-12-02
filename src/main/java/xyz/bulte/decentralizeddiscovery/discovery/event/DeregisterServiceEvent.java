package xyz.bulte.decentralizeddiscovery.discovery.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.ApplicationEvent;

@Getter
@EqualsAndHashCode
public class DeregisterServiceEvent extends ApplicationEvent {
    private ServiceInstance serviceInstance;

    private DeregisterServiceEvent(Object source, ServiceInstance serviceInstance) {
        super(source);
        this.serviceInstance = serviceInstance;
    }

    public static DeregisterServiceEvent of(Object source, ServiceInstance serviceInstance) {
        return new DeregisterServiceEvent(source, serviceInstance);
    }
}

package xyz.bulte.decentralizeddiscovery.discovery.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.ApplicationEvent;

@Getter
@EqualsAndHashCode
public class NewServiceRegisteredEvent extends ApplicationEvent {
    private ServiceInstance serviceInstance;

    private NewServiceRegisteredEvent(Object source, ServiceInstance serviceInstance) {
        super(source);
        this.serviceInstance = serviceInstance;
    }

    public static NewServiceRegisteredEvent of(Object source, ServiceInstance serviceInstance) {
        return new NewServiceRegisteredEvent(source, serviceInstance);
    }
}

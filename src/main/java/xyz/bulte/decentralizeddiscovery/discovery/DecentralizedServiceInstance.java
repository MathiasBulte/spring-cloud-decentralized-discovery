package xyz.bulte.decentralizeddiscovery.discovery;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;

import java.net.URI;
import java.util.Map;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DecentralizedServiceInstance implements ServiceInstance {

    @EqualsAndHashCode.Include
    private final String instanceId;

    @EqualsAndHashCode.Include
    private final String serviceId;

    private final String host;
    private final int port;
    private final boolean secure;
    private Map<String, String> metadata;

    @Override
    public URI getUri() {
        return DefaultServiceInstance.getUri(this);
    }
}

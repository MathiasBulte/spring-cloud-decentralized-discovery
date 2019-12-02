package xyz.bulte.decentralizeddiscovery;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.discovery.noop.NoopDiscoveryClientAutoConfiguration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistryAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xyz.bulte.decentralizeddiscovery.discovery.config.DecentralizedDiscoveryConfiguration;
import xyz.bulte.decentralizeddiscovery.upkeep.config.DecentralizedDiscoveryUpkeepConfiguration;

@Configuration
@EnableConfigurationProperties
@Import({DecentralizedDiscoveryConfiguration.class, DecentralizedDiscoveryUpkeepConfiguration.class})
@ConditionalOnProperty(value = "eureka.client.enabled", matchIfMissing = true)
@ConditionalOnDiscoveryEnabled
@AutoConfigureBefore({ NoopDiscoveryClientAutoConfiguration.class,
        CommonsClientAutoConfiguration.class, ServiceRegistryAutoConfiguration.class })
@AutoConfigureAfter(name = {
        "org.springframework.cloud.autoconfigure.RefreshAutoConfiguration",
        "org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration",
        "org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationAutoConfiguration",
        "xyz.bulte.decentralizeddiscovery.discovery.autoconfigure.DiscoveryAutoConfiguration"
})
public class DecentralizedDiscoveryAutoConfiguration {
}

package xyz.bulte.decentralizeddiscovery;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xyz.bulte.decentralizeddiscovery.discovery.config.DecentralizedDiscoveryConfiguration;
import xyz.bulte.decentralizeddiscovery.upkeep.config.DecentralizedDiscoveryUpkeepConfiguration;

@Configuration
@EnableConfigurationProperties
@Import({DecentralizedDiscoveryConfiguration.class, DecentralizedDiscoveryUpkeepConfiguration.class})
@ConditionalOnProperty(value = "discovery.enabled", matchIfMissing = true)
@ConditionalOnDiscoveryEnabled
public class DecentralizedDiscoveryAutoConfiguration {
}

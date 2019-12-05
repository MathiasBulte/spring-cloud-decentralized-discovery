package xyz.bulte.decentralizeddiscovery.upkeep.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import xyz.bulte.decentralizeddiscovery.discovery.autoconfigure.DiscoveryAutoConfiguration;

@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(
        value = {"discovery.enabled"},
        matchIfMissing = true
)
@ConditionalOnDiscoveryEnabled
@ComponentScan("xyz.bulte.decentralizeddiscovery.upkeep")
@AutoConfigureAfter({DiscoveryAutoConfiguration.class})
@EnableScheduling
@EnableAsync
public class DecentralizedDiscoveryUpkeepConfiguration {

}

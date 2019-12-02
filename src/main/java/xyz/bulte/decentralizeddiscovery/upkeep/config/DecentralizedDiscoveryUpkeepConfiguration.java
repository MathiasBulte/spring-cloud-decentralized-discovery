package xyz.bulte.decentralizeddiscovery.upkeep.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(
        value = {"eureka.client.enabled"},
        matchIfMissing = true
)
@ConditionalOnDiscoveryEnabled
@ComponentScan("xyz.bulte.decentralizeddiscovery.upkeep")
@EnableScheduling
@EnableAsync
public class DecentralizedDiscoveryUpkeepConfiguration {

}

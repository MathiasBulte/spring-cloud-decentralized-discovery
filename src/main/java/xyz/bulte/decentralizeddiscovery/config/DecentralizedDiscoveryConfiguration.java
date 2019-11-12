package xyz.bulte.decentralizeddiscovery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import xyz.bulte.decentralizeddiscovery.LocalInstance;

import java.util.UUID;

@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(
        value = {"eureka.client.enabled"},
        matchIfMissing = true
)
@ConditionalOnDiscoveryEnabled
@ComponentScan("xyz.bulte.decentralizeddiscovery")
@EnableScheduling
@EnableAsync
public class DecentralizedDiscoveryConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

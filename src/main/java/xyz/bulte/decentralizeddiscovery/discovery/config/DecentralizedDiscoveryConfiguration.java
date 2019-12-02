package xyz.bulte.decentralizeddiscovery.discovery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import xyz.bulte.decentralizeddiscovery.discovery.RefreshableDiscoveryClientServiceInstanceListSupplier;
import xyz.bulte.decentralizeddiscovery.discovery.client.DecentralizedDiscoveryClient;

@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(
        value = {"eureka.client.enabled"},
        matchIfMissing = true
)
@ConditionalOnDiscoveryEnabled
@ComponentScan("xyz.bulte.decentralizeddiscovery.discovery")
@EnableScheduling
@EnableAsync
public class DecentralizedDiscoveryConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnBean(DiscoveryClient.class)
    @ConditionalOnMissingBean
    public RefreshableDiscoveryClientServiceInstanceListSupplier discoveryClientServiceInstanceSupplier(
            DecentralizedDiscoveryClient discoveryClient, @Value("${spring.application.name}") String serviceId) {
        return new RefreshableDiscoveryClientServiceInstanceListSupplier(
                discoveryClient, serviceId);
    }
}

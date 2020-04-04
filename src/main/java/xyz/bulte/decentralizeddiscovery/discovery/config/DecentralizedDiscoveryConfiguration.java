package xyz.bulte.decentralizeddiscovery.discovery.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerWebClientBuilderBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import xyz.bulte.decentralizeddiscovery.discovery.RefreshableDiscoveryClientServiceInstanceListSupplier;
import xyz.bulte.decentralizeddiscovery.discovery.client.DecentralizedDiscoveryClient;

@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(
        value = {"discovery.enabled"},
        matchIfMissing = true
)
@ConditionalOnDiscoveryEnabled
@ComponentScan("xyz.bulte.decentralizeddiscovery.discovery")
@EnableScheduling
@EnableAsync
@AutoConfigureBefore(LoadBalancerWebClientBuilderBeanPostProcessor.class)
public class DecentralizedDiscoveryConfiguration {

    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @Primary
    public WebClient reactiveWebClient(WebClient.Builder loadBalancedWebClientBuilder) {
        return loadBalancedWebClientBuilder.build();
    }

    @Bean
    @Qualifier("nonLoadBalanced")
    public WebClient nonLoadBalancedWebClient() {
        return WebClient.create();
    }

    @Bean
    @ConditionalOnBean({DiscoveryClient.class, ReactiveDiscoveryClient.class})
    @ConditionalOnMissingBean
    public RefreshableDiscoveryClientServiceInstanceListSupplier discoveryClientServiceInstanceSupplier(
            DecentralizedDiscoveryClient discoveryClient, @Value("${spring.application.name}") String serviceId) {
        return new RefreshableDiscoveryClientServiceInstanceListSupplier(
                discoveryClient, serviceId);
    }
}

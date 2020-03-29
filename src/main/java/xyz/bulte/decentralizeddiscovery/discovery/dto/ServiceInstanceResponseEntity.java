package xyz.bulte.decentralizeddiscovery.discovery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@Data
@AllArgsConstructor(staticName = "of")
@RequiredArgsConstructor(staticName = "emptyResponse")
public class ServiceInstanceResponseEntity {
    private final ServiceInstance serviceInstance;
    private ResponseEntity<String> responseEntity;

    public Optional<ResponseEntity<String>> getResponseEntity() {
        return Optional.ofNullable(responseEntity);
    }
}

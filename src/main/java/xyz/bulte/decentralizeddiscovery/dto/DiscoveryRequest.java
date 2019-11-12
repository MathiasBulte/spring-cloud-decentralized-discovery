package xyz.bulte.decentralizeddiscovery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscoveryRequest {
    private String instanceId;
    private String serviceId;
    private boolean secured;
    private int port;
}

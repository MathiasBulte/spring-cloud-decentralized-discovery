package xyz.bulte.decentralizeddiscovery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscoveryRequest {

    @NotBlank
    @NonNull
    private String instanceId;

    @NotBlank
    @NonNull
    private String serviceId;

    @NotNull
    private boolean secured;

    @Min(1)
    @Max(65535)
    private int port;
}

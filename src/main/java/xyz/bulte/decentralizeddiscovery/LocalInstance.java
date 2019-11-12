package xyz.bulte.decentralizeddiscovery;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public class LocalInstance {
    private String instanceId;
    private String serviceId;
    private Boolean secured;
    private Integer portNumber;
}

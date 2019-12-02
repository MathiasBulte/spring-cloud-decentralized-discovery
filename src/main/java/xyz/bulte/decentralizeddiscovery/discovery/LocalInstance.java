package xyz.bulte.decentralizeddiscovery.discovery;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public class LocalInstance {

    @NonNull
    private String instanceId;

    @NonNull
    private String serviceId;

    @NonNull
    private Boolean secured;

    @NonNull
    private Integer portNumber;
}

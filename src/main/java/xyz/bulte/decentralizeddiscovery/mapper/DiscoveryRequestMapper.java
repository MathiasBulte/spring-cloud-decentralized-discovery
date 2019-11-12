package xyz.bulte.decentralizeddiscovery.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import xyz.bulte.decentralizeddiscovery.dto.DiscoveryRequest;

import java.io.IOException;
import java.io.StringWriter;

@Component
public class DiscoveryRequestMapper {

    private ObjectMapper objectMapper = new ObjectMapper();

    public String parse(DiscoveryRequest discoveryRequest) {
        StringWriter stringWriter = new StringWriter();

        try {
            objectMapper.writerFor(DiscoveryRequest.class).writeValue(stringWriter, discoveryRequest);
        } catch (IOException e) {
            throw new RuntimeException("Error while parsing DiscoveryRequest to String", e);
        }

        return stringWriter.toString();
    }

    public DiscoveryRequest parse(String discoveryRequest) {
        try {
            return objectMapper.readerFor(DiscoveryRequest.class).readValue(discoveryRequest);
        } catch (IOException e) {
            throw new RuntimeException("Error while parsing String to DiscoveryRequest", e);
        }
    }


}

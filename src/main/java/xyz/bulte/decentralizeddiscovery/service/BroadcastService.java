package xyz.bulte.decentralizeddiscovery.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Unchecked;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import xyz.bulte.decentralizeddiscovery.DecentralizedServiceInstance;
import xyz.bulte.decentralizeddiscovery.LocalInstance;
import xyz.bulte.decentralizeddiscovery.dto.DiscoveryRequest;
import xyz.bulte.decentralizeddiscovery.mapper.DiscoveryRequestMapper;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.Valid;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.function.Predicate.not;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class BroadcastService {

    private final DiscoveryRequestMapper discoveryRequestMapper;
    private final RegistryService registryService;
    private final LocalInstanceService localInstanceService;

    private DatagramSocket socket;
    private List<Integer> udpSocketPorts = List.of(28500, 28501, 28502, 28503, 28504);

    @Async
    @SneakyThrows
    public void broadcast() {
        log.debug("Broadcasting");

        LocalInstance localInstance = localInstanceService.getLocalInstance();

        listAllBroadcastAddresses().forEach(address -> broadcast(discoveryRequestMapper.parse(
                new DiscoveryRequest(
                        localInstance.getInstanceId(),
                        localInstance.getServiceId(),
                        localInstance.getSecured(),
                        localInstance.getPortNumber()
                )),
                address)
        );
    }

    @Async
    public void listenForBroadcasts() {
        log.info("Listening for broadcasts");
        run();
    }

    private void broadcast(String broadcastMessage, InetAddress address) {
        try (var socket = new DatagramSocket()) {
            log.debug("Broadcasting on address {}", address);

            socket.setBroadcast(true);

            var buffer = broadcastMessage.getBytes();
            udpSocketPorts.forEach(Unchecked.consumer(port -> {
                var packet = new DatagramPacket(buffer, buffer.length, address, port);

                socket.send(packet);
            }));
        } catch (RuntimeException | IOException e) {
            log.error("Error when broadcasting", e);
        }
    }


    @SneakyThrows
    private void run() {
        while (true) {
            byte[] buffer = new byte[256];

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            var discoveryRequest = discoveryRequestMapper.parse(new String(packet.getData()));

            handleIncomingMessage(packet, discoveryRequest);
        }
    }

    @SneakyThrows
    private Stream<InetAddress> listAllBroadcastAddresses() {
        var networkInterfaces = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(NetworkInterface.getNetworkInterfaces().asIterator(), Spliterator.ORDERED),
                false
        );

        var availableInterfaces = networkInterfaces
                .filter(not(Unchecked.predicate(NetworkInterface::isLoopback)))
                .filter(Unchecked.predicate(NetworkInterface::isUp));

        return availableInterfaces
                .flatMap(networkInterface -> networkInterface.getInterfaceAddresses().stream())
                .map(InterfaceAddress::getBroadcast)
                .filter(Objects::nonNull);
    }


    private void handleIncomingMessage(DatagramPacket packet, @Valid DiscoveryRequest discoveryRequest) {
        LocalInstance localInstance = localInstanceService.getLocalInstance();
        if (localInstance.getInstanceId().equals(discoveryRequest.getInstanceId())) {
            // don't wanna register ourselves
            return;
        }

        var serviceInstance = new DecentralizedServiceInstance(
                discoveryRequest.getInstanceId(),
                discoveryRequest.getServiceId(),
                packet.getAddress().getHostAddress(),
                discoveryRequest.getPort(),
                discoveryRequest.isSecured()
        );

        registryService.register(serviceInstance);
    }

    @PostConstruct
    public void init() {
        socket = udpSocketPorts.stream()
                .map(tryCreateSocket())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Couldn't open socket, all available ports are in use"));
    }

    @PreDestroy
    public void shutdown() {
        socket.close();
    }

    private Function<Integer, Optional<DatagramSocket>> tryCreateSocket() {
        return integer -> {
            try {
                return Optional.of(new DatagramSocket(integer));
            } catch (SocketException e) {
                return Optional.empty();
            }
        };
    }

}

package xyz.bulte.decentralizeddiscovery.discovery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.bulte.decentralizeddiscovery.discovery.RefreshableDiscoveryClientServiceInstanceListSupplier;
import xyz.bulte.decentralizeddiscovery.discovery.event.DeregisterServiceEvent;
import xyz.bulte.decentralizeddiscovery.discovery.event.NewServiceRegisteredEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscoveryService {

    private final BroadcastService broadcastService;
    private final RegistryService registryService;
    private final RefreshableDiscoveryClientServiceInstanceListSupplier refreshableDiscoveryClientServiceInstanceListSupplier;

    /**
     * Scheduling only starts after a prolonged time.
     * First announcement should be triggered when the local instance
     * is set up.
     */
    @Scheduled(fixedDelay = 3600000, initialDelay = 3600000)
    public void publicAnnouncement() {
        broadcastService.broadcast();
    }

    @EventListener
    public void privateAnnouncement(NewServiceRegisteredEvent event) {
        registryService.registerWith(event.getServiceInstance());
        refreshableDiscoveryClientServiceInstanceListSupplier.refresh();
    }

    @EventListener
    public void deregisterService(DeregisterServiceEvent event) {
        log.debug("Handling DeregisterServiceEvent {}", event);
        registryService.deregister(event.getServiceInstance());
    }

    @EventListener
    public void listenForApplicationReadyEvent(ApplicationReadyEvent event) {
        publicAnnouncement();
        broadcastService.listenForBroadcasts();
    }

}

package xyz.bulte.decentralizeddiscovery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.bulte.decentralizeddiscovery.event.NewServiceRegisteredEvent;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscoveryService {

    private final BroadcastService broadcastService;
    private final RegistryService registryService;

    @PostConstruct
    public void init() {
        broadcastService.listenForBroadcasts();
    }

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
    }
}

package xyz.bulte.decentralizeddiscovery.upkeep.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/ping")
public class PingController {

    @GetMapping
    public String pong() {
        log.debug("Received ping");
        return "pong";
    }
}

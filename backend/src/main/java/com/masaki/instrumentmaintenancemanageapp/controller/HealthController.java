package com.masaki.instrumentmaintenancemanageapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health")

    // renderのsleep対策
    public String health() {
        return "ok";
    }
}
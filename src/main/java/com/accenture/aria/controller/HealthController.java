package com.accenture.aria.controller;

import com.accenture.aria.dto.HealthResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<HealthResponseDTO> getHealth() {
        HealthResponseDTO health = new HealthResponseDTO("UP", "1.0.0", "aria");
        return ResponseEntity.ok(health);
    }
}

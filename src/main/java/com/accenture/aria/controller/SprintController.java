package com.accenture.aria.controller;

import com.accenture.aria.dto.SprintRequestDTO;
import com.accenture.aria.dto.SprintResponseDTO;
import com.accenture.aria.dto.SprintSummaryResponseDTO;
import com.accenture.aria.model.SprintStatus;
import com.accenture.aria.service.SprintMapper;
import com.accenture.aria.service.SprintService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sprints")
public class SprintController {

    private final SprintService sprintService;

    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @GetMapping
    public ResponseEntity<List<SprintResponseDTO>> findAll(
            @RequestParam(required = false) SprintStatus status) {
        List<SprintResponseDTO> response;
        if (status != null) {
            response = sprintService.findByStatus(status).stream()
                    .map(SprintMapper::toResponse)
                    .toList();
        } else {
            response = sprintService.findAll().stream()
                    .map(SprintMapper::toResponse)
                    .toList();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SprintResponseDTO> findById(@PathVariable Long id) {
        return sprintService.findById(id)
                .map(SprintMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<SprintSummaryResponseDTO> getSummary(@PathVariable Long id) {
        return sprintService.getSummary(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SprintResponseDTO> create(@Valid @RequestBody SprintRequestDTO requestDTO) {
        SprintResponseDTO response = SprintMapper.toResponse(sprintService.createFromDTO(requestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SprintResponseDTO> update(@PathVariable Long id,
                                                    @Valid @RequestBody SprintRequestDTO requestDTO) {
        return sprintService.updateFromDTO(id, requestDTO)
                .map(SprintMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!sprintService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
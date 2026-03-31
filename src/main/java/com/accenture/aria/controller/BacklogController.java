package com.accenture.aria.controller;

import com.accenture.aria.dto.BacklogRequestDTO;
import com.accenture.aria.dto.BacklogResponseDTO;
import com.accenture.aria.dto.TicketResponseDTO;
import com.accenture.aria.service.BacklogMapper;
import com.accenture.aria.service.BacklogService;
import com.accenture.aria.service.TicketMapper;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/backlogs")
public class BacklogController {

    private final BacklogService backlogService;

    public BacklogController(BacklogService backlogService) {
        this.backlogService = backlogService;
    }

    @GetMapping
    public ResponseEntity<List<BacklogResponseDTO>> findAll() {
        List<BacklogResponseDTO> response = backlogService.findAll().stream()
                .map(BacklogMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BacklogResponseDTO> findById(@PathVariable Long id) {
        return backlogService.findById(id)
                .map(BacklogMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<TicketResponseDTO>> findTickets(@PathVariable Long id) {
        if (backlogService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<TicketResponseDTO> response = backlogService.findTicketsByBacklogId(id).stream()
                .map(TicketMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BacklogResponseDTO> create(@Valid @RequestBody BacklogRequestDTO requestDTO) {
        BacklogResponseDTO response = BacklogMapper.toResponse(backlogService.createFromDTO(requestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BacklogResponseDTO> update(@PathVariable Long id,
                                                     @Valid @RequestBody BacklogRequestDTO requestDTO) {
        return backlogService.updateFromDTO(id, requestDTO)
                .map(BacklogMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!backlogService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
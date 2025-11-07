package com.accenture.aria.controller;

import com.accenture.aria.dto.TicketRequestDTO;
import com.accenture.aria.dto.TicketResponseDTO;
import com.accenture.aria.model.Ticket;
import com.accenture.aria.service.TicketMapper;
import com.accenture.aria.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @GetMapping
    public List<TicketResponseDTO> listAll() {
        return service.findAll().stream().map(TicketMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(TicketMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> create(@Valid @RequestBody TicketRequestDTO dto) {
        Ticket toCreate = TicketMapper.toEntity(dto);
        Ticket created = service.create(toCreate);
        TicketResponseDTO resp = TicketMapper.toResponse(created);
        return ResponseEntity.created(URI.create("/api/tickets/" + created.getId())).body(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> update(@PathVariable Long id, @Valid @RequestBody TicketRequestDTO dto) {
        Ticket updatedEntity = TicketMapper.toEntity(dto);
        return service.update(id, updatedEntity)
                .map(TicketMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.accenture.aria.controller;

import com.accenture.aria.dto.CommentRequestDTO;
import com.accenture.aria.dto.CommentResponseDTO;
import com.accenture.aria.dto.TicketRequestDTO;
import com.accenture.aria.dto.TicketResponseDTO;
import com.accenture.aria.dto.TicketStatusUpdateRequestDTO;
import com.accenture.aria.service.CommentMapper;
import com.accenture.aria.service.CommentService;
import com.accenture.aria.service.TicketMapper;
import com.accenture.aria.service.TicketService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final CommentService commentService;

    public TicketController(TicketService ticketService, CommentService commentService) {
        this.ticketService = ticketService;
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> findAll() {
        List<TicketResponseDTO> response = ticketService.findAll().stream()
                .map(TicketMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> findById(@PathVariable Long id) {
        return ticketService.findById(id)
                .map(TicketMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> create(@Valid @RequestBody TicketRequestDTO requestDTO) {
        TicketResponseDTO response = TicketMapper.toResponse(ticketService.createFromDTO(requestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> update(@PathVariable Long id,
                                                    @Valid @RequestBody TicketRequestDTO requestDTO) {
        return ticketService.updateFromDTO(id, requestDTO)
                .map(TicketMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponseDTO> updateStatus(@PathVariable Long id,
                                                          @Valid @RequestBody TicketStatusUpdateRequestDTO requestDTO) {
        return ticketService.updateStatus(id, requestDTO.getStatus())
                .map(TicketMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!ticketService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable Long id,
                                                            @Valid @RequestBody CommentRequestDTO requestDTO) {
        CommentResponseDTO response = CommentMapper.toResponse(commentService.createComment(id, requestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

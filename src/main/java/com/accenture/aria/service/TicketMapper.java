package com.accenture.aria.service;

import com.accenture.aria.dto.TicketRequestDTO;
import com.accenture.aria.dto.TicketResponseDTO;
import com.accenture.aria.model.Ticket;

public class TicketMapper {

    public static Ticket toEntity(TicketRequestDTO dto) {
        if (dto == null) return null;
        return Ticket.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .priority(dto.getPriority())
                .reporter(dto.getReporter())
                .assignee(dto.getAssignee())
                .build();
    }

    public static TicketResponseDTO toResponse(Ticket t) {
        if (t == null) return null;
        return TicketResponseDTO.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .status(t.getStatus())
                .priority(t.getPriority())
                .reporter(t.getReporter())
                .assignee(t.getAssignee())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }
}

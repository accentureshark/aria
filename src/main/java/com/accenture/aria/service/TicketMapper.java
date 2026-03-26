package com.accenture.aria.service;

import com.accenture.aria.dto.TicketRequestDTO;
import com.accenture.aria.dto.TicketResponseDTO;
import com.accenture.aria.model.Ticket;

public class TicketMapper {

    private TicketMapper() {
    }

    public static Ticket toEntity(TicketRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Ticket.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .priority(dto.getPriority())
                .build();
    }

    public static TicketResponseDTO toResponse(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        return TicketResponseDTO.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .reporter(PersonMapper.toResponse(ticket.getReporter()))
                .assignee(PersonMapper.toResponse(ticket.getAssignee()))
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}


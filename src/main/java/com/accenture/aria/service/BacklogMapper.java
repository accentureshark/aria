package com.accenture.aria.service;

import com.accenture.aria.dto.BacklogRequestDTO;
import com.accenture.aria.dto.BacklogResponseDTO;
import com.accenture.aria.model.Backlog;

public final class BacklogMapper {

    private BacklogMapper() {
    }

    public static Backlog toEntity(BacklogRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Backlog.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public static BacklogResponseDTO toResponse(Backlog backlog) {
        if (backlog == null) {
            return null;
        }

        return BacklogResponseDTO.builder()
                .id(backlog.getId())
                .name(backlog.getName())
                .description(backlog.getDescription())
                .createdAt(backlog.getCreatedAt())
                .updatedAt(backlog.getUpdatedAt())
                .build();
    }
}
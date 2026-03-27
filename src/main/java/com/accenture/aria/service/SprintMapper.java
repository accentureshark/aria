package com.accenture.aria.service;

import com.accenture.aria.dto.SprintRequestDTO;
import com.accenture.aria.dto.SprintResponseDTO;
import com.accenture.aria.model.Sprint;

public class SprintMapper {

    private SprintMapper() {
    }

    public static Sprint toEntity(SprintRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Sprint.builder()
                .name(dto.getName())
                .goal(dto.getGoal())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(dto.getStatus())
                .build();
    }

    public static SprintResponseDTO toResponse(Sprint sprint) {
        if (sprint == null) {
            return null;
        }

        return SprintResponseDTO.builder()
                .id(sprint.getId())
                .name(sprint.getName())
                .goal(sprint.getGoal())
                .startDate(sprint.getStartDate())
                .endDate(sprint.getEndDate())
                .status(sprint.getStatus())
                .createdAt(sprint.getCreatedAt())
                .updatedAt(sprint.getUpdatedAt())
                .build();
    }
}
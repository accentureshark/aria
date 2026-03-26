package com.accenture.aria.dto;

import com.accenture.aria.model.Priority;
import com.accenture.aria.model.Status;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDTO {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private PersonResponseDTO reporter;
    private PersonResponseDTO assignee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


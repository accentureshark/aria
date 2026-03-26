package com.accenture.aria.dto;

import com.accenture.aria.model.Priority;
import com.accenture.aria.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDTO {

    @NotBlank
    @Size(max = 140)
    private String title;

    @Size(max = 2000)
    private String description;

    private Status status;
    private Priority priority;

    private Long reporterId;
    private Long assigneeId;
}


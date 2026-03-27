package com.accenture.aria.dto;

import com.accenture.aria.model.SprintStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SprintRequestDTO {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String goal;

    private LocalDate startDate;

    private LocalDate endDate;

    private SprintStatus status;
}
package com.accenture.aria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String department;
    private Boolean active;
}


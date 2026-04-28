package com.accenture.aria.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {

    private Long id;

    private String author;

    private String content;

    private Long ticketId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

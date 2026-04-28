package com.accenture.aria.service;

import com.accenture.aria.dto.CommentRequestDTO;
import com.accenture.aria.dto.CommentResponseDTO;
import com.accenture.aria.model.Comment;

public class CommentMapper {

    private CommentMapper() {
    }

    public static Comment toEntity(CommentRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Comment.builder()
                .author(dto.getAuthor())
                .content(dto.getContent())
                .build();
    }

    public static CommentResponseDTO toResponse(Comment comment) {
        if (comment == null) {
            return null;
        }

        return CommentResponseDTO.builder()
                .id(comment.getId())
                .author(comment.getAuthor())
                .content(comment.getContent())
                .ticketId(comment.getTicket() != null ? comment.getTicket().getId() : null)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}

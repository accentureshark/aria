package com.accenture.aria.service;

import com.accenture.aria.dto.CommentRequestDTO;
import com.accenture.aria.model.Comment;
import com.accenture.aria.model.Ticket;
import com.accenture.aria.repository.CommentRepository;
import com.accenture.aria.repository.TicketRepository;
import com.accenture.aria.service.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;

    public CommentService(CommentRepository commentRepository, TicketRepository ticketRepository) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
    }

    public Comment createComment(Long ticketId, CommentRequestDTO dto) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketId));

        Comment comment = CommentMapper.toEntity(dto);
        comment.setTicket(ticket);
        LocalDateTime now = LocalDateTime.now();
        comment.setCreatedAt(now);
        comment.setUpdatedAt(now);

        return commentRepository.save(comment);
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    public boolean delete(Long id) {
        if (!commentRepository.existsById(id)) {
            return false;
        }
        commentRepository.deleteById(id);
        return true;
    }
}

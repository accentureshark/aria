package com.accenture.aria.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.accenture.aria.dto.CommentRequestDTO;
import com.accenture.aria.model.Comment;
import com.accenture.aria.model.Priority;
import com.accenture.aria.model.Status;
import com.accenture.aria.model.Ticket;
import com.accenture.aria.repository.CommentRepository;
import com.accenture.aria.repository.TicketRepository;
import com.accenture.aria.service.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void createComment_withValidData_createsAndReturnsComment() {
        Long ticketId = 1L;
        Ticket ticket = buildTicket(ticketId);
        CommentRequestDTO dto = CommentRequestDTO.builder()
                .author("John Doe")
                .content("This is a test comment")
                .build();
        Comment savedComment = Comment.builder()
                .id(1L)
                .author("John Doe")
                .content("This is a test comment")
                .ticket(ticket)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        Comment result = commentService.createComment(ticketId, dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAuthor()).isEqualTo("John Doe");
        assertThat(result.getContent()).isEqualTo("This is a test comment");
        assertThat(result.getTicket().getId()).isEqualTo(ticketId);
        verify(ticketRepository).findById(ticketId);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void createComment_whenTicketDoesNotExist_throwsResourceNotFoundException() {
        Long ticketId = 99L;
        CommentRequestDTO dto = CommentRequestDTO.builder()
                .author("John Doe")
                .content("This is a test comment")
                .build();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.createComment(ticketId, dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ticket not found: " + ticketId);
    }

    @Test
    void findById_withValidId_returnsComment() {
        Long commentId = 1L;
        Comment comment = buildComment(commentId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        Optional<Comment> result = commentService.findById(commentId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(commentId);
        verify(commentRepository).findById(commentId);
    }

    @Test
    void findById_withNonExistentId_returnsEmpty() {
        Long commentId = 99L;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        Optional<Comment> result = commentService.findById(commentId);

        assertThat(result).isEmpty();
    }

    @Test
    void delete_withValidId_deletesAndReturnsTrue() {
        Long commentId = 1L;

        when(commentRepository.existsById(commentId)).thenReturn(true);

        boolean result = commentService.delete(commentId);

        assertThat(result).isTrue();
        verify(commentRepository).existsById(commentId);
        verify(commentRepository).deleteById(commentId);
    }

    @Test
    void delete_withNonExistentId_returnsFalse() {
        Long commentId = 99L;

        when(commentRepository.existsById(commentId)).thenReturn(false);

        boolean result = commentService.delete(commentId);

        assertThat(result).isFalse();
    }

    private Ticket buildTicket(Long id) {
        return Ticket.builder()
                .id(id)
                .title("Test Ticket")
                .description("Test Description")
                .status(Status.TODO)
                .priority(Priority.MEDIUM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private Comment buildComment(Long id) {
        return Comment.builder()
                .id(id)
                .author("John Doe")
                .content("Test comment")
                .ticket(buildTicket(1L))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}

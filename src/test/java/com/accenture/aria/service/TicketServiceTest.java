package com.accenture.aria.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.accenture.aria.model.Priority;
import com.accenture.aria.model.Status;
import com.accenture.aria.model.Ticket;
import com.accenture.aria.repository.PersonRepository;
import com.accenture.aria.repository.TicketRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void updateStatus_existingTicket_updatesStatusAndTimestamp() {
        Ticket existing = buildTicket(1L, Status.TODO);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Ticket> result = ticketService.updateStatus(1L, "DONE");

        assertThat(result).isPresent();
        assertThat(result.get().getStatus()).isEqualTo(Status.DONE);
        assertThat(result.get().getUpdatedAt()).isNotNull();
    }

    @Test
    void updateStatus_nonExistingTicket_returnsEmpty() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Ticket> result = ticketService.updateStatus(99L, "IN_PROGRESS");

        assertThat(result).isEmpty();
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void updateStatus_invalidStatus_throwsBadRequestError() {
        assertThatThrownBy(() -> ticketService.updateStatus(1L, "INVALID"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid status. Allowed values: TODO, IN_PROGRESS, DONE");
        verify(ticketRepository, never()).findById(any());
    }

    private Ticket buildTicket(Long id, Status status) {
        return Ticket.builder()
                .id(id)
                .title("Ticket " + id)
                .description("Description")
                .status(status)
                .priority(Priority.MEDIUM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}

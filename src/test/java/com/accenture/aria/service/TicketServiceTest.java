package com.accenture.aria.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.accenture.aria.model.Backlog;
import com.accenture.aria.model.Priority;
import com.accenture.aria.model.Status;
import com.accenture.aria.model.Ticket;
import com.accenture.aria.repository.PersonRepository;
import com.accenture.aria.repository.TicketRepository;
import java.time.LocalDateTime;
import java.util.List;
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

    @Test
    void findBacklog_withoutPriority_returnsAllBacklogTickets() {
        Backlog backlog = Backlog.builder().id(1L).build();
        Ticket t1 = buildBacklogTicket(1L, Priority.HIGH, backlog);
        Ticket t2 = buildBacklogTicket(2L, Priority.LOW, backlog);
        when(ticketRepository.findAllBacklogTickets()).thenReturn(List.of(t1, t2));

        List<Ticket> result = ticketService.findBacklog(null, "created_at");

        assertThat(result).hasSize(2);
        verify(ticketRepository).findAllBacklogTickets();
    }

    @Test
    void findBacklog_withPriority_returnsFilteredTickets() {
        Backlog backlog = Backlog.builder().id(1L).build();
        Ticket t1 = buildBacklogTicket(1L, Priority.HIGH, backlog);
        when(ticketRepository.findBacklogTicketsByPriority(Priority.HIGH)).thenReturn(List.of(t1));

        List<Ticket> result = ticketService.findBacklog(Priority.HIGH, "created_at");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    void findBacklog_sortByPriority_returnsSortedByPriority() {
        Backlog backlog = Backlog.builder().id(1L).build();
        Ticket low = buildBacklogTicket(1L, Priority.LOW, backlog);
        Ticket high = buildBacklogTicket(2L, Priority.HIGH, backlog);
        Ticket urgent = buildBacklogTicket(3L, Priority.URGENT, backlog);
        when(ticketRepository.findAllBacklogTickets()).thenReturn(List.of(low, high, urgent));

        List<Ticket> result = ticketService.findBacklog(null, "priority");

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getPriority()).isEqualTo(Priority.URGENT);
        assertThat(result.get(1).getPriority()).isEqualTo(Priority.HIGH);
        assertThat(result.get(2).getPriority()).isEqualTo(Priority.LOW);
    }

    @Test
    void findBacklog_emptyBacklog_returnsEmptyList() {
        when(ticketRepository.findAllBacklogTickets()).thenReturn(List.of());

        List<Ticket> result = ticketService.findBacklog(null, "created_at");

        assertThat(result).isEmpty();
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

    private Ticket buildBacklogTicket(Long id, Priority priority, Backlog backlog) {
        return Ticket.builder()
                .id(id)
                .title("Backlog Ticket " + id)
                .description("Backlog ticket")
                .status(Status.TODO)
                .priority(priority)
                .backlog(backlog)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}

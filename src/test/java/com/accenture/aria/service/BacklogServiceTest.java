package com.accenture.aria.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.accenture.aria.dto.BacklogRequestDTO;
import com.accenture.aria.model.Backlog;
import com.accenture.aria.model.Ticket;
import com.accenture.aria.repository.BacklogRepository;
import com.accenture.aria.repository.TicketRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BacklogServiceTest {

    @Mock
    private BacklogRepository backlogRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private BacklogService backlogService;

    @Test
    void findAll_returnsAllBacklogs() {
        List<Backlog> backlogs = List.of(buildBacklog(1L, "Product Backlog"), buildBacklog(2L, "Tech Debt"));
        when(backlogRepository.findAll()).thenReturn(backlogs);

        List<Backlog> result = backlogService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Product Backlog");
    }

    @Test
    void findById_existingBacklog_returnsBacklog() {
        Backlog backlog = buildBacklog(1L, "Product Backlog");
        when(backlogRepository.findById(1L)).thenReturn(Optional.of(backlog));

        Optional<Backlog> result = backlogService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Product Backlog");
    }

    @Test
    void findById_nonExistingBacklog_returnsEmpty() {
        when(backlogRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Backlog> result = backlogService.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void findTicketsByBacklogId_returnsTicketsForBacklog() {
        List<Ticket> tickets = List.of(
                Ticket.builder().id(1L).title("Fix bug").build(),
                Ticket.builder().id(2L).title("Add feature").build());
        when(ticketRepository.findByBacklogId(1L)).thenReturn(tickets);

        List<Ticket> result = backlogService.findTicketsByBacklogId(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Fix bug");
    }

    @Test
    void create_setsTimestamps() {
        Backlog backlog = Backlog.builder().name("Product Backlog").build();
        when(backlogRepository.save(any(Backlog.class))).thenAnswer(inv -> inv.getArgument(0));

        Backlog result = backlogService.create(backlog);

        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    void createFromDTO_persistsBacklog() {
        BacklogRequestDTO dto = BacklogRequestDTO.builder()
                .name("Product Backlog")
                .description("All unassigned tickets")
                .build();
        when(backlogRepository.save(any(Backlog.class))).thenAnswer(inv -> {
            Backlog b = inv.getArgument(0);
            b.setId(1L);
            return b;
        });

        Backlog result = backlogService.createFromDTO(dto);

        assertThat(result.getName()).isEqualTo("Product Backlog");
        assertThat(result.getDescription()).isEqualTo("All unassigned tickets");
    }

    @Test
    void update_existingBacklog_appliesChanges() {
        Backlog existing = buildBacklog(1L, "Product Backlog");
        Backlog updates = Backlog.builder().name("Product Backlog - Updated").build();
        when(backlogRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(backlogRepository.save(any(Backlog.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Backlog> result = backlogService.update(1L, updates);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Product Backlog - Updated");
        assertThat(result.get().getUpdatedAt()).isNotNull();
    }

    @Test
    void update_nonExistingBacklog_returnsEmpty() {
        when(backlogRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Backlog> result = backlogService.update(99L, Backlog.builder().name("x").build());

        assertThat(result).isEmpty();
        verify(backlogRepository, never()).save(any());
    }

    @Test
    void delete_existingBacklog_returnsTrue() {
        when(backlogRepository.existsById(1L)).thenReturn(true);

        boolean result = backlogService.delete(1L);

        assertThat(result).isTrue();
        verify(backlogRepository).deleteById(1L);
    }

    @Test
    void delete_nonExistingBacklog_returnsFalse() {
        when(backlogRepository.existsById(99L)).thenReturn(false);

        boolean result = backlogService.delete(99L);

        assertThat(result).isFalse();
        verify(backlogRepository, never()).deleteById(any());
    }

    private Backlog buildBacklog(Long id, String name) {
        return Backlog.builder()
                .id(id)
                .name(name)
                .description("Sample description")
                .build();
    }
}
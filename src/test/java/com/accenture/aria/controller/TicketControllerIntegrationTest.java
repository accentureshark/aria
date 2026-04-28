package com.accenture.aria.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.accenture.aria.model.Backlog;
import com.accenture.aria.model.Priority;
import com.accenture.aria.model.Status;
import com.accenture.aria.model.Ticket;
import com.accenture.aria.repository.BacklogRepository;
import com.accenture.aria.repository.TicketRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @BeforeEach
    void cleanDatabase() {
        ticketRepository.deleteAll();
        backlogRepository.deleteAll();
    }

    @Test
    void patchStatus_withValidStatus_returnsUpdatedTicket() throws Exception {
        Ticket saved = ticketRepository.save(buildTicket(Status.TODO));

        mockMvc.perform(patch("/api/tickets/{id}/status", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"DONE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void patchStatus_withInvalidStatus_returnsBadRequest() throws Exception {
        Ticket saved = ticketRepository.save(buildTicket(Status.TODO));

        mockMvc.perform(patch("/api/tickets/{id}/status", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"INVALID\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Invalid status")));
    }

    @Test
    void patchStatus_whenTicketDoesNotExist_returnsNotFound() throws Exception {
        mockMvc.perform(patch("/api/tickets/{id}/status", 9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBacklog_withoutFilters_returnsAllBacklogTickets() throws Exception {
        Backlog backlog = backlogRepository.save(Backlog.builder().name("Test Backlog").build());
        ticketRepository.save(buildBacklogTicket("Backlog 1", Priority.HIGH, backlog));
        ticketRepository.save(buildBacklogTicket("Backlog 2", Priority.MEDIUM, backlog));

        mockMvc.perform(get("/api/tickets/backlog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getBacklog_withPriorityFilter_returnsFilteredTickets() throws Exception {
        Backlog backlog = backlogRepository.save(Backlog.builder().name("Test Backlog").build());
        ticketRepository.save(buildBacklogTicket("High 1", Priority.HIGH, backlog));
        ticketRepository.save(buildBacklogTicket("Low 1", Priority.LOW, backlog));
        ticketRepository.save(buildBacklogTicket("High 2", Priority.HIGH, backlog));

        mockMvc.perform(get("/api/tickets/backlog?priority=HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getBacklog_emptyBacklog_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/tickets/backlog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getBacklog_invalidPriority_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/tickets/backlog?priority=INVALID"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBacklog_sortByPriority_returnsSortedByPriority() throws Exception {
        Backlog backlog = backlogRepository.save(Backlog.builder().name("Test Backlog").build());
        ticketRepository.save(buildBacklogTicket("Low", Priority.LOW, backlog));
        ticketRepository.save(buildBacklogTicket("Urgent", Priority.URGENT, backlog));
        ticketRepository.save(buildBacklogTicket("High", Priority.HIGH, backlog));

        mockMvc.perform(get("/api/tickets/backlog?sort=priority"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].priority").value("URGENT"))
                .andExpect(jsonPath("$[1].priority").value("HIGH"))
                .andExpect(jsonPath("$[2].priority").value("LOW"));
    }

    private Ticket buildBacklogTicket(String title, Priority priority, Backlog backlog) {
        return Ticket.builder()
                .title(title)
                .description("Backlog ticket")
                .status(Status.TODO)
                .priority(priority)
                .backlog(backlog)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private Ticket buildTicket(Status status) {
        return Ticket.builder()
                .title("Integration ticket")
                .description("Integration test")
                .status(status)
                .priority(Priority.MEDIUM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}

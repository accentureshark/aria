package com.accenture.aria.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.accenture.aria.model.Priority;
import com.accenture.aria.model.Status;
import com.accenture.aria.model.Ticket;
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

    @BeforeEach
    void cleanDatabase() {
        ticketRepository.deleteAll();
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
    void search_withPriority_returnsFilteredTickets() throws Exception {
        ticketRepository.save(buildTicket("Pago atrasado", Status.TODO, Priority.HIGH));
        ticketRepository.save(buildTicket("Pago pendiente", Status.IN_PROGRESS, Priority.MEDIUM));
        ticketRepository.save(buildTicket("Error login", Status.TODO, Priority.HIGH));

        mockMvc.perform(get("/api/tickets/search")
                        .queryParam("priority", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].priority").value("HIGH"))
                .andExpect(jsonPath("$[1].priority").value("HIGH"));
    }

    @Test
    void search_withInvalidPriority_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/tickets/search")
                        .queryParam("priority", "INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Invalid prioridad")));
    }

    @Test
    void search_withNoPriority_returnsAllTickets() throws Exception {
        ticketRepository.save(buildTicket("Pago atrasado", Status.TODO, Priority.HIGH));
        ticketRepository.save(buildTicket("Pago pendiente", Status.IN_PROGRESS, Priority.MEDIUM));

        mockMvc.perform(get("/api/tickets/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void search_withPriorityNoResults_returnsEmptyList() throws Exception {
        ticketRepository.save(buildTicket("Pago atrasado", Status.TODO, Priority.LOW));

        mockMvc.perform(get("/api/tickets/search")
                        .queryParam("priority", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private Ticket buildTicket(Status status) {
        return buildTicket("Integration ticket", status, Priority.MEDIUM);
    }

    private Ticket buildTicket(String title, Status status, Priority priority) {
        return Ticket.builder()
                .title(title)
                .description("Integration test")
                .status(status)
                .priority(priority)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}

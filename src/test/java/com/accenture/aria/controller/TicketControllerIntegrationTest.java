package com.accenture.aria.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.accenture.aria.model.Priority;
import com.accenture.aria.model.Status;
import com.accenture.aria.model.Ticket;
import com.accenture.aria.repository.CommentRepository;
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
    private CommentRepository commentRepository;

    @BeforeEach
    void cleanDatabase() {
        commentRepository.deleteAll();
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
    void createComment_withValidData_returns201AndCreatedComment() throws Exception {
        Ticket saved = ticketRepository.save(buildTicket(Status.TODO));

        mockMvc.perform(post("/api/tickets/{id}/comments", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"author\":\"John Doe\",\"content\":\"This is a test comment\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.author").value("John Doe"))
                .andExpect(jsonPath("$.content").value("This is a test comment"))
                .andExpect(jsonPath("$.ticketId").value(saved.getId()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    void createComment_whenTicketDoesNotExist_returns404() throws Exception {
        mockMvc.perform(post("/api/tickets/{id}/comments", 9999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"author\":\"John Doe\",\"content\":\"This is a test comment\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createComment_withMissingAuthor_returnsBadRequest() throws Exception {
        Ticket saved = ticketRepository.save(buildTicket(Status.TODO));

        mockMvc.perform(post("/api/tickets/{id}/comments", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"This is a test comment\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createComment_withMissingContent_returnsBadRequest() throws Exception {
        Ticket saved = ticketRepository.save(buildTicket(Status.TODO));

        mockMvc.perform(post("/api/tickets/{id}/comments", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"author\":\"John Doe\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createComment_withBlankAuthor_returnsBadRequest() throws Exception {
        Ticket saved = ticketRepository.save(buildTicket(Status.TODO));

        mockMvc.perform(post("/api/tickets/{id}/comments", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"author\":\"\",\"content\":\"This is a test comment\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createComment_withBlankContent_returnsBadRequest() throws Exception {
        Ticket saved = ticketRepository.save(buildTicket(Status.TODO));

        mockMvc.perform(post("/api/tickets/{id}/comments", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"author\":\"John Doe\",\"content\":\"\"}"))
                .andExpect(status().isBadRequest());
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

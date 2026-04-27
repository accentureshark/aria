package com.accenture.aria.controller;

import static org.hamcrest.Matchers.closeTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.accenture.aria.model.Priority;
import com.accenture.aria.model.Sprint;
import com.accenture.aria.model.SprintStatus;
import com.accenture.aria.model.Status;
import com.accenture.aria.model.Ticket;
import com.accenture.aria.repository.SprintRepository;
import com.accenture.aria.repository.TicketRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SprintControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void cleanDatabase() {
        ticketRepository.deleteAll();
        sprintRepository.deleteAll();
    }

    @Test
    void getSummary_existingSprint_returnsSprintSummary() throws Exception {
        Sprint sprint = sprintRepository.save(buildSprint());
        createAndSaveTickets(sprint);

        mockMvc.perform(get("/api/sprints/{id}/summary", sprint.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sprint.getId()))
                .andExpect(jsonPath("$.name").value("Sprint 1"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.totalTickets").value(10))
                .andExpect(jsonPath("$.ticketsByPriority.low").value(2))
                .andExpect(jsonPath("$.ticketsByPriority.medium").value(5))
                .andExpect(jsonPath("$.ticketsByPriority.high").value(2))
                .andExpect(jsonPath("$.ticketsByPriority.urgent").value(1))
                .andExpect(jsonPath("$.ticketsByStatus.low").value(3))
                .andExpect(jsonPath("$.ticketsByStatus.medium").value(6))
                .andExpect(jsonPath("$.ticketsByStatus.high").value(1));
    }

    @Test
    void getSummary_nonExistingSprint_returns404() throws Exception {
        mockMvc.perform(get("/api/sprints/{id}/summary", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSummary_sprintWithNoTickets_returnsZeroCounts() throws Exception {
        Sprint sprint = sprintRepository.save(buildSprint());

        mockMvc.perform(get("/api/sprints/{id}/summary", sprint.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTickets").value(0))
                .andExpect(jsonPath("$.ticketsByPriority.low").value(0))
                .andExpect(jsonPath("$.ticketsByPriority.medium").value(0))
                .andExpect(jsonPath("$.ticketsByPriority.high").value(0))
                .andExpect(jsonPath("$.ticketsByPriority.urgent").value(0))
                .andExpect(jsonPath("$.ticketsByStatus.low").value(0))
                .andExpect(jsonPath("$.ticketsByStatus.medium").value(0))
                .andExpect(jsonPath("$.ticketsByStatus.high").value(0));
    }

    private void createAndSaveTickets(Sprint sprint) {
        ticketRepository.save(buildTicket(sprint, Priority.LOW, Status.TODO));
        ticketRepository.save(buildTicket(sprint, Priority.LOW, Status.IN_PROGRESS));
        ticketRepository.save(buildTicket(sprint, Priority.MEDIUM, Status.IN_PROGRESS));
        ticketRepository.save(buildTicket(sprint, Priority.MEDIUM, Status.IN_PROGRESS));
        ticketRepository.save(buildTicket(sprint, Priority.MEDIUM, Status.IN_PROGRESS));
        ticketRepository.save(buildTicket(sprint, Priority.MEDIUM, Status.IN_PROGRESS));
        ticketRepository.save(buildTicket(sprint, Priority.MEDIUM, Status.TODO));
        ticketRepository.save(buildTicket(sprint, Priority.HIGH, Status.DONE));
        ticketRepository.save(buildTicket(sprint, Priority.HIGH, Status.IN_PROGRESS));
        ticketRepository.save(buildTicket(sprint, Priority.URGENT, Status.TODO));
    }

    private Sprint buildSprint() {
        return Sprint.builder()
                .name("Sprint 1")
                .goal("Deliver features")
                .status(SprintStatus.ACTIVE)
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 1, 14))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private Ticket buildTicket(Sprint sprint, Priority priority, Status status) {
        return Ticket.builder()
                .title("Ticket")
                .description("Description")
                .status(status)
                .priority(priority)
                .sprint(sprint)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}

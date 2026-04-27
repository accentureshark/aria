package com.accenture.aria.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.accenture.aria.model.Person;
import com.accenture.aria.model.Ticket;
import com.accenture.aria.repository.PersonRepository;
import com.accenture.aria.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void cleanDatabase() {
        ticketRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    void findAssignedTickets_withValidPersonAndTickets_returnsTickets() throws Exception {
        Person person = personRepository.save(buildPerson("alice", "alice@example.com", "Alice", "Engineering", true));
        Ticket ticket1 = ticketRepository.save(buildTicket("Task 1", person));
        Ticket ticket2 = ticketRepository.save(buildTicket("Task 2", person));

        mockMvc.perform(get("/api/persons/{id}/tickets", person.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    void findAssignedTickets_withPersonWithoutTickets_returnsEmptyList() throws Exception {
        Person person = personRepository.save(buildPerson("bob", "bob@example.com", "Bob", "Support", true));

        mockMvc.perform(get("/api/persons/{id}/tickets", person.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void findAssignedTickets_withNonExistentPerson_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/persons/{id}/tickets", 99L))
                .andExpect(status().isNotFound());
    }

    private Person buildPerson(String username, String email, String fullName, String department, boolean active) {
        return Person.builder()
                .username(username)
                .email(email)
                .fullName(fullName)
                .department(department)
                .active(active)
                .build();
    }

    private Ticket buildTicket(String title, Person assignee) {
        return Ticket.builder()
                .title(title)
                .description("Description for " + title)
                .assignee(assignee)
                .build();
    }
}

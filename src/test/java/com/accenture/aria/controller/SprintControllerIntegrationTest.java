package com.accenture.aria.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.accenture.aria.model.Sprint;
import com.accenture.aria.model.SprintStatus;
import com.accenture.aria.repository.SprintRepository;
import java.time.LocalDate;
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

    @BeforeEach
    void cleanDatabase() {
        sprintRepository.deleteAll();
    }

    @Test
    void findActive_returnsOnlyActiveSprints() throws Exception {
        sprintRepository.save(buildSprint("Sprint Active", SprintStatus.ACTIVE));
        sprintRepository.save(buildSprint("Sprint Planned", SprintStatus.PLANNED));

        mockMvc.perform(get("/api/sprints/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Sprint Active"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    private Sprint buildSprint(String name, SprintStatus status) {
        return Sprint.builder()
                .name(name)
                .goal("Deliver features")
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 1, 15))
                .status(status)
                .build();
    }
}

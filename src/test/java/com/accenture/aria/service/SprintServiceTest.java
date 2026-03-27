package com.accenture.aria.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.accenture.aria.dto.SprintRequestDTO;
import com.accenture.aria.model.Sprint;
import com.accenture.aria.model.SprintStatus;
import com.accenture.aria.repository.SprintRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SprintServiceTest {

    @Mock
    private SprintRepository sprintRepository;

    @InjectMocks
    private SprintService sprintService;

    @Test
    void findAll_returnsAllSprints() {
        List<Sprint> sprints = List.of(buildSprint(1L, "Sprint 1"), buildSprint(2L, "Sprint 2"));
        when(sprintRepository.findAll()).thenReturn(sprints);

        List<Sprint> result = sprintService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Sprint 1");
    }

    @Test
    void findById_existingSprint_returnsSprint() {
        Sprint sprint = buildSprint(1L, "Sprint 1");
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(sprint));

        Optional<Sprint> result = sprintService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Sprint 1");
    }

    @Test
    void findById_nonExistingSprint_returnsEmpty() {
        when(sprintRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Sprint> result = sprintService.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void findByStatus_returnsFilteredSprints() {
        List<Sprint> activeSprints = List.of(buildSprint(1L, "Sprint 1"));
        when(sprintRepository.findByStatus(SprintStatus.ACTIVE)).thenReturn(activeSprints);

        List<Sprint> result = sprintService.findByStatus(SprintStatus.ACTIVE);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(SprintStatus.ACTIVE);
    }

    @Test
    void create_setsDefaultStatusAndTimestamps() {
        Sprint sprint = Sprint.builder().name("Sprint 1").build();
        when(sprintRepository.save(any(Sprint.class))).thenAnswer(inv -> inv.getArgument(0));

        Sprint result = sprintService.create(sprint);

        assertThat(result.getStatus()).isEqualTo(SprintStatus.PLANNED);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    void createFromDTO_persistsSprint() {
        SprintRequestDTO dto = SprintRequestDTO.builder()
                .name("Sprint 1")
                .goal("Deliver auth module")
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 1, 14))
                .build();
        when(sprintRepository.save(any(Sprint.class))).thenAnswer(inv -> {
            Sprint s = inv.getArgument(0);
            s.setId(1L);
            return s;
        });

        Sprint result = sprintService.createFromDTO(dto);

        assertThat(result.getName()).isEqualTo("Sprint 1");
        assertThat(result.getGoal()).isEqualTo("Deliver auth module");
        assertThat(result.getStatus()).isEqualTo(SprintStatus.PLANNED);
    }

    @Test
    void update_existingSprint_appliesChanges() {
        Sprint existing = buildSprint(1L, "Sprint 1");
        Sprint updates = Sprint.builder().name("Sprint 1 - Updated").build();
        when(sprintRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(sprintRepository.save(any(Sprint.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Sprint> result = sprintService.update(1L, updates);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Sprint 1 - Updated");
        assertThat(result.get().getUpdatedAt()).isNotNull();
    }

    @Test
    void update_nonExistingSprint_returnsEmpty() {
        when(sprintRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Sprint> result = sprintService.update(99L, Sprint.builder().name("x").build());

        assertThat(result).isEmpty();
        verify(sprintRepository, never()).save(any());
    }

    @Test
    void delete_existingSprint_returnsTrue() {
        when(sprintRepository.existsById(1L)).thenReturn(true);

        boolean result = sprintService.delete(1L);

        assertThat(result).isTrue();
        verify(sprintRepository).deleteById(1L);
    }

    @Test
    void delete_nonExistingSprint_returnsFalse() {
        when(sprintRepository.existsById(99L)).thenReturn(false);

        boolean result = sprintService.delete(99L);

        assertThat(result).isFalse();
        verify(sprintRepository, never()).deleteById(any());
    }

    private Sprint buildSprint(Long id, String name) {
        return Sprint.builder()
                .id(id)
                .name(name)
                .status(SprintStatus.ACTIVE)
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 1, 14))
                .build();
    }
}
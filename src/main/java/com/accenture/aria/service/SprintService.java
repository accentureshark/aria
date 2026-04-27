package com.accenture.aria.service;

import com.accenture.aria.dto.SprintRequestDTO;
import com.accenture.aria.dto.SprintSummaryResponseDTO;
import com.accenture.aria.model.Priority;
import com.accenture.aria.model.Sprint;
import com.accenture.aria.model.SprintStatus;
import com.accenture.aria.model.Status;
import com.accenture.aria.repository.SprintRepository;
import com.accenture.aria.repository.TicketRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class SprintService {

    private final SprintRepository sprintRepository;
    private final TicketRepository ticketRepository;

    public SprintService(SprintRepository sprintRepository, TicketRepository ticketRepository) {
        this.sprintRepository = sprintRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<Sprint> findAll() {
        return sprintRepository.findAll();
    }

    public Optional<Sprint> findById(Long id) {
        return sprintRepository.findById(id);
    }

    public List<Sprint> findByStatus(SprintStatus status) {
        return sprintRepository.findByStatus(status);
    }

    public Sprint create(Sprint sprint) {
        if (sprint.getStatus() == null) {
            sprint.setStatus(SprintStatus.PLANNED);
        }
        LocalDateTime now = LocalDateTime.now();
        sprint.setCreatedAt(now);
        sprint.setUpdatedAt(now);
        return sprintRepository.save(sprint);
    }

    public Sprint createFromDTO(SprintRequestDTO dto) {
        return create(SprintMapper.toEntity(dto));
    }

    public Optional<Sprint> update(Long id, Sprint updates) {
        return sprintRepository.findById(id)
                .map(existing -> {
                    applyPartialUpdate(existing, updates);
                    existing.setUpdatedAt(LocalDateTime.now());
                    return sprintRepository.save(existing);
                });
    }

    public Optional<Sprint> updateFromDTO(Long id, SprintRequestDTO dto) {
        return update(id, SprintMapper.toEntity(dto));
    }

    public boolean delete(Long id) {
        if (!sprintRepository.existsById(id)) {
            return false;
        }
        sprintRepository.deleteById(id);
        return true;
    }

    public Optional<SprintSummaryResponseDTO> getSummary(Long id) {
        return sprintRepository.findById(id)
                .map(sprint -> {
                    Integer totalTickets = ticketRepository.countBySprintId(id);
                    Integer lowCount = ticketRepository.countBySprintIdAndPriority(id, Priority.LOW);
                    Integer mediumCount = ticketRepository.countBySprintIdAndPriority(id, Priority.MEDIUM);
                    Integer highCount = ticketRepository.countBySprintIdAndPriority(id, Priority.HIGH);
                    Integer urgentCount = ticketRepository.countBySprintIdAndPriority(id, Priority.URGENT);

                    Integer todoCount = ticketRepository.countBySprintIdAndStatus(id, Status.TODO);
                    Integer inProgressCount = ticketRepository.countBySprintIdAndStatus(id, Status.IN_PROGRESS);
                    Integer doneCount = ticketRepository.countBySprintIdAndStatus(id, Status.DONE);

                    SprintSummaryResponseDTO.TicketStats priorityStats = SprintSummaryResponseDTO.TicketStats.builder()
                            .low(lowCount)
                            .medium(mediumCount)
                            .high(highCount)
                            .urgent(urgentCount)
                            .build();

                    SprintSummaryResponseDTO.TicketStats statusStats = SprintSummaryResponseDTO.TicketStats.builder()
                            .low(todoCount)
                            .medium(inProgressCount)
                            .high(doneCount)
                            .build();

                    return SprintSummaryResponseDTO.builder()
                            .id(sprint.getId())
                            .name(sprint.getName())
                            .status(sprint.getStatus())
                            .totalTickets(totalTickets)
                            .ticketsByPriority(priorityStats)
                            .ticketsByStatus(statusStats)
                            .build();
                });
    }

    private void applyPartialUpdate(Sprint existing, Sprint updates) {
        if (updates.getName() != null) {
            existing.setName(updates.getName());
        }
        if (updates.getGoal() != null) {
            existing.setGoal(updates.getGoal());
        }
        if (updates.getStartDate() != null) {
            existing.setStartDate(updates.getStartDate());
        }
        if (updates.getEndDate() != null) {
            existing.setEndDate(updates.getEndDate());
        }
        if (updates.getStatus() != null) {
            existing.setStatus(updates.getStatus());
        }
    }
}
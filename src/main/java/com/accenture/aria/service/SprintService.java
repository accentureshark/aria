package com.accenture.aria.service;

import com.accenture.aria.dto.SprintRequestDTO;
import com.accenture.aria.model.Sprint;
import com.accenture.aria.model.SprintStatus;
import com.accenture.aria.repository.SprintRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class SprintService {

    private final SprintRepository sprintRepository;

    public SprintService(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
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
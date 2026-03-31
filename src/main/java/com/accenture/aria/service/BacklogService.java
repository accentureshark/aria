package com.accenture.aria.service;

import com.accenture.aria.dto.BacklogRequestDTO;
import com.accenture.aria.model.Backlog;
import com.accenture.aria.model.Ticket;
import com.accenture.aria.repository.BacklogRepository;
import com.accenture.aria.repository.TicketRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BacklogService {

    private final BacklogRepository backlogRepository;
    private final TicketRepository ticketRepository;

    public BacklogService(BacklogRepository backlogRepository, TicketRepository ticketRepository) {
        this.backlogRepository = backlogRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<Backlog> findAll() {
        return backlogRepository.findAll();
    }

    public Optional<Backlog> findById(Long id) {
        return backlogRepository.findById(id);
    }

    public List<Ticket> findTicketsByBacklogId(Long backlogId) {
        return ticketRepository.findByBacklogId(backlogId);
    }

    public Backlog create(Backlog backlog) {
        LocalDateTime now = LocalDateTime.now();
        backlog.setCreatedAt(now);
        backlog.setUpdatedAt(now);
        return backlogRepository.save(backlog);
    }

    public Backlog createFromDTO(BacklogRequestDTO dto) {
        return create(BacklogMapper.toEntity(dto));
    }

    public Optional<Backlog> update(Long id, Backlog updates) {
        return backlogRepository.findById(id)
                .map(existing -> {
                    applyPartialUpdate(existing, updates);
                    existing.setUpdatedAt(LocalDateTime.now());
                    return backlogRepository.save(existing);
                });
    }

    public Optional<Backlog> updateFromDTO(Long id, BacklogRequestDTO dto) {
        return update(id, BacklogMapper.toEntity(dto));
    }

    public boolean delete(Long id) {
        if (!backlogRepository.existsById(id)) {
            return false;
        }
        backlogRepository.deleteById(id);
        return true;
    }

    private void applyPartialUpdate(Backlog existing, Backlog updates) {
        if (updates.getName() != null) {
            existing.setName(updates.getName());
        }
        if (updates.getDescription() != null) {
            existing.setDescription(updates.getDescription());
        }
    }
}
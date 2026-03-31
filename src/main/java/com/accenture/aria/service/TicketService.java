package com.accenture.aria.service;

import com.accenture.aria.dto.TicketRequestDTO;
import com.accenture.aria.model.Backlog;
import com.accenture.aria.model.Person;
import com.accenture.aria.model.Priority;
import com.accenture.aria.model.Status;
import com.accenture.aria.model.Ticket;
import com.accenture.aria.repository.BacklogRepository;
import com.accenture.aria.repository.PersonRepository;
import com.accenture.aria.repository.TicketRepository;
import com.accenture.aria.service.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final PersonRepository personRepository;
    private final BacklogRepository backlogRepository;

    public TicketService(TicketRepository ticketRepository, PersonRepository personRepository,
            BacklogRepository backlogRepository) {
        this.ticketRepository = ticketRepository;
        this.personRepository = personRepository;
        this.backlogRepository = backlogRepository;
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket create(Ticket ticket) {
        if (ticket.getStatus() == null) {
            ticket.setStatus(Status.OPEN);
        }
        if (ticket.getPriority() == null) {
            ticket.setPriority(Priority.MEDIUM);
        }
        LocalDateTime now = LocalDateTime.now();
        ticket.setCreatedAt(now);
        ticket.setUpdatedAt(now);
        return ticketRepository.save(ticket);
    }

    public Ticket createFromDTO(TicketRequestDTO dto) {
        Ticket ticket = TicketMapper.toEntity(dto);
        if (dto.getReporterId() != null) {
            ticket.setReporter(resolvePersonOrThrow(dto.getReporterId()));
        }
        if (dto.getAssigneeId() != null) {
            ticket.setAssignee(resolvePersonOrThrow(dto.getAssigneeId()));
        }
        if (dto.getBacklogId() != null) {
            ticket.setBacklog(resolveBacklogOrThrow(dto.getBacklogId()));
        }
        return create(ticket);
    }

    public Optional<Ticket> update(Long id, Ticket updates) {
        return ticketRepository.findById(id)
                .map(existing -> {
                    applyPartialUpdate(existing, updates);
                    existing.setUpdatedAt(LocalDateTime.now());
                    return ticketRepository.save(existing);
                });
    }

    public Optional<Ticket> updateFromDTO(Long id, TicketRequestDTO dto) {
        return ticketRepository.findById(id)
                .map(existing -> {
                    Ticket updates = TicketMapper.toEntity(dto);
                    applyPartialUpdate(existing, updates);
                    if (dto.getReporterId() != null) {
                        existing.setReporter(resolvePersonOrThrow(dto.getReporterId()));
                    }
                    if (dto.getAssigneeId() != null) {
                        existing.setAssignee(resolvePersonOrThrow(dto.getAssigneeId()));
                    }
                    if (dto.getBacklogId() != null) {
                        existing.setBacklog(resolveBacklogOrThrow(dto.getBacklogId()));
                    }
                    existing.setUpdatedAt(LocalDateTime.now());
                    return ticketRepository.save(existing);
                });
    }

    public boolean delete(Long id) {
        if (!ticketRepository.existsById(id)) {
            return false;
        }
        ticketRepository.deleteById(id);
        return true;
    }

    private void applyPartialUpdate(Ticket existing, Ticket updates) {
        if (updates.getTitle() != null) {
            existing.setTitle(updates.getTitle());
        }
        if (updates.getDescription() != null) {
            existing.setDescription(updates.getDescription());
        }
        if (updates.getStatus() != null) {
            existing.setStatus(updates.getStatus());
        }
        if (updates.getPriority() != null) {
            existing.setPriority(updates.getPriority());
        }
        if (updates.getReporter() != null) {
            existing.setReporter(updates.getReporter());
        }
        if (updates.getAssignee() != null) {
            existing.setAssignee(updates.getAssignee());
        }
    }

    private Person resolvePersonOrThrow(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found: " + personId));
    }

    private Backlog resolveBacklogOrThrow(Long backlogId) {
        return backlogRepository.findById(backlogId)
                .orElseThrow(() -> new ResourceNotFoundException("Backlog not found: " + backlogId));
    }
}


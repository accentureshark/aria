package com.accenture.aria.repository;

import com.accenture.aria.model.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByBacklogId(Long backlogId);
}


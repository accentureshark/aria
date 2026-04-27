package com.accenture.aria.repository;

import com.accenture.aria.model.Priority;
import com.accenture.aria.model.Status;
import com.accenture.aria.model.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByBacklogId(Long backlogId);

    List<Ticket> findBySprintId(Long sprintId);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.sprint.id = :sprintId AND t.priority = :priority")
    Integer countBySprintIdAndPriority(@Param("sprintId") Long sprintId, @Param("priority") Priority priority);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.sprint.id = :sprintId AND t.status = :status")
    Integer countBySprintIdAndStatus(@Param("sprintId") Long sprintId, @Param("status") Status status);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.sprint.id = :sprintId")
    Integer countBySprintId(@Param("sprintId") Long sprintId);
}


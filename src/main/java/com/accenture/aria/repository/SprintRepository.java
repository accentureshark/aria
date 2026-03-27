package com.accenture.aria.repository;

import com.accenture.aria.model.Sprint;
import com.accenture.aria.model.SprintStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprintRepository extends JpaRepository<Sprint, Long> {

    List<Sprint> findByStatus(SprintStatus status);
}
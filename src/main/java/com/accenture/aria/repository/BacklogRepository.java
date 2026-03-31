package com.accenture.aria.repository;

import com.accenture.aria.model.Backlog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BacklogRepository extends JpaRepository<Backlog, Long> {
}
package com.accenture.aria.repository;

import com.accenture.aria.model.Person;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);

    Optional<Person> findByUsername(String username);

    List<Person> findByDepartment(String department);
}


package com.accenture.aria.service;

import com.accenture.aria.dto.PersonRequestDTO;
import com.accenture.aria.model.Person;
import com.accenture.aria.repository.PersonRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);
    }

    public Optional<Person> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    public Optional<Person> findByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    public List<Person> findByDepartment(String department) {
        return personRepository.findByDepartment(department);
    }

    public Person create(Person person) {
        if (person.getActive() == null) {
            person.setActive(true);
        }
        return personRepository.save(person);
    }

    public Person createFromDTO(PersonRequestDTO dto) {
        return create(PersonMapper.toEntity(dto));
    }

    public Optional<Person> update(Long id, Person person) {
        return personRepository.findById(id)
                .map(existing -> {
                    applyPartialUpdate(existing, person);
                    return personRepository.save(existing);
                });
    }

    public Optional<Person> updateFromDTO(Long id, PersonRequestDTO dto) {
        return update(id, PersonMapper.toEntity(dto));
    }

    public boolean delete(Long id) {
        if (!personRepository.existsById(id)) {
            return false;
        }
        personRepository.deleteById(id);
        return true;
    }

    public Optional<Person> deactivate(Long id) {
        return personRepository.findById(id)
                .map(person -> {
                    person.setActive(false);
                    return personRepository.save(person);
                });
    }

    private void applyPartialUpdate(Person existing, Person updates) {
        if (updates.getUsername() != null) {
            existing.setUsername(updates.getUsername());
        }
        if (updates.getEmail() != null) {
            existing.setEmail(updates.getEmail());
        }
        if (updates.getFullName() != null) {
            existing.setFullName(updates.getFullName());
        }
        if (updates.getDepartment() != null) {
            existing.setDepartment(updates.getDepartment());
        }
        if (updates.getActive() != null) {
            existing.setActive(updates.getActive());
        }
    }
}


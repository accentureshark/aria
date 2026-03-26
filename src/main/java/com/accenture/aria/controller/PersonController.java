package com.accenture.aria.controller;

import com.accenture.aria.dto.PersonRequestDTO;
import com.accenture.aria.dto.PersonResponseDTO;
import com.accenture.aria.service.PersonMapper;
import com.accenture.aria.service.PersonService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<PersonResponseDTO>> findAll() {
        List<PersonResponseDTO> response = personService.findAll().stream()
                .map(PersonMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> findById(@PathVariable Long id) {
        return personService.findById(id)
                .map(PersonMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<PersonResponseDTO> findByEmail(@PathVariable String email) {
        return personService.findByEmail(email)
                .map(PersonMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<PersonResponseDTO> findByUsername(@PathVariable String username) {
        return personService.findByUsername(username)
                .map(PersonMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<PersonResponseDTO>> findByDepartment(@PathVariable String department) {
        List<PersonResponseDTO> response = personService.findByDepartment(department).stream()
                .map(PersonMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PersonResponseDTO> create(@Valid @RequestBody PersonRequestDTO requestDTO) {
        PersonResponseDTO response = PersonMapper.toResponse(personService.createFromDTO(requestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> update(@PathVariable Long id,
                                                    @Valid @RequestBody PersonRequestDTO requestDTO) {
        return personService.updateFromDTO(id, requestDTO)
                .map(PersonMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!personService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<PersonResponseDTO> deactivate(@PathVariable Long id) {
        return personService.deactivate(id)
                .map(PersonMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}


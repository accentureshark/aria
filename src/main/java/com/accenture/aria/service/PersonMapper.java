package com.accenture.aria.service;

import com.accenture.aria.dto.PersonRequestDTO;
import com.accenture.aria.dto.PersonResponseDTO;
import com.accenture.aria.model.Person;

public class PersonMapper {

    private PersonMapper() {
    }

    public static Person toEntity(PersonRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Person.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .department(dto.getDepartment())
                .active(dto.getActive())
                .build();
    }

    public static PersonResponseDTO toResponse(Person person) {
        if (person == null) {
            return null;
        }

        return PersonResponseDTO.builder()
                .id(person.getId())
                .username(person.getUsername())
                .email(person.getEmail())
                .fullName(person.getFullName())
                .department(person.getDepartment())
                .active(person.getActive())
                .build();
    }
}


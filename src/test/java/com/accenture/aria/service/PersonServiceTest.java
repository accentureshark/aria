package com.accenture.aria.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.accenture.aria.model.Person;
import com.accenture.aria.model.Ticket;
import com.accenture.aria.repository.PersonRepository;
import com.accenture.aria.repository.TicketRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    void findAssignedTickets_withValidPerson_returnsTickets() {
        Long personId = 1L;
        Person person = buildPerson(personId, "Engineering", true);
        Ticket ticket1 = buildTicket(1L);
        Ticket ticket2 = buildTicket(2L);
        when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        when(ticketRepository.findByAssigneeId(personId)).thenReturn(List.of(ticket1, ticket2));

        Optional<List<Ticket>> result = personService.findAssignedTickets(personId);

        assertThat(result).isPresent();
        assertThat(result.get()).containsExactly(ticket1, ticket2);
        verify(personRepository).findById(personId);
        verify(ticketRepository).findByAssigneeId(personId);
    }

    @Test
    void findAssignedTickets_withPersonWithoutTickets_returnsEmptyList() {
        Long personId = 1L;
        Person person = buildPerson(personId, "Engineering", true);
        when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        when(ticketRepository.findByAssigneeId(personId)).thenReturn(List.of());

        Optional<List<Ticket>> result = personService.findAssignedTickets(personId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEmpty();
    }

    @Test
    void findAssignedTickets_withNonExistentPerson_returnsEmpty() {
        Long personId = 99L;
        when(personRepository.findById(personId)).thenReturn(Optional.empty());

        Optional<List<Ticket>> result = personService.findAssignedTickets(personId);

        assertThat(result).isEmpty();
        verify(ticketRepository, never()).findByAssigneeId(personId);
    }

    private Person buildPerson(Long id, String department, Boolean active) {
        return Person.builder()
                .id(id)
                .username("user" + id)
                .email("user" + id + "@example.com")
                .fullName("User " + id)
                .department(department)
                .active(active)
                .build();
    }

    private Ticket buildTicket(Long id) {
        return Ticket.builder()
                .id(id)
                .title("Ticket " + id)
                .description("Description for ticket " + id)
                .build();
    }
}

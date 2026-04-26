# ARIA — AshCraft Labs Agent Context

## Project
ARIA — Automated Request & Incident Assistant
Spring Boot 3.2.8 / Java 17 / Maven / H2 in-memory

## Repo
https://github.com/accentureshark/aria
Local: /home/faguero/dev/accenture/aria
Base branch: main

## Architecture
Layered hexagonal — strict separation:
- controller: REST endpoints, DTOs in/out, no business logic
- service: business logic only, no JPA/HTTP dependencies
- repository: Spring Data JPA interfaces only
- model: JPA entities
- dto: request/response DTOs + static mappers

## Entities
- Ticket (title, description, priority, reporter, assignee, backlog)
- Person (name, email, username, department, active)
- Sprint (name, status: PLANNED/ACTIVE/COMPLETED/CANCELLED)
- Backlog (container for unassigned tickets)

## Rules for agents
- Never put business logic in controllers or repositories
- Always use DTOs for request/response — never expose entities directly
- Static mappers in DTO classes (no MapStruct)
- Jakarta Validation on all request DTOs
- Tests: JUnit 5 + MockMvc for controllers, plain unit tests for services
- Run: mvn clean test && mvn spring-boot:run
- H2 console: http://localhost:8080/h2-console (jdbc:h2:mem:ticketdb / sa / empty)

## Endpoints base
- /api/tickets
- /api/persons
- /api/sprints
- /api/backlogs

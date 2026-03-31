# Aria - Sistema de Gestion de Tickets

Sistema de gestion de tickets inspirado en Jira, construido con Spring Boot 3.2.8 y Java 17.

## Caracteristicas

- CRUD completo de `Ticket`, `Person`, `Sprint` y `Backlog`
- Arquitectura hexagonal por capas (`controller`, `service`, `repository`)
- DTOs para requests/responses y mappers estaticos
- Validaciones con Jakarta Validation en request DTOs
- Base de datos H2 en memoria
- Backlog como contenedor de tickets no asignados a un sprint

## Tecnologias

- Java 17
- Spring Boot 3.2.8
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- Maven

## Ejecutar proyecto

```bash
mvn clean test
mvn spring-boot:run
```

## Endpoints

### Tickets (`/api/tickets`)

- `GET /api/tickets`
- `GET /api/tickets/{id}`
- `POST /api/tickets`
- `PUT /api/tickets/{id}`
- `DELETE /api/tickets/{id}`

### Persons (`/api/persons`)

- `GET /api/persons`
- `GET /api/persons/{id}`
- `GET /api/persons/email/{email}`
- `GET /api/persons/username/{username}`
- `GET /api/persons/department/{department}`
- `POST /api/persons`
- `PUT /api/persons/{id}`
- `DELETE /api/persons/{id}`
- `PATCH /api/persons/{id}/deactivate`

### Sprints (`/api/sprints`)

- `GET /api/sprints` (soporta filtro `?status=PLANNED|ACTIVE|COMPLETED|CANCELLED`)
- `GET /api/sprints/{id}`
- `POST /api/sprints`
- `PUT /api/sprints/{id}`
- `DELETE /api/sprints/{id}`

### Backlogs (`/api/backlogs`)

- `GET /api/backlogs`
- `GET /api/backlogs/{id}`
- `GET /api/backlogs/{id}/tickets`
- `POST /api/backlogs`
- `PUT /api/backlogs/{id}`
- `DELETE /api/backlogs/{id}`

## Relacion entre entidades

`TicketRequestDTO` utiliza IDs para relaciones:

```json
{
  "title": "Bug en login",
  "description": "No permite iniciar sesion",
  "priority": "HIGH",
  "reporterId": 1,
  "assigneeId": 2,
  "backlogId": 1
}
```

En `TicketResponseDTO`, `reporter` y `assignee` se devuelven como objetos anidados completos. El campo `backlogId` se expone como ID.

Un `Backlog` agrupa tickets que todavia no han sido seleccionados para un sprint. Para asociar un ticket a un backlog se usa el campo `backlogId` en el request. El endpoint `GET /api/backlogs/{id}/tickets` devuelve todos los tickets vinculados a ese backlog.

## H2 Console

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:ticketdb`
- Usuario: `sa`
- Password: vacia

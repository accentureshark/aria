# Aria - Sistema de Gestion de Tickets

Sistema de gestion de tickets inspirado en Jira, construido con Spring Boot 3.2.8 y Java 17.

## Caracteristicas

- CRUD completo de `Ticket` y `Person`
- Arquitectura hexagonal por capas (`controller`, `service`, `repository`)
- DTOs para requests/responses y mappers estaticos
- Validaciones con Jakarta Validation en request DTOs
- Base de datos H2 en memoria

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

## Relacion entre entidades

`TicketRequestDTO` utiliza IDs para relaciones:

```json
{
  "title": "Bug en login",
  "description": "No permite iniciar sesion",
  "priority": "HIGH",
  "reporterId": 1,
  "assigneeId": 2
}
```

En `TicketResponseDTO`, `reporter` y `assignee` se devuelven como objetos anidados completos.

## H2 Console

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:ticketdb`
- Usuario: `sa`
- Password: vacia

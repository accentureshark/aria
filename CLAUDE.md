# CLAUDE.md — Aria

## Descripción del Proyecto

**Aria** es un sistema de gestión de tickets estilo Jira, desarrollado por el equipo de Accenture. Expone una REST API para gestionar tickets, personas (usuarios) y sprints.

- **Versión:** 0.0.1-SNAPSHOT
- **Puerto por defecto:** 8080
- **Base de datos:** H2 in-memory (`jdbc:h2:mem:ticketdb`)
- **H2 Console:** `http://localhost:8080/h2-console` (user: `sa`, password: vacío)

---

## Stack Tecnológico

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java 17 |
| Framework | Spring Boot 3.2.8 |
| Web | Spring Web (REST) |
| ORM | Spring Data JPA + Hibernate |
| Validación | Jakarta Validation |
| Base de datos | H2 in-memory |
| Reducción de boilerplate | Lombok |
| Build | Maven 3.x (con Maven Wrapper) |
| Tests unitarios | JUnit 5 + Mockito |
| Tests de integración | Postman / Newman 6.1.3 |
| Reportes de tests | newman-reporter-htmlextra |

---

## Estructura del Proyecto

```
aria/
├── src/main/java/com/accenture/aria/
│   ├── AriaApplication.java          # Entry point
│   ├── controller/
│   │   ├── TicketController.java
│   │   ├── PersonController.java
│   │   ├── SprintController.java
│   │   └── GlobalExceptionHandler.java
│   ├── model/
│   │   ├── Ticket.java
│   │   ├── Person.java
│   │   ├── Sprint.java
│   │   ├── Status.java               # Enum: OPEN, IN_PROGRESS, RESOLVED, CLOSED
│   │   ├── Priority.java             # Enum: LOW, MEDIUM, HIGH, URGENT
│   │   └── SprintStatus.java         # Enum: PLANNED, ACTIVE, COMPLETED, CANCELLED
│   ├── dto/
│   │   ├── TicketRequestDTO.java / TicketResponseDTO.java
│   │   ├── PersonRequestDTO.java / PersonResponseDTO.java
│   │   ├── SprintRequestDTO.java / SprintResponseDTO.java
│   │   ├── ApiErrorResponseDTO.java
│   │   └── FieldValidationErrorDTO.java
│   ├── repository/
│   │   ├── TicketRepository.java
│   │   ├── PersonRepository.java
│   │   └── SprintRepository.java
│   └── service/
│       ├── TicketService.java / TicketMapper.java
│       ├── PersonService.java / PersonMapper.java
│       ├── SprintService.java / SprintMapper.java
│       └── exception/ResourceNotFoundException.java
├── src/main/resources/
│   └── application.properties
├── src/test/java/com/accenture/aria/
│   ├── AriaApplicationTests.java
│   └── service/SprintServiceTest.java
├── postman/
│   ├── Aria-Persons-CRUD-Casuistica.postman_collection.json
│   ├── Aria-Tickets-CRUD-Casuistica.postman_collection.json
│   ├── Aria-Sprints-CRUD-Casuistica.postman_collection.json
│   ├── Aria-local.postman_environment.json
│   └── package.json
└── docs/
    ├── README_Excusas_Sharks.md
    └── json/                         # Datos de ejemplo
```

---

## Arquitectura

El proyecto sigue una **arquitectura en capas** (Controller → Service → Repository → Model):

1. **Controller** — Recibe peticiones HTTP, delega al servicio, devuelve respuestas estructuradas.
2. **Service** — Lógica de negocio, mapeo de DTOs a entidades y viceversa.
3. **Repository** — Acceso a datos mediante `JpaRepository` de Spring Data.
4. **Model** — Entidades JPA con anotaciones Lombok.
5. **DTO** — Objetos de transferencia (Request/Response) con validaciones Jakarta.

**Patrones clave:**
- Mappers estáticos (`TicketMapper`, `PersonMapper`, `SprintMapper`) para conversión DTO ↔ entidad.
- `applyPartialUpdate()` en servicios para actualizaciones parciales null-safe.
- `GlobalExceptionHandler` con `@RestControllerAdvice` para respuestas de error consistentes.

---

## Endpoints REST

### Tickets — `/api/tickets`
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/tickets` | Listar todos |
| GET | `/api/tickets/{id}` | Obtener por ID |
| POST | `/api/tickets` | Crear ticket |
| PUT | `/api/tickets/{id}` | Actualizar ticket |
| DELETE | `/api/tickets/{id}` | Eliminar ticket |

### Persons — `/api/persons`
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/persons` | Listar todos |
| GET | `/api/persons/{id}` | Obtener por ID |
| GET | `/api/persons/email/{email}` | Buscar por email |
| GET | `/api/persons/username/{username}` | Buscar por username |
| GET | `/api/persons/department/{department}` | Buscar por departamento |
| POST | `/api/persons` | Crear persona |
| PUT | `/api/persons/{id}` | Actualizar persona |
| DELETE | `/api/persons/{id}` | Eliminar persona |
| PATCH | `/api/persons/{id}/deactivate` | Desactivar persona |

### Sprints — `/api/sprints`
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/sprints` | Listar (con filtro opcional `?status=`) |
| GET | `/api/sprints/{id}` | Obtener por ID |
| POST | `/api/sprints` | Crear sprint |
| PUT | `/api/sprints/{id}` | Actualizar sprint |
| DELETE | `/api/sprints/{id}` | Eliminar sprint |

---

## Modelos de Datos

### Person
`id`, `username` (único), `email` (único), `fullName`, `department`, `active`

### Ticket
`id`, `title`, `description`, `status` (default: OPEN), `priority` (default: MEDIUM), `createdAt`, `updatedAt`
Relaciones: `reporter` → Person (ManyToOne, lazy), `assignee` → Person (ManyToOne, lazy)

### Sprint
`id`, `name`, `goal`, `startDate`, `endDate`, `status` (default: PLANNED), `createdAt`, `updatedAt`

---

## Comandos Principales

```bash
# Ejecutar la aplicación
./mvnw spring-boot:run

# Ejecutar tests unitarios
./mvnw clean test

# Compilar JAR
./mvnw clean package

# Tests de integración con Newman
cd postman
npm install
npm run test:persons   # Tests de Persons
npm run test:tickets   # Tests de Tickets
npm run test:sprints   # Tests de Sprints (genera reporte HTML)
npm run test:all       # Todos los tests
```

---

## Convenciones del Proyecto

- **Commits:** Conventional Commits (`feat:`, `fix:`, `test:`, `build:`, `refactor:`, etc.)
- **Validaciones:** Se usan anotaciones Jakarta en los DTOs de request (`@NotBlank`, `@Email`, `@Size`)
- **Errores HTTP esperados:** 400 (validación), 404 (recurso no encontrado), 409 (conflicto de unicidad)
- **Lazy loading:** Las relaciones `reporter` y `assignee` en Ticket son lazy — evitar serialización fuera de transacción.
- **H2 es in-memory:** Los datos no persisten entre reinicios de la aplicación.

---

## Tests

### Unitarios (JUnit 5 + Mockito)
- `SprintServiceTest.java` — cobertura completa del servicio: findAll, findById, findByStatus, create, update, delete (happy path + excepciones).
- Usar `@Mock` e `@InjectMocks`; no usar mocks para la base de datos.

### Integración (Newman/Postman)
- Las colecciones cubren casos happy path y escenarios de error para los tres recursos.
- El entorno local está en `postman/Aria-local.postman_environment.json` (baseUrl: `http://localhost:8080`).

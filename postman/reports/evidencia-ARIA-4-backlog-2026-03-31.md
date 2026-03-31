# Evidencia de Pruebas - Entidad Backlog

**Ticket Jira:** [ARIA-4] - Entidad Backlog que van a contener ticket que todavia no estan seleccionados en el Sprint
**Fecha de ejecución:** 2026-03-31
**Entorno:** Local (http://localhost:8080)
**Colección:** Aria-Backlogs-CRUD-Casuistica.postman_collection.json

---

## Resumen Ejecutivo

| Métrica | Valor |
|---------|-------|
| Total requests | 12 |
| Assertions totales | 26 |
| Assertions pasadas | 26 |
| Assertions fallidas | 0 |
| Tasa de éxito | 100% |
| Tiempo total | ~1955ms |

**Estado general:** APROBADO

---

## Casos de Prueba Ejecutados

| # | Caso de Prueba | Método | Endpoint | Status Esperado | Status Obtenido | Resultado |
|---|----------------|--------|----------|-----------------|-----------------|-----------|
| 1 | Crear backlog | POST | /api/backlogs | 201 | 201 | PASS |
| 2 | Listar todos | GET | /api/backlogs | 200 | 200 | PASS |
| 3 | Obtener por ID | GET | /api/backlogs/:id | 200 | 200 | PASS |
| 4 | Listar tickets del backlog | GET | /api/backlogs/:id/tickets | 200 | 200 | PASS |
| 5 | Actualizar backlog | PUT | /api/backlogs/:id | 200 | 200 | PASS |
| 6 | Eliminar backlog | DELETE | /api/backlogs/:id | 204 | 204 | PASS |
| 7 | Backlog eliminado retorna 404 (GET) | GET | /api/backlogs/:id | 404 | 404 | PASS |
| 8 | Backlog eliminado retorna 404 (tickets) | GET | /api/backlogs/:id/tickets | 404 | 404 | PASS |
| 9 | ID inexistente retorna 404 (GET) | GET | /api/backlogs/99999 | 404 | 404 | PASS |
| 10 | ID inexistente retorna 404 (PUT) | PUT | /api/backlogs/99999 | 404 | 404 | PASS |
| 11 | ID inexistente retorna 404 (DELETE) | DELETE | /api/backlogs/99999 | 404 | 404 | PASS |
| 12 | Body sin name retorna 400 | POST | /api/backlogs | 400 | 400 | PASS |

---

## Endpoints Validados

| Método | Endpoint | Descripción | Estado |
|--------|----------|-------------|--------|
| GET | /api/backlogs | Listar todos los backlogs | PASS |
| GET | /api/backlogs/{id} | Obtener backlog por ID | PASS |
| GET | /api/backlogs/{id}/tickets | Listar tickets del backlog | PASS |
| POST | /api/backlogs | Crear backlog | PASS |
| PUT | /api/backlogs/{id} | Actualizar backlog | PASS |
| DELETE | /api/backlogs/{id} | Eliminar backlog | PASS |

---

## Cobertura de Unit Tests

| Métrica | Valor |
|---------|-------|
| Tests ejecutados | 10 |
| Tests pasados | 10 |
| Failures | 0 |
| Errors | 0 |
| Skipped | 0 |
| Tasa de éxito | 100% |

### Detalle por método de test

| # | Test Method | Resultado | Detalle |
|---|-------------|-----------|---------|
| 1 | `findAll_returnsAllBacklogs` | PASS | — |
| 2 | `findById_existingBacklog_returnsBacklog` | PASS | — |
| 3 | `findById_nonExistingBacklog_returnsEmpty` | PASS | — |
| 4 | `findTicketsByBacklogId_returnsTicketsForBacklog` | PASS | — |
| 5 | `create_setsTimestamps` | PASS | — |
| 6 | `createFromDTO_persistsBacklog` | PASS | — |
| 7 | `update_existingBacklog_appliesChanges` | PASS | — |
| 8 | `update_nonExistingBacklog_returnsEmpty` | PASS | — |
| 9 | `delete_existingBacklog_returnsTrue` | PASS | — |
| 10 | `delete_nonExistingBacklog_returnsFalse` | PASS | — |

> Tests ejecutados con: `mvn test -Dtest=BacklogServiceTest`
> Reporte Surefire: `target/surefire-reports/TEST-com.accenture.aria.service.BacklogServiceTest.xml`

---

## Artefactos

- **Colección Newman:** `postman/Aria-Backlogs-CRUD-Casuistica.postman_collection.json`
- **Reporte HTML:** `postman/reports/backlog-ARIA-4-report.html`
- **Reporte Surefire XML:** `target/surefire-reports/TEST-com.accenture.aria.service.BacklogServiceTest.xml`
- **Entorno:** `postman/Aria-local.postman_environment.json`

---

*Evidencia generada automáticamente con Claude Code — 2026-03-31*

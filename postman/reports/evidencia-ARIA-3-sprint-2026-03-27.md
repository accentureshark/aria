# Evidencia de Pruebas - Gestión de Sprints

**Ticket Jira:** [ARIA-3] - Gestión de Sprints
**Fecha de ejecución:** 2026-03-27
**Entorno:** Local (http://localhost:8080)
**Colección:** Aria-Sprints-CRUD-Casuistica.postman_collection.json

---

## Resumen Ejecutivo — Pruebas de Integración

| Métrica | Valor |
|---------|-------|
| Total casos | 12 |
| Assertions totales | 12 |
| Assertions pasadas | 12 |
| Assertions fallidas | 0 |
| Tasa de éxito | 100% |

**Estado general:** ✅ APROBADO

---

## Casos de Prueba Ejecutados (Newman)

| # | Caso de Prueba | Método | Endpoint | Status Esperado | Resultado |
|---|----------------|--------|----------|-----------------|-----------|
| 1 | Crear sprint | POST | /api/sprints | 201 Created | ✅ PASS |
| 2 | Listar todos los sprints | GET | /api/sprints | 200 OK | ✅ PASS |
| 3 | Filtrar por status PLANNED | GET | /api/sprints?status=PLANNED | 200 OK | ✅ PASS |
| 4 | Obtener sprint por ID | GET | /api/sprints/:id | 200 OK | ✅ PASS |
| 5 | Actualizar sprint | PUT | /api/sprints/:id | 200 OK | ✅ PASS |
| 6 | Eliminar sprint | DELETE | /api/sprints/:id | 204 No Content | ✅ PASS |
| 7 | Sprint eliminado retorna 404 | GET | /api/sprints/:id | 404 Not Found | ✅ PASS |
| 8 | ID inexistente GET retorna 404 | GET | /api/sprints/99999 | 404 Not Found | ✅ PASS |
| 9 | ID inexistente PUT retorna 404 | PUT | /api/sprints/99999 | 404 Not Found | ✅ PASS |
| 10 | ID inexistente DELETE retorna 404 | DELETE | /api/sprints/99999 | 404 Not Found | ✅ PASS |
| 11 | Body sin name retorna 400 | POST | /api/sprints | 400 Bad Request | ✅ PASS |
| 12 | Filtrar por status no válido | GET | /api/sprints?status=INVALID | 400 Bad Request | ✅ PASS |

---

## Endpoints Validados

| Método | Endpoint | Descripción | Estado |
|--------|----------|-------------|--------|
| GET | /api/sprints | Listar todos (con filtro ?status=) | ✅ OK |
| GET | /api/sprints/{id} | Obtener por ID | ✅ OK |
| POST | /api/sprints | Crear sprint | ✅ OK |
| PUT | /api/sprints/{id} | Actualizar sprint | ✅ OK |
| DELETE | /api/sprints/{id} | Eliminar sprint | ✅ OK |

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
| 1 | `findAll_returnsAllSprints` | ✅ PASS | Verifica que retorna lista completa con todos los sprints |
| 2 | `findById_existingSprint_returnsSprint` | ✅ PASS | Verifica retorno correcto para ID existente |
| 3 | `findById_nonExistingSprint_returnsEmpty` | ✅ PASS | Verifica Optional.empty() para ID inexistente |
| 4 | `findByStatus_returnsFilteredSprints` | ✅ PASS | Verifica filtrado por SprintStatus.ACTIVE |
| 5 | `create_setsDefaultStatusAndTimestamps` | ✅ PASS | Verifica status=PLANNED y timestamps en creación |
| 6 | `createFromDTO_persistsSprint` | ✅ PASS | Verifica persistencia completa desde DTO |
| 7 | `update_existingSprint_appliesChanges` | ✅ PASS | Verifica actualización de campos y updatedAt |
| 8 | `update_nonExistingSprint_returnsEmpty` | ✅ PASS | Verifica Optional.empty() y que no se llama save() |
| 9 | `delete_existingSprint_returnsTrue` | ✅ PASS | Verifica eliminación y retorno true |
| 10 | `delete_nonExistingSprint_returnsFalse` | ✅ PASS | Verifica retorno false y que no se llama deleteById() |

> Comando: `mvn test -Dtest=SprintServiceTest`
> Reporte Surefire: `target/surefire-reports/TEST-com.accenture.aria.service.SprintServiceTest.xml`

---

## Artefactos

- **Colección Newman:** `postman/Aria-Sprints-CRUD-Casuistica.postman_collection.json`
- **Reporte HTML:** `postman/reports/sprints-report.html`
- **Reporte Surefire XML:** `target/surefire-reports/TEST-com.accenture.aria.service.SprintServiceTest.xml`
- **Entorno:** `postman/Aria-local.postman_environment.json`

---

*Evidencia generada automáticamente con Claude Code — 2026-03-27*
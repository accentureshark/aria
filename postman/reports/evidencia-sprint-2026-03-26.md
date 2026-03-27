ACTU# Evidencia de Pruebas - Entidad Sprint para manejar los ciclos en Scrum

**Ticket Jira:** [ARIA-3] - Entidad Sprint para manejar los ciclos en Scrum
**Fecha de ejecución:** 2026-03-26
**Entorno:** Local (http://localhost:8080)
**Colección:** Aria - Sprints CRUD Casuistica

---

## Resumen Ejecutivo

| Métrica | Valor |
|---------|-------|
| Total casos | 11 |
| Assertions totales | 25 |
| Assertions pasadas | 25 |
| Assertions fallidas | 0 |
| Tasa de éxito | 100% |
| Tiempo total | 1497ms |
| Tiempo promedio de respuesta | 52ms |

**Estado general:** ✅ APROBADO

---

## Casos de Prueba Ejecutados

| # | Caso de Prueba | Método | Endpoint | Status Esperado | Status Obtenido | Resultado |
|---|----------------|--------|----------|-----------------|-----------------|-----------|
| 1 | Crear sprint | POST | /api/sprints | 201 | 201 | ✅ PASS |
| 2 | Listar todos | GET | /api/sprints | 200 | 200 | ✅ PASS |
| 3 | Filtrar por status | GET | /api/sprints?status=PLANNED | 200 | 200 | ✅ PASS |
| 4 | Obtener por ID | GET | /api/sprints/:id | 200 | 200 | ✅ PASS |
| 5 | Actualizar sprint | PUT | /api/sprints/:id | 200 | 200 | ✅ PASS |
| 6 | Eliminar sprint | DELETE | /api/sprints/:id | 204 | 204 | ✅ PASS |
| 7 | GET id eliminado | GET | /api/sprints/:id | 404 | 404 | ✅ PASS |
| 8 | GET id inexistente | GET | /api/sprints/99999 | 404 | 404 | ✅ PASS |
| 9 | PUT id inexistente | PUT | /api/sprints/99999 | 404 | 404 | ✅ PASS |
| 10 | DELETE id inexistente | DELETE | /api/sprints/99999 | 404 | 404 | ✅ PASS |
| 11 | POST sin name | POST | /api/sprints | 400 | 400 | ✅ PASS |

---

## Detalle por Caso

### ✅ Caso 1 - POST /api/sprints → 201 Created
- **Request:**
  ```json
  {
    "name": "Sprint Test 1743014...",
    "goal": "Implementar modulo de autenticacion",
    "startDate": "2025-02-01",
    "endDate": "2025-02-14"
  }
  ```
- **Assertions validadas:**
  - [x] Status code es 201
  - [x] Response contiene campo `id` (number)
  - [x] `name` coincide con el enviado
  - [x] `status` default es `PLANNED`
  - [x] `createdAt` está presente

### ✅ Caso 2 - GET /api/sprints → 200 OK
- **Assertions validadas:**
  - [x] Status code es 200
  - [x] Response es array
  - [x] Contiene al menos un sprint

### ✅ Caso 3 - GET /api/sprints?status=PLANNED → 200 OK
- **Assertions validadas:**
  - [x] Status code es 200
  - [x] Response es array
  - [x] Todos los elementos tienen `status = PLANNED`

### ✅ Caso 4 - GET /api/sprints/:id → 200 OK
- **Assertions validadas:**
  - [x] Status code es 200
  - [x] `id` coincide con el creado
  - [x] `name` está presente
  - [x] `status` está presente

### ✅ Caso 5 - PUT /api/sprints/:id → 200 OK
- **Request:**
  ```json
  {
    "name": "Sprint Test ...",
    "goal": "Goal actualizado",
    "startDate": "2025-02-01",
    "endDate": "2025-02-14",
    "status": "ACTIVE"
  }
  ```
- **Assertions validadas:**
  - [x] Status code es 200
  - [x] `status` actualizado a `ACTIVE`
  - [x] `goal` actualizado a "Goal actualizado"
  - [x] `updatedAt` está presente

### ✅ Caso 6 - DELETE /api/sprints/:id → 204 No Content
- **Assertions validadas:**
  - [x] Status code es 204

### ✅ Caso 7 - GET id eliminado → 404 Not Found
- **Assertions validadas:**
  - [x] Status code es 404

### ✅ Casos 8, 9, 10 - ID inexistente (99999) → 404 Not Found
- **Assertions validadas:**
  - [x] GET /api/sprints/99999 → 404
  - [x] PUT /api/sprints/99999 → 404
  - [x] DELETE /api/sprints/99999 → 404

### ✅ Caso 11 - POST sin campo name → 400 Bad Request
- **Request:** `{ "goal": "Sin nombre no deberia crear" }`
- **Assertions validadas:**
  - [x] Status code es 400 (validación @NotBlank activa)

---

## Endpoints Validados

| Método | Endpoint | Descripción | Estado |
|--------|----------|-------------|--------|
| GET | /api/sprints | Listar todos | ✅ |
| GET | /api/sprints?status={status} | Filtrar por estado | ✅ |
| GET | /api/sprints/{id} | Obtener por ID | ✅ |
| POST | /api/sprints | Crear sprint | ✅ |
| PUT | /api/sprints/{id} | Actualizar sprint | ✅ |
| DELETE | /api/sprints/{id} | Eliminar sprint | ✅ |

---

## Artefactos

- **Colección Newman:** `postman/Aria-Sprints-CRUD-Casuistica.postman_collection.json`
- **Reporte HTML:** `postman/reports/sprints-report.html`
- **Entorno:** `postman/Aria-local.postman_environment.json`

---

*Evidencia generada automáticamente con Claude Code — 2026-03-26 18:38*
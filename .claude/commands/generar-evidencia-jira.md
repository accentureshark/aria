# Skill: generar-evidencia-jira

Dado el ID de un ticket Jira de tipo Test (`$ARGUMENTS`), genera un documento Markdown con la evidencia de las pruebas ejecutadas y lo sube como comentario al ticket.

## Pasos a seguir

### 1. Validar el argumento
Si `$ARGUMENTS` está vacío, pregunta al usuario:
- El ID del ticket Jira de tipo Test (ej: `ARIA-456`)
- El nombre de la entidad/feature que se probó (si no se puede deducir del ticket)

### 2. Obtener contexto del ticket Jira
Usa `mcp__claude_ai_Atlassian__getJiraIssue` con el ID del ticket para obtener:
- Título del ticket de test
- Descripción / criterios de aceptación
- Ticket de feature relacionado (si está vinculado)

### 3. Recopilar evidencia de pruebas

**a) Leer el reporte Newman más reciente:**
Busca en `postman/reports/` el archivo HTML o JSON más reciente con `Glob("postman/reports/**")`.

Si no hay reportes, ejecuta las pruebas primero:
```bash
cd postman && npm run test:all -- --reporters json --reporter-json-export reports/last-run.json
```

**b) Leer los resultados:**
Si existe `postman/reports/last-run.json` o similar, léelo para extraer:
- Total de casos ejecutados
- Casos pasados / fallidos
- Tiempo de ejecución
- Detalle por request: nombre, método, URL, status code obtenido, assertions pasadas/fallidas

**c) Leer la colección Newman relevante:**
Busca `postman/{Entity}-CRUD-Casuistica.postman_collection.json` para listar todos los casos de prueba y su propósito.

### 4. Generar el documento Markdown de evidencia

Crea el archivo `postman/reports/evidencia-{entity}-{fecha}.md` con esta estructura:

```markdown
# Evidencia de Pruebas - {Nombre del Ticket}

**Ticket Jira:** [{TICKET-ID}] - {Título del ticket}
**Feature relacionada:** [{FEATURE-ID}] - {Título de la feature} (si aplica)
**Fecha de ejecución:** {fecha actual}
**Entorno:** Local (http://localhost:8080)
**Ejecutado por:** {usuario si se puede obtener de Jira o dejar como "QA Automation"}

---

## Resumen Ejecutivo

| Métrica | Valor |
|---------|-------|
| Total casos | N |
| Casos pasados | N |
| Casos fallidos | N |
| Tasa de éxito | XX% |
| Tiempo total | X.XXs |

**Estado general:** ✅ APROBADO / ❌ FALLIDO

---

## Casos de Prueba Ejecutados

| # | Caso de Prueba | Método | Endpoint | Status Esperado | Status Obtenido | Resultado |
|---|----------------|--------|----------|-----------------|-----------------|-----------|
| 1 | Nombre del caso | POST | /api/xxx | 201 | 201 | ✅ PASS |
| 2 | ... | ... | ... | ... | ... | ... |

---

## Detalle por Caso

### ✅ [Nombre del caso] - PASS
- **Request:** `POST /api/{entidades}`
- **Body enviado:**
  ```json
  { ... }
  ```
- **Response obtenido (status: 201):**
  ```json
  { ... }
  ```
- **Assertions validadas:**
  - [x] Status code es 201
  - [x] Response contiene campo `id`

### ❌ [Nombre del caso fallido] - FAIL (si aplica)
- **Error:** Descripción del error
- **Causa probable:** ...

---

## Endpoints Validados

| Método | Endpoint | Descripción | Estado |
|--------|----------|-------------|--------|
| GET | /api/{entidades} | Listar todos | ✅ |
| GET | /api/{entidades}/{id} | Obtener por ID | ✅ |
| POST | /api/{entidades} | Crear | ✅ |
| PUT | /api/{entidades}/{id} | Actualizar | ✅ |
| DELETE | /api/{entidades}/{id} | Eliminar | ✅ |

---

## Criterios de Aceptación

Mapear cada criterio del ticket Jira con el resultado:

| Criterio | Caso(s) que lo validan | Estado |
|----------|------------------------|--------|
| {Criterio 1 del ticket} | Caso X, Caso Y | ✅ Cumplido |

---

## Artefactos

- Colección Newman: `postman/{Entity}-CRUD-Casuistica.postman_collection.json`
- Reporte HTML: `postman/reports/{entity}-report.html`
- Entorno: `postman/Aria-local.postman_environment.json`

---

*Evidencia generada automáticamente con Claude Code — {fecha y hora}*
```

Usa los datos reales del reporte Newman para completar todos los campos.

### 5. Subir la evidencia al ticket Jira

**a) Formatear el comentario para Jira:**
Jira acepta Atlassian Document Format (ADF) en los comentarios via API. Convierte el markdown a un comentario bien estructurado.

Usa `mcp__claude_ai_Atlassian__addCommentToJiraIssue` con el ID del ticket y el contenido del documento de evidencia.

El comentario debe incluir:
- El resumen ejecutivo (tabla de métricas)
- La tabla de casos de prueba con resultados
- La sección de criterios de aceptación
- Una nota indicando que el reporte HTML completo está disponible localmente

**b) Transicionar el ticket si todas las pruebas pasaron:**
Si el resultado es 100% PASS:
- Usa `mcp__claude_ai_Atlassian__getTransitionsForJiraIssue` para ver las transiciones disponibles
- Si existe una transición tipo "Done", "Pass", o "Closed", usa `mcp__claude_ai_Atlassian__transitionJiraIssue` para moverla

Si hay fallos, NO transicionar y notificar al usuario.

### 6. Reporte final

Muestra:
- Ruta del archivo MD generado
- Resumen de resultados (passed/failed)
- Confirmación de que el comentario fue subido al ticket Jira con link directo
- Si el ticket fue transicionado, indicar el nuevo estado
- Si hay fallos, listar los casos fallidos con sugerencias de corrección
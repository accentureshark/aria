# Skill: generar-evidencia-jira

Dado el ID de un ticket Jira de tipo Feature o Tarea (`$ARGUMENTS`), genera un documento Markdown con la evidencia de las pruebas ejecutadas, crea un subtask de tipo Test en Jira con ese contenido y lo vincula al ticket padre.

Uso: `/generar-evidencia-jira ARIA-3` o `/generar-evidencia-jira ARIA-3 --dry-run`

## Pasos a seguir

### 0. Detectar modo DRYRUN

Revisa si `$ARGUMENTS` contiene `--dry-run` o `--dryrun` (case-insensitive).

- Si está presente: activar **MODO DRYRUN**. Extrae el ticket ID del resto del argumento.
- Si no está presente: ejecutar normalmente.

En **MODO DRYRUN**:
- Ejecutar los pasos 1–5 normalmente (leer Jira, recopilar evidencia, generar MD)
- **NO crear el subtask en Jira** (saltar paso 6)
- **NO vincular ni transicionar** (saltar pasos 7 y 8)
- En su lugar: mostrar el contenido ADF que se enviaría a Jira como preview en bloque de código
- Terminar con el bloque de resumen DRYRUN

### 1. Validar el argumento
Si `$ARGUMENTS` está vacío (o solo contiene `--dry-run`), pregunta al usuario el ID del ticket Jira padre (ej: `ARIA-3`).

### 2. Obtener contexto del ticket Jira padre
Usa `mcp__claude_ai_Atlassian__getJiraIssue` con el ID del ticket para obtener:
- Título de la feature/tarea
- Descripción / criterios de aceptación
- `project.key` (necesario para crear el subtask)
- `issuetype` del ticket padre

### 3. Obtener tipos de issue disponibles en el proyecto
Usa `mcp__claude_ai_Atlassian__getJiraProjectIssueTypesMetadata` con el `project.key` para listar los tipos disponibles.

Buscar en este orden de preferencia:
1. `Test` (nombre exacto)
2. `Subtask` o `Sub-task`
3. `Tarea` (si no existe ninguno de los anteriores)

Guardar el `id` del tipo elegido.

### 4. Recopilar evidencia de pruebas

**a) Buscar el reporte Newman:**
Usa Glob con el patrón `postman/reports/evidencia-{TICKET-ID}-*.md` para encontrar el MD ya generado para este ticket.
Si no hay MD, buscar en `postman/reports/*-{TICKET-ID}-report.html` (reporte HTML de Newman).
Si tampoco hay reportes, indicar al usuario que ejecute `/generar-newman {TICKET-ID}` primero.

**b) Leer el MD de evidencia:**
Leer el archivo encontrado para extraer:
- Resumen ejecutivo (métricas: total casos, pasados, fallidos, tasa de éxito)
- Tabla de casos de prueba
- Estado general (APROBADO / FALLIDO)

**c) Ejecutar unit tests y recopilar cobertura:**

Inferir el nombre de la entidad desde el título del ticket Jira (ej: "Gestión de Comentarios" → `Comment`).

Correr los unit tests del servicio generado:
```bash
cd {project-root} && mvn test -Dtest={Entity}ServiceTest -q 2>&1 | tail -20
```

Si el archivo de test no existe, correr todos los tests y filtrar la salida:
```bash
cd {project-root} && mvn test -q 2>&1 | tail -30
```

Del output de Maven Surefire extraer:
- Tests run (total)
- Failures
- Errors
- Skipped

Además, leer el reporte XML generado por Surefire para listar los test methods individuales:
```bash
cat target/surefire-reports/com.accenture.aria.service.{Entity}ServiceTest.xml 2>/dev/null
```

Del XML extraer cada `<testcase name="...">` y si tiene `<failure>` o `<error>` para determinar el resultado por método.

### 5. Generar el MD de evidencia (si no existe)

Si no existe un MD de evidencia previo, generarlo en `postman/reports/evidencia-{TICKET-ID}-{entity}-{fecha}.md` con esta estructura:

```markdown
# Evidencia de Pruebas - {Nombre del Ticket}

**Ticket Jira:** [{TICKET-ID}] - {Título}
**Fecha de ejecución:** {fecha actual}
**Entorno:** Local (http://localhost:8080)
**Colección:** {nombre de la colección Newman}

---

## Resumen Ejecutivo

| Métrica | Valor |
|---------|-------|
| Total casos | N |
| Assertions totales | N |
| Assertions pasadas | N |
| Assertions fallidas | N |
| Tasa de éxito | XX% |
| Tiempo total | Xs |

**Estado general:** ✅ APROBADO / ❌ FALLIDO

---

## Casos de Prueba Ejecutados

| # | Caso de Prueba | Método | Endpoint | Status Esperado | Status Obtenido | Resultado |
|---|----------------|--------|----------|-----------------|-----------------|-----------|
...

---

## Endpoints Validados

| Método | Endpoint | Descripción | Estado |
|--------|----------|-------------|--------|
...

---

## Cobertura de Unit Tests

| Métrica | Valor |
|---------|-------|
| Tests ejecutados | N |
| Tests pasados | N |
| Failures | N |
| Errors | N |
| Skipped | N |
| Tasa de éxito | XX% |

### Detalle por método de test

| # | Test Method | Resultado | Detalle |
|---|-------------|-----------|---------|
| 1 | `deberiaRetornarTodosLos{Entity}s` | ✅ PASS | — |
| 2 | `deberiaRetornar{Entity}PorId` | ✅ PASS | — |
| 3 | `deberiaLanzarExcepcionSi{Entity}NoExiste` | ✅ PASS | — |
| 4 | `deberiaCrear{Entity}Exitosamente` | ✅ PASS | — |
| 5 | `deberiaActualizar{Entity}Exitosamente` | ✅ PASS | — |
| 6 | `deberiaEliminar{Entity}Exitosamente` | ✅ PASS | — |
...

> Tests ejecutados con: `mvn test -Dtest={Entity}ServiceTest`
> Reporte Surefire: `target/surefire-reports/com.accenture.aria.service.{Entity}ServiceTest.xml`

---

## Artefactos

- **Colección Newman:** `postman/{Entity}-CRUD-Casuistica.postman_collection.json`
- **Reporte HTML:** `postman/reports/{entity}-{TICKET-ID}-report.html`
- **Reporte Surefire XML:** `target/surefire-reports/com.accenture.aria.service.{Entity}ServiceTest.xml`
- **Entorno:** `postman/Aria-local.postman_environment.json`

---

*Evidencia generada automáticamente con Claude Code — {fecha y hora}*
```

### 6. Crear el subtask de tipo Test en Jira

Usa `mcp__claude_ai_Atlassian__createJiraIssue` con:
- `projectKey`: el key del proyecto (ej: `ARIA`)
- `issueType`: el id obtenido en el paso 3
- `summary`: `[Test] {Título del ticket padre} - Evidencia CRUD`
- `description`: convertir el contenido del MD a Atlassian Document Format (ADF):
  - El resumen ejecutivo como una tabla ADF
  - La lista de casos como tabla ADF
  - Los endpoints validados como tabla ADF
  - Texto plano para las secciones narrativas
- `parentKey`: el ID del ticket padre (`$ARGUMENTS`) — esto lo vincula como subtask

**Estructura ADF mínima para el description:**
```json
{
  "type": "doc",
  "version": 1,
  "content": [
    {
      "type": "heading",
      "attrs": { "level": 2 },
      "content": [{ "type": "text", "text": "Resumen Ejecutivo — Pruebas de Integración" }]
    },
    {
      "type": "paragraph",
      "content": [
        { "type": "text", "text": "Total casos: N | Pasados: N | Fallidos: N | Tasa de éxito: XX% | Estado: APROBADO", "marks": [{"type": "strong"}] }
      ]
    },
    {
      "type": "heading",
      "attrs": { "level": 2 },
      "content": [{ "type": "text", "text": "Casos de Prueba (Newman)" }]
    },
    {
      "type": "table",
      "attrs": { "isNumberColumnEnabled": false, "layout": "default" },
      "content": [ "... filas con los casos de integración ..." ]
    },
    {
      "type": "heading",
      "attrs": { "level": 2 },
      "content": [{ "type": "text", "text": "Endpoints Validados" }]
    },
    {
      "type": "table",
      "attrs": { "isNumberColumnEnabled": false, "layout": "default" },
      "content": [ "... filas con método, endpoint, estado ..." ]
    },
    {
      "type": "heading",
      "attrs": { "level": 2 },
      "content": [{ "type": "text", "text": "Cobertura de Unit Tests" }]
    },
    {
      "type": "paragraph",
      "content": [
        { "type": "text", "text": "Tests ejecutados: N | Pasados: N | Failures: 0 | Errors: 0 | Skipped: 0 | Tasa: 100%", "marks": [{"type": "strong"}] }
      ]
    },
    {
      "type": "table",
      "attrs": { "isNumberColumnEnabled": false, "layout": "default" },
      "content": [
        {
          "type": "tableRow",
          "content": [
            { "type": "tableHeader", "content": [{ "type": "paragraph", "content": [{ "type": "text", "text": "#" }] }] },
            { "type": "tableHeader", "content": [{ "type": "paragraph", "content": [{ "type": "text", "text": "Test Method" }] }] },
            { "type": "tableHeader", "content": [{ "type": "paragraph", "content": [{ "type": "text", "text": "Resultado" }] }] }
          ]
        },
        "... una tableRow por cada método de test del Surefire XML ..."
      ]
    },
    {
      "type": "paragraph",
      "content": [{ "type": "text", "text": "Evidencia generada con Claude Code — {fecha}", "marks": [{"type": "em"}] }]
    }
  ]
}
```

### 7. Vincular el subtask al padre (si no quedó vinculado automáticamente)

Verificar en la respuesta de `createJiraIssue` si el `parentKey` fue aceptado.
Si no, usar `mcp__claude_ai_Atlassian__createIssueLink` para crear el vínculo manualmente con tipo `"is subtask of"` o `"relates to"`.

### 8. Transicionar el ticket padre si todo pasó

Si el resultado de las pruebas es 100% PASS:
- Usa `mcp__claude_ai_Atlassian__getTransitionsForJiraIssue` sobre el ticket **padre** (`$ARGUMENTS`)
- Si existe una transición tipo "Done", "Listo", o "Cerrado", aplicarla con `mcp__claude_ai_Atlassian__transitionJiraIssue`

### 9. Reporte final

Mostrar:
- Ruta del MD de evidencia generado/encontrado
- Key del subtask creado en Jira (ej: `ARIA-7`) con link directo: `https://faguero.atlassian.net/browse/ARIA-7`
- Resumen: N casos pasados / N total
- Si el ticket padre fue transicionado, indicar el nuevo estado
- Si hay fallos en las pruebas, listar los casos fallidos sin transicionar el ticket

Si estás en **MODO DRYRUN**, termina con:
```
[DRYRUN] Vista previa completada. No se creó ningún issue en Jira ni se transicionó el ticket padre.
MD de evidencia: postman/reports/evidencia-{TICKET-ID}-{entity}-{fecha}.md (generado/encontrado)
Subtask que se crearía: [Test] {título} - Evidencia CRUD
Transición pendiente: {TICKET-ID} → Listo (si 100% PASS)
Ejecuta `/generar-evidencia-jira {TICKET-ID}` para aplicar los cambios en Jira.
```
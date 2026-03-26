# Skill: generar-newman

Dado el nombre de una entidad o el ID de un ticket Jira (`$ARGUMENTS`), genera los casos de prueba en una nueva colección Newman/Postman y ejecuta las pruebas generando evidencia HTML.

## Pasos a seguir

### 1. Determinar el recurso a probar

Si `$ARGUMENTS` parece un ID de Jira (ej: `ARIA-123`):
- Usa `mcp__claude_ai_Atlassian__getJiraIssue` para obtener el nombre de la entidad y los endpoints
- Extrae el nombre del recurso y el path base (ej: `Comment` → `/api/comments`)

Si `$ARGUMENTS` es un nombre de entidad (ej: `Comment`):
- Deduce el path como `/api/{entidades-en-plural-lowercase}`
- Lee el controller correspondiente en `src/main/java/com/accenture/aria/controller/` para confirmar los endpoints

### 2. Analizar el controller generado
Lee el archivo del controller para identificar:
- Todos los endpoints disponibles (GET, POST, PUT, DELETE)
- Los campos del RequestDTO (leer el DTO correspondiente)
- Los campos del ResponseDTO
- Si la entidad tiene relaciones con otras (ej: necesita crear entidades padre primero)

### 3. Generar la colección Newman

Crea el archivo `postman/{Entity}-CRUD-Casuistica.postman_collection.json` siguiendo el patrón de `Aria-Tickets-CRUD-Casuistica.postman_collection.json`.

La colección debe incluir estos grupos de casos en orden:

**Setup** (si la entidad tiene relaciones foráneas):
- Crear entidades relacionadas necesarias, guardando sus IDs en collectionVariables

**CRUD Exitoso:**
- `POST /api/{entidades}` → 201 Created (guardar ID en variable `{entity}Id`)
- `GET /api/{entidades}` → 200 OK (lista con al menos 1 elemento)
- `GET /api/{entidades}/{{entity}Id}` → 200 OK
- `PUT /api/{entidades}/{{entity}Id}` → 200 OK (verificar campo actualizado)
- `DELETE /api/{entidades}/{{entity}Id}` → 204 No Content

**Casos de Error:**
- `GET /api/{entidades}/99999` → 404 Not Found
- `PUT /api/{entidades}/99999` → 404 Not Found
- `DELETE /api/{entidades}/99999` → 404 Not Found
- `POST /api/{entidades}` con body vacío → 400 Bad Request (si hay validaciones)

**Formato de cada item en la colección:**
```json
{
  "name": "Descripción del caso (código HTTP esperado)",
  "event": [
    {
      "listen": "prerequest",
      "script": { "type": "text/javascript", "exec": ["// setup si hace falta"] }
    },
    {
      "listen": "test",
      "script": {
        "type": "text/javascript",
        "exec": [
          "pm.test('status XXX', function () { pm.response.to.have.status(XXX); });",
          "// assertions adicionales según el caso"
        ]
      }
    }
  ],
  "request": {
    "method": "METHOD",
    "header": [{ "key": "Content-Type", "value": "application/json" }],
    "body": { "mode": "raw", "raw": "{ ... }" },
    "url": { "raw": "{{baseUrl}}/api/{entidades}", "host": ["{{baseUrl}}"], "path": ["api", "{entidades}"] }
  }
}
```

Usar `pm.collectionVariables` para pasar IDs entre requests (igual que las colecciones existentes).

### 4. Agregar script al package.json

Edita `postman/package.json` y agrega:
```json
"test:{entidades}": "newman run {Entity}-CRUD-Casuistica.postman_collection.json -e Aria-local.postman_environment.json --reporters cli,htmlextra --reporter-htmlextra-export reports/{entity}-report.html"
```

También actualiza el script `test:all` para incluir la nueva colección.

Instalar `newman-reporter-htmlextra` si no está en devDependencies:
- Agrégalo a `devDependencies` en `package.json`
- Indica al usuario que ejecute `npm install` en la carpeta `postman/`

### 5. Ejecutar las pruebas

Corre el comando:
```bash
cd postman && npm run test:{entidades}
```

Si el servidor no está corriendo, avisa al usuario que debe iniciarlo con:
```bash
cd .. && mvn spring-boot:run
```
Y luego reintentar la ejecución.

### 6. Reporte final

Muestra:
- Ruta del archivo de colección creado
- Lista de casos de prueba generados con su status esperado
- Resultado de la ejecución (passed/failed por caso)
- Ruta del reporte HTML generado: `postman/reports/{entity}-report.html`
- Si hay fallos, explica el error y sugiere la corrección

Al terminar sugiere: `/generar-evidencia-jira {JIRA-TICKET-ID}` para subir la evidencia al ticket de test.
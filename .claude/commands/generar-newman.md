# Skill: ya

Dado el nombre de una entidad o el ID de un ticket Jira (`$ARGUMENTS`), genera los casos de prueba en una nueva colección Newman/Postman y ejecuta las pruebas generando evidencia HTML.

Uso: `/generar-newman ARIA-5` o `/generar-newman ARIA-5 --dry-run`

## Pasos a seguir

### 0. Detectar modo DRYRUN

Revisa si `$ARGUMENTS` contiene `--dry-run` o `--dryrun` (case-insensitive).

- Si está presente: activar **MODO DRYRUN**. Extrae el ticket ID / nombre de entidad del resto del argumento.
- Si no está presente: ejecutar normalmente.

En **MODO DRYRUN**:
- Ejecutar los pasos 1 y 2 (leer Jira / analizar controller) normalmente
- En el paso 3: mostrar el contenido completo del JSON de la colección que se crearía, pero **NO escribir el archivo**
- En el paso 4: mostrar los scripts que se agregarían a `package.json`, pero **NO editar el archivo**
- **Saltar completamente los pasos 5 y 6** (no compilar, no iniciar servidor, no ejecutar Newman)
- Terminar con el bloque de resumen DRYRUN

### 1. Determinar el recurso a probar

Si `$ARGUMENTS` parece un ID de Jira (ej: `ARIA-123`):
- Usa `mcp__claude_ai_Atlassian__getJiraIssue` para obtener el nombre de la entidad y los endpoints
- Extrae el nombre del recurso y el path base (ej: `Comment` → `/api/comments`)
- Guarda el ID del ticket como `TICKET_ID` (ej: `ARIA-123`) para usarlo en el nombre del reporte

Si `$ARGUMENTS` es un nombre de entidad (ej: `Comment`):
- Deduce el path como `/api/{entidades-en-plural-lowercase}`
- Lee el controller correspondiente en `src/main/java/com/accenture/aria/controller/` para confirmar los endpoints
- Usa `TICKET_ID = "local"` como sufijo del reporte al no haber ID de Jira

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

El nombre del reporte HTML debe incluir el ID del ticket Jira para trazabilidad.

Edita `postman/package.json` y agrega:
```json
"test:{entidades}": "newman run {Entity}-CRUD-Casuistica.postman_collection.json -e Aria-local.postman_environment.json --reporters cli,htmlextra --reporter-htmlextra-export reports/{entity}-{TICKET_ID}-report.html"
```

Ejemplo concreto si el ticket es `ARIA-5`:
```json
"test:comments": "newman run Comment-CRUD-Casuistica.postman_collection.json -e Aria-local.postman_environment.json --reporters cli,htmlextra --reporter-htmlextra-export reports/comment-ARIA-5-report.html"
```

También actualiza el script `test:all` para incluir la nueva colección.

Instalar `newman-reporter-htmlextra` si no está en devDependencies:
- Agrégalo a `devDependencies` en `package.json`
- Ejecuta `npm install` dentro de la carpeta `postman/` usando el Bash tool

### 5. Compilar y reiniciar el servidor

Antes de correr Newman, asegurar que el servidor tenga el código más reciente compilado y corriendo.

**a) Verificar si el servidor ya está corriendo:**
```bash
curl -s --max-time 3 http://localhost:8080/actuator/health 2>/dev/null || curl -s --max-time 3 http://localhost:8080/api/tickets 2>/dev/null
```

**b) Matar el proceso existente si está corriendo:**
En Windows (bash/WSL):
```bash
PID=$(netstat -ano 2>/dev/null | grep ":8080" | grep "LISTENING" | awk '{print $NF}' | head -1)
if [ -n "$PID" ]; then taskkill //F //PID $PID 2>/dev/null; fi
```

**c) Compilar el proyecto:**
```bash
cd {project-root} && mvn compile -q 2>&1 | tail -5
```
Si hay errores de compilación, mostrarlos al usuario y detenerse.

**d) Levantar el servidor en background:**
```bash
cd {project-root} && mvn spring-boot:run > /tmp/aria-server.log 2>&1 &
echo "Servidor iniciando (PID $!)..."
```

**e) Esperar a que el servidor esté listo (máximo 60 segundos):**
```bash
for i in $(seq 1 12); do
  sleep 5
  STATUS=$(curl -s --max-time 3 -o /dev/null -w "%{http_code}" http://localhost:8080/api/tickets 2>/dev/null)
  if [ "$STATUS" = "200" ]; then
    echo "Servidor listo"
    break
  fi
  echo "Esperando... intento $i/12"
done
```

Si después de 60 segundos no responde, mostrar los últimos logs:
```bash
tail -30 /tmp/aria-server.log
```
Y pedir al usuario que revise el error.

**f) Verificar que el endpoint nuevo responde (no 404):**
```bash
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/{entidades}
```
Si retorna 404, el controlador no se registró — revisar logs de startup.
Si retorna 500, leer `/tmp/aria-server.log` para identificar el error y corregirlo antes de continuar.

### 6. Ejecutar las pruebas

Crear el directorio de reportes si no existe:
```bash
mkdir -p {project-root}/postman/reports
```

Correr Newman:
```bash
cd {project-root}/postman && npm run test:{entidades} 2>&1
```

Si hay fallos de assertions (no de conexión), analizar el output e identificar la causa raíz en el código antes de reportar.

### 7. Reporte final

Muestra:
- Ruta del archivo de colección creado (o que se crearía en DRYRUN)
- Lista de casos de prueba generados con su status esperado
- Resultado de la ejecución (passed/failed por caso) — solo en modo normal
- Ruta del reporte HTML generado: `postman/reports/{entity}-{TICKET_ID}-report.html`
- Si hay fallos, explica el error y sugiere la corrección

Si estás en **MODO DRYRUN**, termina con:
```
[DRYRUN] Vista previa completada. No se crearon archivos ni se ejecutó el servidor.
Colección: postman/{Entity}-CRUD-Casuistica.postman_collection.json (pendiente de crear)
Script package.json: "test:{entidades}" (pendiente de agregar)
Ejecuta `/generar-newman {TICKET-ID}` para aplicar los cambios y correr las pruebas.
```

Al terminar en modo normal sugiere: `/generar-evidencia-jira {JIRA-TICKET-ID}` para subir la evidencia al ticket de test.
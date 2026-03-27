# Demo: Feature completa sin tocar código manualmente

---

## SLIDE 1 — El problema de siempre

```
  Ticket Jira asignado
         ↓
  Leer el ticket
         ↓
  Crear Entity, Repository, DTO Request,
  DTO Response, Mapper, Service, Controller...
         ↓
  Escribir unit tests
         ↓
  Armar colección Postman
         ↓
  Ejecutar Newman, capturar evidencia
         ↓
  Subir evidencia al ticket
         ↓
  Mover el ticket a "Done"
```

**Promedio por feature CRUD simple: 2-4 horas de trabajo repetitivo**

---

## SLIDE 2 — El flujo con Claude Code

```
  /generar-feature ARIA-XX
         │
         ├─► Lee el ticket Jira automáticamente
         ├─► Genera 7 archivos siguiendo los patrones del proyecto
         └─► Unit tests incluidos
                    ↓
  /generar-newman Comment
         │
         ├─► Genera colección Postman con happy path + error cases
         ├─► Levanta el servidor, compila, espera a que esté listo
         └─► Corre Newman → genera reporte HTML
                    ↓
  /generar-evidencia-jira ARIA-XX
         │
         ├─► Crea subtask de tipo Test en Jira
         ├─► Sube evidencia en formato ADF (tablas, métricas)
         └─► Mueve el ticket a Done si todo pasó ✅
```

**Todo el pipeline: ~5 minutos. Cero código escrito a mano.**

---

---

# HANDS-ON DEMO

## Pre-requisitos (verificar antes de arrancar)

```bash
# Proyecto compila limpio
./mvnw clean test -q

# Newman instalado
cd postman && npm install && cd ..

# Jira ticket creado (ver abajo)
```

## Ticket Jira para la demo

Crear en Jira el siguiente ticket antes de la sesión:

```
Proyecto: ARIA
Tipo: Story / Feature
Título: Gestión de Comentarios en Tickets

Descripción:
Como usuario del sistema, quiero poder agregar comentarios
a un ticket para registrar avances, notas o bloqueos
durante su ciclo de vida.

Campos del recurso:
- content (texto del comentario, requerido, max 1000 chars)
- author (relación con Person, requerido)
- ticket (relación con Ticket, requerido)
- createdAt, updatedAt (timestamps automáticos)

Endpoints a exponer:
- GET  /api/comments              → listar todos
- GET  /api/comments/{id}         → obtener por ID
- GET  /api/comments/ticket/{id}  → comentarios de un ticket
- POST /api/comments              → crear comentario
- PUT  /api/comments/{id}         → editar comentario
- DELETE /api/comments/{id}       → eliminar comentario

Acceptance Criteria:
- No se puede crear un comentario con content vacío
- Si author o ticket no existen → 404
- Respuesta incluye: id, content, createdAt, author username, ticket title
```

**Anotar el ID del ticket** (ej: `ARIA-5`) antes de empezar la demo.

---

## PASO 1 — Generar la feature desde el ticket Jira

```
/generar-feature ARIA-5
```

**Lo que pasa:**
1. Claude lee el ticket Jira via MCP
2. Analiza la entidad, campos y relaciones
3. Genera los 7 archivos:
   - `model/Comment.java`
   - `repository/CommentRepository.java`
   - `dto/CommentRequestDTO.java`
   - `dto/CommentResponseDTO.java`
   - `service/CommentMapper.java`
   - `service/CommentService.java`
   - `controller/CommentController.java`
4. Genera `test/service/CommentServiceTest.java`

**Mostrar:** abrir uno o dos archivos en el IDE para que los compas vean
que el código sigue los mismos patrones del proyecto (Lombok, constructor
injection, ResourceNotFoundException, etc.)

---

## PASO 2 — Correr los unit tests generados

```bash
./mvnw test -pl . -Dtest=CommentServiceTest -q
```

Deben pasar sin intervención. Si alguno falla, Claude lo diagnostica.

---

## PASO 3 — Generar colección Newman y correr pruebas de integración

```
/generar-newman ARIA-5
```

**Lo que pasa:**
1. Lee el controller generado para inferir endpoints
2. Crea `postman/Comment-CRUD-Casuistica.postman_collection.json`
3. Agrega `npm run test:comments` al `package.json`
4. Compila el proyecto, mata el proceso anterior en :8080 si existe
5. Levanta Spring Boot en background, espera a que responda
6. Ejecuta Newman → CLI output + `postman/reports/comments-report.html`

**Mostrar:** abrir el reporte HTML en el browser mientras habla

---

## PASO 4 — Subir evidencia a Jira y cerrar el ticket

```
/generar-evidencia-jira ARIA-5
```

**Lo que pasa:**
1. Lee el ticket padre en Jira
2. Toma las métricas del reporte Newman
3. Crea subtask de tipo Test con tablas de evidencia en ADF
4. Vincula el subtask al padre
5. Si todo pasó al 100% → transiciona el ticket padre a "Done"

**Mostrar:** abrir Jira en el browser y navegar al ticket para que vean
el subtask creado y el estado actualizado.

---

## Puntos de discusión durante la demo

- **Los skills viven en `.claude/commands/`** → se pueden versionar en el repo
- **Son prompts Markdown**, no código → cualquiera del equipo los puede editar
- **El contexto del proyecto está en `CLAUDE.md`** → Claude sigue los patrones
  sin que nadie le explique la arquitectura cada vez
- **MCP de Atlassian** conecta directamente con Jira sin copiar/pegar
- **Newman corre en CI** → el mismo `npm run test:all` que usa la demo
  se puede meter en el pipeline de GitHub Actions

---

## Comandos de emergencia (si algo falla en vivo)

```bash
# Ver qué está corriendo en :8080
netstat -ano | grep :8080

# Matar el proceso
taskkill /F /PID <PID>

# Levantar manualmente
./mvnw spring-boot:run

# Ver logs si el servidor no levanta
tail -50 /tmp/aria-server.log
```
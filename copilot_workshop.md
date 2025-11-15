# 🧭 Workshop: GitHub Copilot + VS Code (Documento reorganizado y validado)
**Duración:** 1h – 1h 15min  
**Ámbito:** Tribu Java Sharks · Squad Innovación & Capacitación

---

# 🧭 BLOQUE 1 — Introducción y Apertura (5 min)

## Objetivo del Workshop
- Entender cómo funciona GitHub Copilot realmente.
- Aprender a configurarlo y personalizarlo.
- Dominar el contexto (@, #, /) y modos (Inline, Chat, Agente, Edit).
- Preparar un entorno IA-Ready.
- Introducción a buenas prácticas y prompts efectivos.

## Rompehielos
- ¿Quién ya usa Copilot?
- ¿Cuál fue el mejor y el peor prompt que tiraste? 😅

---

# 🧱 BLOQUE 2 — Instalación y Configuración (VS Code) [10–15 min]

## Requisitos previos
- Visual Studio Code actualizado.
- Cuenta GitHub con Copilot activo.
- Internet + login desde VS Code.

## Extensiones esenciales
1. **GitHub Copilot**  
2. **GitHub Copilot Chat**  
3. **(Opcional)** REST Client / Thunder Client, **PlantUML**, **SonarLint / SonarQube IDE**.

## Instalación
- VS Code → *View → Extensions* (Ctrl+Shift+X)
- Buscar e instalar “GitHub Copilot” y “GitHub Copilot Chat”.
- Reiniciar VS Code si lo solicita.

## Iniciar sesión y activar
- Accounts → **Sign in with GitHub**.
- Ver estado: **Copilot: activo** en la barra inferior.

## Configuraciones recomendadas
- Settings (Ctrl+,) → buscar "Copilot":
  - **Enable GitHub Copilot** → ON
  - **Enable GitHub Copilot Chat** → ON
  - **Inline Suggestions** → Enabled
  - **Show Suggested Completions** → ON
- Copilot Chat (⚙️ en el panel):
  - **Use Workspace Instructions** → ON (lee .github/copilot-instructions.md)
  - **Include context from open files** → ON
  - **Remember chat history** → ON
  - **Response language**: Configurable según preferencia
  - **Model**: Depende de tu suscripción (GPT-4 en Copilot Enterprise)

## Atajos útiles
- `Tab` → Aceptar sugerencia inline  
- `Esc` → Descartar sugerencia  
- `Ctrl + Enter` → Ver lista completa de sugerencias
- `Ctrl + Alt + I` → Abrir el chat de Copilot
- `Ctrl + I` → **Inline Chat** (chat contextual en el editor)
- `Alt + [` y `Alt + ]` → Navegar entre sugerencias (cuando hay múltiples)
- *View → Command Palette → "Copilot"* → Acceder a todos los comandos disponibles

## Verificación rápida (2 min)
- Escribir un comentario: `// función para validar email con regex` → aceptar propuesta con **Tab**.
- En Copilot Chat: “explicá este método” o “explicá #selection”.

> *Nota:* Si usás IntelliJ IDEA, esta sección es equivalente (plugins Copilot + Copilot Chat, atajos propios).

---

# 🧠 BLOQUE 3 — Fundamentos: ¿Cómo funciona Copilot? [10–15 min]

Copilot prioriza **contexto** para dar mejores respuestas.

## Fuentes de contexto

### Contexto **implícito** (automático)
- **Archivo actual** y **código seleccionado** (máxima prioridad).
- **Archivos abiertos en pestañas**.
- **Árbol del proyecto** (escaneo semántico).
- **Archivos de configuración** (pom.xml, package.json, .editorconfig, README.md).

### Contexto **explícito** (manual)
**Chat Participants (@):**
- `@workspace` → inspección de todo el proyecto.
- `@terminal` → últimos comandos y salidas del terminal.
- `@vscode` → ayuda con configuración del IDE.
- `@github` → consulta/repos/issues/PRs (según permisos).

**Variables (#):**
- `#file:Nombre.ext` → referencia a un archivo específico.
- `#selection` → el código actualmente seleccionado.
- `#codebase` → búsqueda semántica en todo el proyecto.
- `#terminalLastCommand`, `#terminalSelection` → contexto del terminal.

## Buenas prácticas para maximizar contexto
1) Abrí los archivos relevantes antes de pedir algo.  
2) Seleccioná el código relevante (se detecta automáticamente).  
3) Usá `#file:` o `#selection` para ser explícito.  
4) Usá `@workspace` cuando necesites ver todo el repo.  
5) Mantené **README**, **copilot-instructions.md** y guías al día.

## Ejemplos de prompts (mal vs bien)
- ❌ "creá un endpoint"  
- ✅ "creá un endpoint **POST /api/tickets** que use **#file:TicketService.java** siguiendo el patrón de **#file:TicketController.java** (@workspace)"

---

# 🔤 BLOQUE 3.5 — Sintaxis de Copilot: **@**, **#**, **/**, **?**

## **@ (Agentes y Menciones de Contexto)**
Traen contexto o delegan tareas a agentes especializados.

**Ejemplos**
```text
@workspace explicá la arquitectura del proyecto Aria
@terminal qué hizo el último mvn spring-boot:run (#terminalLastCommand)
@vscode cómo configuro el debugger para Spring Boot en Aria
@github listá mis PR abiertos en accentureshark/aria
```

## **# (Variables de Contexto)**
Referencias a elementos específicos para precisar tu consulta.

**Ejemplos**
```text
Explicá #file:TicketService.java 
Documentá #selection del método create() con Javadoc
¿Qué hace #codebase con el manejo de Status y Priority?
Analizá el error de #terminalLastCommand en la ejecución del build
```

## **/ (Slash Commands)**
Atajos para acciones rápidas en Copilot Chat.
```text
/explain - Explica el código seleccionado
/fix - Sugiere correcciones para problemas
/tests - Genera tests unitarios
/doc - Genera documentación
/optimize - Optimiza el código
/new - Crea nuevo código basado en descripción
/clear - Limpia el historial del chat
```

## **? (Ayuda Contextual)**
Obten ayuda sobre comandos y características:
```text
? - Muestra ayuda general
/? - Muestra ayuda sobre comandos slash
@? - Muestra ayuda sobre participantes del chat
#? - Muestra ayuda sobre referencias de contexto
```

## Combos potentes
```text
@workspace genera /tests para #file:TicketService.java
/fix para #selection del update() siguiendo SOLID
@terminal explica por qué falló #terminalLastCommand del mvn test
/doc para #selection con formato Javadoc del @workspace siguiendo estándares de Aria
```

---

# 💬 BLOQUE 4 — Copilot en el Editor: Inline, Chat, Agent, Edit

Para efectos didácticos, cloná este repositorio: https://github.com/accentureshark/aria

## 1) **Autocompletado Inline** (Completions)
- Sugerencias en tiempo real (ghost text).
- Contexto: archivo actual + abiertos.

**Ejemplo**
```java
// Método para crear un ticket con valores por defecto
public Ticket createTicket(String title, String description) {
    Ticket ticket = new Ticket();
    ticket.setTitle(title);
    ticket.setDescription(description);
    ticket.setStatus(Status.OPEN);
    ticket.setPriority(Priority.MEDIUM);
    ticket.setCreatedAt(LocalDateTime.now());
    return ticket;
}
```

## 2) **Inline Chat** (`Ctrl + I`)
- Chat contextual dentro del editor.
- Acciones: **Edit**, **Insert**, **Explain**, **Accept**, **Discard**.

**Ejemplos**
```text
refactorizá el método findAll() de TicketController usando streams
agregá manejo de excepciones en el método create() de TicketService
extraé la lógica de validación de estado a un método privado validateStatus()
```

## 3) **Panel de Copilot Chat**
- **Agente (@)**: `@workspace`, `@terminal`, `@vscode`, `@github`.
- **Chat libre**: preguntas generales / explicaciones.
- **Edit Mode**: refactor global multiarchivo.
- **Configurar (⚙️)**: activar instrucciones del workspace, open files, idioma, modelo.

---

# ⚙️ BLOQUE 5 — Configuración avanzada del Chat (⚙️)

**Panel de Chat → Icono ⚙️**  
- **Use workspace instructions** → lee `.github/copilot-instructions.md`.  
- **Include context from open files** → usa archivos abiertos como referencia.  
- **Remember chat history** → mantiene contexto conversacional.  
- **Response language** → Español/Inglés (no afecta comprensión de código).  
- **Model** → GPT-4 (si está disponible) / GPT-3.5 (más veloz).

---

# 🧪 BLOQUE 5.5 — Generación de código con Copilot (demo rápido)

Objetivo: mostrar con un ejemplo simple cómo cambia la cantidad y la longitud de prompts, y cómo la personalización (`.github/copilot-instructions.md`, templates) mejora la asertividad y calidad del código generado.

Ejercicio (contexto): "Crear un endpoint REST `GET /api/tickets/filter` que permita filtrar por `status` y `priority` y devuelva `TicketResponseDTO`."

Flujo A — Ad-hoc (rápido, varios prompts)
- Prompt 1: "Agregá al `TicketController` un endpoint `GET /api/tickets/filter` que reciba `status` y `priority` como query params."  
- Prompt 2: "Generá el método `findByFilters()` en `TicketService` que reciba los filtros y llame al repositorio."  
- Prompt 3: "Agregá un método `findByStatusAndPriority()` en `TicketRepository` que busque por status y priority."  

Comentarios: suele requerir 2–4 prompts, cada uno corto; las respuestas son útiles pero puede haber más ida y vuelta y falta de consistencia en validaciones o estilo.

Flujo B — Instrucción personalizada (menos prompts, más preciso)
- Preparación: tener `.github/copilot-instructions.md` con estándares (Java 17, Spring Boot 3.2.8, DTOs validados, Lombok, Problem Details).  
- Prompt único (ejemplo): "@workspace /new: Crear endpoint `GET /api/tickets/filter` en `TicketController` que use `TicketService.findByFilters(status, priority)`. Usar `TicketResponseDTO` con `TicketMapper`, validaciones Jakarta para params opcionales, y devolver `ResponseEntity<List<TicketResponseDTO>>` con manejo de errores usando Problem Details RFC 7807. Seguir los estándares del proyecto Aria y el patrón existente en `#file:TicketController.java`."  

Comentarios: con contexto del workspace y un prompt bien estructurado se logra en 1 prompt más extenso; el resultado suele ser más consistente con estilos del proyecto, validaciones y nombres alineados a las convenciones.

Qué mostrar en la práctica (demo de 5–8 min)
- Ejecutar Flujo A delante del grupo y contar prompts usados.  
- Ejecutar Flujo B mostrando el prompt estructurado y la respuesta optimizada.  
- Concluir: comparar número de prompts, longitud/claridad de prompts y calidad/consistencia del código generado.

Beneficio pedagógico: los asistentes verán por qué BLOQUE 6 (configurar instrucciones, templates y archivos de contexto) reduce iteraciones y mejora la precisión del producto generado.



## Copilotinstruction - java
https://github.com/github/awesome-copilot/blob/54fcb2e06815e31475e8ef164727ce8a1153be82/instructions/java.instructions.md


## prompt para springboot
https://github.com/github/awesome-copilot/blob/54fcb2e06815e31475e8ef164727ce8a1153be82/prompts/java-springboot.prompt.md
---
# 📁 BLOQUE 6 — Archivos que Copilot **realmente** usa (validado)

## Prioridad y soportes

### Invocables explícitamente (vía prompt)
- ✅ `.github/prompts/*.prompt.md` → Ejecutables con `/<nombre-del-prompt>` o desde Command Palette.
  - Alcance: VS Code, compatible con Copilot Enterprise.
  - Extras: Soporta frontmatter con:
    - `tools`: Define herramientas disponibles
    - `mode`: Configura el comportamiento del agente
    - `description`: Describe el propósito del prompt
    - `model`: Especifica el modelo a usar (si está disponible)

### Implícitos (aplicados automáticamente)
- ✅ `.github/copilot-instructions.md` (**PRIORIDAD MÁXIMA**) → Se aplica a todos los requests del workspace.
- ✅ `AGENTS.md` (en raíz) → Instrucciones para agentes; se aplica automáticamente (configurable) y soporta múltiples archivos anidados (experimental).

### Condicionados / Adjuntables (no se invocan con `/`)
- ✅ `.github/instructions/*.instructions.md` → Se aplican según `applyTo`. También podés adjuntarlos explícitamente desde Chat > Add Context > Instructions.
- ✅ `.github/workflows/*.yml` → No ejecutables desde chat; Copilot los usa como referencia para sugerir comandos. Podés adjuntarlos como `#file:`.
- ✅ `README.md`, `pom.xml` / `package.json`, `.editorconfig` → Contexto general; Copilot los usa implícitamente. Podés adjuntarlos con `#file:` cuando sea relevante.

### ⚠️ No oficiales (no influyen directo en Copilot)
- `.github/agents.md` (usar **AGENTS.md** en raíz)
- `.github/coding-guidelines.md` (podés tenerlo, pero no es de lectura prioritaria)
- Issue/PR templates (útiles para GitHub UI, no para Copilot)

### 🧭 Guía rápida
- Ejecutables con `/`: solo `.prompt.md`.
- Efecto automático: `copilot-instructions.md`, `AGENTS.md`.
- Adjuntables como contexto: `*.instructions.md`, workflows, README/config (`#file:` o Add Context…).

### 🗂️ Tabla resumen (invocación vs contexto)

| Archivo | ¿Se invoca con `/`? | ¿Se aplica automático? | ¿Se puede adjuntar? |
|---|---|---|---|
| `.github/prompts/*.prompt.md` | Sí (`/mi-prompt`) | No | Opcional (abrir y ▶) |
| `.github/copilot-instructions.md` | No | Sí (todas las requests) | No necesario |
| `.github/instructions/*.instructions.md` | No | Según `applyTo` | Sí (Add Context > Instructions) |
| `AGENTS.md` (raíz) | No | Sí (configurable) | Opcional (`#file:AGENTS.md`) |
| `.github/workflows/*.yml` | No | No (sólo referencia) | Sí (`#file:ci.yml`) |
| `README.md`, `pom.xml`, `package.json`, `.editorconfig` | No | Implícito como contexto | Sí (`#file:`) |
| No oficiales (guidelines, etc.) | No | No | Sí (`#file:`) |

## Estructura recomendada para Aria
```text
aria/
  .github/
    copilot-instructions.md
    instructions/
      controllers.instructions.md
      model-entities.instructions.md
      services.instructions.md
      tests.instructions.md
    prompts/
      aria-code-review.prompt.md
      aria-new-endpoint.prompt.md
      aria-refactor.prompt.md
    workflows/
      maven-build.yml
  AGENTS.md
  README.md
  pom.xml
  src/
    main/
      java/
        com/accenture/aria/
          controller/
          dto/
          model/
          repository/
          service/
      resources/
        application.properties
```

---

# 🧻 BLOQUE 7 — Ejercicios Prácticos

## Ejercicio 1 — Análisis y Refactor
```text
@workspace analizá la clase TicketService y:
1. Identificá violaciones a SOLID
2. Proponé una refactorización
3. Explicá los beneficios del cambio
4. Mostrá un diagrama de la nueva estructura
5. Implementá los cambios por pasos
```

## Ejercicio 2 — Documentación Técnica
```text
@workspace generá:
1. Diagrama C4 (Contexto y Contenedores)
2. Diagrama de componentes con PlantUML
3. Documentación OpenAPI para endpoints
4. ADR explicando decisiones de diseño
```

## Ejercicio 3 — Testing
```text
/tests para #file:TicketService.java que:
1. Cubran casos de éxito y error
2. Usen @ParameterizedTest
3. Mocken dependencias correctamente
4. Sigan patrón AAA (Arrange-Act-Assert)
```

## Ejercicio 4 — Nueva Feature
```text
@workspace implementá filtrado avanzado de tickets por:
- Estado (OPEN, IN_PROGRESS, RESOLVED, CLOSED)
- Prioridad (LOW, MEDIUM, HIGH, URGENT)
- Fecha de creación (rango: desde/hasta)
- Asignado a (assignee)
- Reportado por (reporter)

Incluir:
1. Endpoint REST GET /api/tickets/search con filtros como query params opcionales
2. Tests unitarios para TicketService.searchTickets()
3. Documentación OpenAPI/Swagger del endpoint
4. Manejo de casos borde (filtros nulos, combinaciones vacías)
5. Usar TicketResponseDTO en la respuesta
```

---


# 🧩 BLOQUE 8 — Personalizar Copilot para tu equipo

### 1) **Instrucciones del Workspace** — `.github/copilot-instructions.md`
```markdown
# Instrucciones para GitHub Copilot - Proyecto Aria

## Estándares del Proyecto
- Java 17, Spring Boot 3.2.8, Maven
- Base de datos: H2 (desarrollo), JPA/Hibernate
- Arquitectura en capas: Controller → Service → Repository
- Principios SOLID

## Estructura del Proyecto
- Modelo: `com.accenture.aria.model` (Ticket, Status, Priority)
- DTOs: `com.accenture.aria.dto` (TicketRequestDTO, TicketResponseDTO)
- Controladores: `com.accenture.aria.controller` (TicketController)
- Servicios: `com.accenture.aria.service` (TicketService, TicketMapper)
- Repositorios: `com.accenture.aria.repository` (TicketRepository)

## Reglas de Código
- Controladores con @RestController y rutas base /api/tickets
- DTOs con validaciones Jakarta (@NotBlank, @Size)
- Usar Lombok (@Data, @Builder, @NoArgsConstructor, @AllArgsConstructor)
- Mappers estáticos en TicketMapper (sin MapStruct)
- ResponseEntity para respuestas HTTP
- Tests unitarios con JUnit 5 + Mockito

## Convenciones
- Enums: Status (OPEN, IN_PROGRESS, RESOLVED, CLOSED)
- Enums: Priority (LOW, MEDIUM, HIGH, URGENT)
- Timestamps: LocalDateTime para createdAt y updatedAt
- IDs: Long con @GeneratedValue(strategy = GenerationType.IDENTITY)

## Calidad
- Código limpio y legible
- Manejo apropiado de Optional
- Sin TODOs sin contexto
```

### 2) **Roles de Agente** — `AGENTS.md`
```markdown
# Roles de Copilot para Aria

## @aria-architect
Arquitecto especializado en:
- Aplicaciones Spring Boot REST
- Diseño de APIs RESTful
- Gestión de tickets y workflows
- Patrones Repository y Service Layer
- JPA/Hibernate y bases de datos

## @aria-reviewer
Revisor de código enfocado en:
- Estándares del proyecto Aria
- Mejores prácticas Spring Boot
- Validaciones y manejo de errores
- Uso correcto de DTOs y mappers
- Convenciones de nomenclatura
- Tests y cobertura de código
```

### 3) **Instrucciones por Dominio** — `.github/instructions/*.instructions.md`
```markdown
# model-entities.instructions.md
applyTo: ["src/main/java/com/accenture/aria/model/**"]

## Reglas de Entidades Aria
- Usar @Entity de Jakarta Persistence
- IDs con @GeneratedValue(strategy = GenerationType.IDENTITY)
- Enums con @Enumerated(EnumType.STRING)
- Lombok: @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @Builder
- LocalDateTime para timestamps
- Validaciones en DTOs, no en entidades

# controllers.instructions.md
applyTo: ["src/main/java/com/accenture/aria/controller/**"]

## Estándares API REST de Aria
- Ruta base: /api/tickets
- @RestController sin @RequestMapping en clase
- @Valid en @RequestBody con DTOs
- ResponseEntity<T> para respuestas
- Códigos HTTP: 200 OK, 201 Created, 204 No Content, 404 Not Found
- Usar TicketMapper para conversión Entity ↔ DTO
- Inyección de dependencias por constructor
```

### 4) **Plantillas de Prompts** — `.github/prompts/*.prompt.md`
```markdown
# aria-code-review.prompt.md
---
mode: reviewer
tools: ['problems', 'tests', 'search']
description: Revisar código del proyecto Aria
---

## Objetivos Review para Aria
1. Validar uso correcto de DTOs y mappers
2. Verificar validaciones Jakarta en RequestDTOs
3. Revisar manejo de Optional en servicios
4. Validar inyección de dependencias por constructor
5. Verificar uso apropiado de ResponseEntity
6. Detectar problemas en queries JPA

# aria-new-endpoint.prompt.md
---
mode: architect
tools: ['workspace', 'problems']
description: Crear nuevo endpoint REST en Aria
---

## Desarrollo Endpoint en Aria
1. Analizar requerimiento del endpoint
2. Definir ruta y método HTTP apropiado
3. Crear/modificar método en TicketController
4. Implementar lógica en TicketService
5. Agregar método en TicketRepository si es necesario
6. Validar uso de DTOs y TicketMapper
7. Agregar validaciones Jakarta si aplica
8. Documentar con comentarios claros
```

### 5) **Contexto del Proyecto**
- README con arquitectura y decisiones
- Diagramas C4 y componentes
- ADRs (Architecture Decision Records)
- Ejemplos de implementación

**Ejercicio**: ¿Qué reglas de calidad agregarías a las instrucciones de Copilot?

---


# 💻 BLOQUE 9 — DEMOS (15–20 min)

## DEMO 1 — Desarrollo con Copilot Chat

### Feature completa: Comentarios en Tickets
1. Análisis con modo Arquitecto:
```text
@workspace diseñá una extensión de Aria para agregar comentarios a tickets:
- Nueva entidad Comment con relación @ManyToOne a Ticket
- Campos: id, content, author, createdAt
- Endpoints REST para crear y listar comentarios de un ticket
- Seguir el patrón existente en #file:TicketController.java
```

2. Implementación guiada:
```text
/new crear estructura para gestión de comentarios:
- Modelo: Comment con @Entity y relación a Ticket
- DTOs: CommentRequestDTO, CommentResponseDTO
- CommentController con rutas /api/tickets/{ticketId}/comments
- CommentService con lógica de negocio
- CommentRepository extendiendo JpaRepository
- CommentMapper para conversiones Entity ↔ DTO
```

3. Testing y documentación:
```text
/tests generar tests unitarios para CommentService
/doc documentar métodos públicos con Javadoc
```

## DEMO 2 — Mejoras Iterativas

### Refactoring con Inline Chat
1. Seleccionar el método `update()` en TicketService + `Ctrl+I`:
```text
Refactorizar este método para:
- Usar un patrón más funcional con Optional.map()
- Extraer la lógica de actualización de campos a un método privado
- Agregar validación de que el ticket existe antes de actualizar
- Mejorar la legibilidad
```

### Optimización con Agent Mode
```text
@workspace analizá TicketService y TicketController para:
1. Identificar posibles queries N+1 si agregamos comentarios
2. Proponer uso de @EntityGraph o JOIN FETCH
3. Sugerir DTOs de proyección para endpoints de listado
4. Validar que los endpoints devuelven códigos HTTP apropiados
```

## DEMO 3 — Revisión de Código

### Code Review Automatizado de Aria
1. Activar modo Reviewer:
```text
@aria-reviewer revisá #file:TicketService.java enfocándote en:
- Uso apropiado de Optional
- Validaciones antes de persistir
- Manejo de casos nulos en update()
- Mejores prácticas de Spring Boot
- Oportunidades para extraer métodos
```

2. Análisis estático del controller:
```text
/analyze #file:TicketController.java buscar:
- Validaciones faltantes en path variables
- Códigos HTTP incorrectos
- Manejo de excepciones
- Uso correcto de ResponseEntity
- Consistencia con REST best practices
```

3. Mejoras sugeridas:
```text
/fix aplicar correcciones en #selection para:
- Agregar validación de ID en métodos de modificación
- Mejorar manejo de errores con @ExceptionHandler
- Estandarizar respuestas de error
```

---

# ✅ BLOQUE 10 — Resumen y Cierre

Aprendimos:
- Qué es Copilot y cómo piensa por contexto.
- Cómo configurarlo (VS Code + Chat ⚙️).
- Sintaxis @, #, / y combinaciones poderosas.
- Archivos que Copilot realmente usa.
- Estructura IA-Ready y personalización.
- Demos: Inline, Chat, Agent, Edit.

---

# 🏁 BLOQUE 11 — Preparación para el Challenge

Checklist:
- ✅ Repo asignado
- ✅ IDE lista
- ✅ Copilot activo
- ✅ Instrucciones configuradas
- ✅ Ganas de romperla en dupla IA + Shark 🦈🤖

---

## 📚 Fuentes (validado)
- GitHub Docs — *Custom instructions for Copilot*  
- VS Code Docs — *Copilot customization & chat*  
- Especificación **AGENTS.md** (OpenAI)  
- GitHub Docs — *Copilot Chat: participants y slash commands*

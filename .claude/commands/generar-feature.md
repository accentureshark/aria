# Skill: generar-feature

Dado el ID de un ticket Jira de tipo Feature (`$ARGUMENTS`), genera todos los artefactos necesarios para implementarlo en este proyecto Spring Boot.

## Pasos a seguir

### 1. Leer el ticket Jira
Usa la herramienta `mcp__claude_ai_Atlassian__getJiraIssue` con el issue key proporcionado en `$ARGUMENTS` para obtener el título, descripción, acceptance criteria y cualquier detalle técnico del ticket.

Si `$ARGUMENTS` está vacío, pregunta al usuario el ID del ticket Jira antes de continuar.

### 2. Analizar la feature
Del ticket extraer:
- El nombre del recurso/entidad (ej: `Comment`, `Sprint`, `Category`)
- Los campos/atributos que debe tener
- Los endpoints que se deben exponer (CRUD completo por defecto, o los que se especifiquen)
- Relaciones con otras entidades existentes (revisar `src/main/java/com/accenture/aria/model/`)

### 3. Generar los artefactos

Seguir **exactamente** los patrones del proyecto:
- Package base: `com.accenture.aria`
- Lombok en todas las entidades: `@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder`
- Constructor injection (sin `@Autowired`) en services y controllers
- Manejo de errores con `ResourceNotFoundException` de `com.accenture.aria.service.exception`
- `createdAt` / `updatedAt` con `LocalDateTime` en todas las entidades

#### Archivos a crear:

**a) Entidad** → `src/main/java/com/accenture/aria/model/{Entity}.java`
```
@Entity @Table(name = "{entidades}")
Con @Id @GeneratedValue(strategy = IDENTITY)
Enums propios en model/ si aplica
Relaciones @ManyToOne con FetchType.LAZY si referencia otras entidades
```

**b) Repository** → `src/main/java/com/accenture/aria/repository/{Entity}Repository.java`
```
public interface {Entity}Repository extends JpaRepository<{Entity}, Long>
Agregar métodos de búsqueda custom solo si la feature los requiere
```

**c) Request DTO** → `src/main/java/com/accenture/aria/dto/{Entity}RequestDTO.java`
```
Con validaciones Jakarta (@NotBlank, @NotNull, @Size según corresponda)
Lombok @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
```

**d) Response DTO** → `src/main/java/com/accenture/aria/dto/{Entity}ResponseDTO.java`
```
Con todos los campos a exponer en la API
Lombok @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
```

**e) Mapper** → `src/main/java/com/accenture/aria/service/{Entity}Mapper.java`
```
Clase final con métodos static toEntity(RequestDTO) y toResponse(Entity)
Usar el patrón Builder de Lombok
```

**f) Service** → `src/main/java/com/accenture/aria/service/{Entity}Service.java`
```
@Service con constructor injection
Métodos: findAll(), findById(Long), create({Entity}), createFromDTO(DTO),
         update(Long, {Entity}), updateFromDTO(Long, DTO), delete(Long)
Seguir exactamente el patrón de TicketService (timestamps, null-checks, resolveOrThrow)
```

**g) Controller** → `src/main/java/com/accenture/aria/controller/{Entity}Controller.java`
```
@RestController @RequestMapping("/api/{entidades}")
Endpoints: GET /, GET /{id}, POST /, PUT /{id}, DELETE /{id}
Usar @Valid en los @RequestBody
Seguir exactamente el patrón de TicketController
```

**h) Unit Tests** → `src/test/java/com/accenture/aria/service/{Entity}ServiceTest.java`
```
@ExtendWith(MockitoExtension.class)
@Mock para los repositorios, @InjectMocks para el servicio
Cubrir: findAll retorna lista, findById existente, findById inexistente,
        create exitoso, update exitoso, update inexistente, delete exitoso, delete inexistente
Usar assertThat de AssertJ y Mockito (when/verify/assertThrows)
```

### 4. Crear los archivos
Usa la herramienta Write para crear cada archivo. Muestra primero un resumen de lo que vas a generar y espera confirmación si hay ambigüedades en la feature.

### 5. Reporte final
Al terminar, muestra:
- Lista de archivos creados con sus rutas
- Resumen de los endpoints generados (método HTTP + path)
- Cualquier decisión de diseño que tomaste y por qué
- Próximos pasos sugeridos (ej: ejecutar `/generar-newman {Entity}` para generar las pruebas)
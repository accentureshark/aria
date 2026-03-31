# Claude Code — Configuración de Usuario

## Filosofía

**Agent-First**: delegar tareas complejas a agentes especializados. Planificar antes de ejecutar. TDD obligatorio. Seguridad siempre primero.

---

## Reglas del Sistema

@rules/coding-style.md
@rules/git-workflow.md
@rules/testing.md
@rules/security.md
@rules/agents.md
@rules/hooks.md

---

## Agentes Disponibles

Almacenados en `~/.claude/agents/`:

| Agente | Cuándo usarlo |
|--------|--------------|
| `code-reviewer` | Inmediatamente después de escribir o modificar código |
| `tdd-guide` | Al iniciar una nueva feature o resolver un bug |
| `security-reviewer` | Cuando se detecta una vulnerabilidad o antes de un release |

---

## Preferencias Personales

- Sin emojis en código ni documentación
- Commits con Conventional Commits (`feat:`, `fix:`, `refactor:`, `docs:`, `test:`)
- Respuestas concisas y directas
- Español como idioma principal

## Estándares de Calidad

- Tests deben pasar al 100% antes de commitear
- Cobertura mínima del 80%
- Cero vulnerabilidades de seguridad críticas
- Código legible y mantenible

## Stack Principal

- Java 17 + Spring Boot 3.x
- Maven como build tool
- JUnit 5 + Mockito para tests unitarios
- Newman/Postman para tests de integración
- H2 (in-memory) como base de datos de desarrollo
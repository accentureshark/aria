# Git Workflow

## Formato de Commits (Conventional Commits)

```
<tipo>: <descripción corta en imperativo>

[cuerpo opcional]
```

Tipos válidos: `feat`, `fix`, `refactor`, `docs`, `test`, `build`, `chore`, `perf`, `ci`

Ejemplos:
- `feat: add sprint filtering by status`
- `fix: handle null assignee in ticket response`
- `test: add SprintService unit tests`
- `refactor: extract ticket mapper to separate class`

## Proceso de Feature

1. **Planificar** — definir criterios de aceptación antes de codificar
2. **TDD** — escribir test (RED) → implementar (GREEN) → refactorizar (IMPROVE)
3. **Revisar** — usar agente `code-reviewer` después de completar
4. **Commitear** — mensaje claro con Conventional Commits
5. **Push** — revisar diff antes de subir

## Pull Requests

- Título corto (bajo 70 caracteres)
- Incluir: resumen, plan de tests, issues relacionados
- Revisar el historial completo con `git diff main...HEAD`
- Resolver TODOS los conflictos antes de mergear

## Reglas

- Nunca commitear directamente a `main` en proyectos de equipo
- Nunca usar `--no-verify` para saltear hooks
- Nunca forzar push a `main`
- Siempre crear branch por feature/fix
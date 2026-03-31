# Skill: cc (Conventional Commit)

Genera un mensaje de commit siguiendo el estándar [Conventional Commits](https://www.conventionalcommits.org/) basándote en los cambios actualmente en el stage de git.

## Pasos a seguir

### 1. Verificar que hay cambios staged

Ejecuta `git diff --staged --stat` para ver un resumen de lo que hay en stage.

Si no hay nada en stage, informa al usuario:
```
No hay cambios en el stage. Usá git add <archivos> primero.
```
Y detente.

### 2. Analizar los cambios en profundidad

Ejecuta `git diff --staged` para ver el diff completo. Analiza:
- **Qué archivos cambiaron** y en qué capa (modelo, servicio, test, config, docs, etc.)
- **Qué se hizo**: se agregó funcionalidad, se corrigió un bug, se refactorizó, se agregaron tests, etc.
- **El alcance (scope)**: el módulo o componente afectado (ej: `auth`, `tickets`, `persons`, `config`)
- **Si hay breaking changes**: cambios que rompen compatibilidad con versiones anteriores

### 3. Elegir el tipo correcto

| Tipo | Cuándo usarlo |
|------|---------------|
| `feat` | Nueva funcionalidad para el usuario |
| `fix` | Corrección de un bug |
| `refactor` | Cambio de código que no agrega feature ni corrige bug |
| `test` | Agregar o corregir tests |
| `docs` | Cambios solo en documentación |
| `style` | Formato, espacios, comas — sin cambio de lógica |
| `perf` | Mejora de performance |
| `chore` | Tareas de mantenimiento, dependencias, build |
| `ci` | Cambios en CI/CD (pipelines, workflows) |
| `build` | Cambios en el sistema de build (Maven, npm, Gradle) |

### 4. Construir el mensaje

Formato: `<tipo>(<scope>): <descripción>`

Reglas para el mensaje:
- **Descripción**: en minúsculas, en inglés, imperativo presente ("add" no "added", "fix" no "fixed"), sin punto al final, máximo 72 caracteres
- **Scope**: opcional, en minúsculas, nombre del módulo o feature afectada
- **Breaking change**: agregar `!` después del tipo/scope, ej: `feat(auth)!: change token format`
- **Body**: si los cambios son complejos, agregar una línea en blanco y luego una explicación de por qué (no del qué)
- **Footer**: si hay breaking change, agregar `BREAKING CHANGE: <descripción>` en el footer

**Ejemplos buenos:**
```
feat(tickets): add priority filter endpoint
fix(persons): return 404 when person not found on update
refactor(service): extract ticket validation to separate method
test(tickets): add unit tests for TicketService CRUD
chore(deps): upgrade spring-boot to 3.3.0
docs: add API usage examples to README
```

### 5. Mostrar el mensaje propuesto

Muestra claramente el mensaje generado:
```
Mensaje de commit sugerido:

  feat(tickets): add priority filter to ticket list

¿Hacemos el commit con este mensaje? (si decís que no, podés sugerir correcciones)
```

Si hay un body o footer, mostrarlo también:
```
  feat(auth)!: change JWT token format to include roles

  Previous format only included userId. New format adds roles array
  to support fine-grained authorization.

  BREAKING CHANGE: clients must update token parsing logic
```

### 6. Confirmar y commitear

Espera confirmación del usuario. Si confirma (o no dice nada negativo), ejecuta:

```bash
git commit -m "$(cat <<'EOF'
<mensaje completo aquí>
EOF
)"
```

Si el usuario sugiere cambios al mensaje, ajústalo y vuelve a mostrar antes de commitear.

### 7. Confirmar el resultado

Ejecuta `git log --oneline -1` y muestra el commit creado:
```
✓ Commit creado:
  abc1234 feat(tickets): add priority filter to ticket list
```
# Hooks System

## Tipos de Hooks Activos

| Hook | Cuándo dispara | Qué hace |
|------|---------------|----------|
| `PreToolUse` | Antes de ejecutar una herramienta | Validación, bloqueos, recordatorios |
| `PostToolUse` | Después de ejecutar una herramienta | Formateo, verificaciones, logs |
| `Stop` | Al terminar cada respuesta | Chequeos finales |

## Hooks Configurados

**Antes de Bash:**
- Bloquea `mvnw spring-boot:run` fuera de tmux (requiere sesión persistente)
- Recuerda usar tmux para comandos largos (`mvn test`, `mvn package`)
- Muestra recordatorio antes de `git push`

**Después de editar archivos Java:**
- Advierte si hay `System.out.println` en el archivo editado

**Al terminar cada respuesta (Stop):**
- Verifica `System.out.println` en archivos Java modificados

## Permisos

- Habilitar auto-accept solo para planes bien definidos
- Mantener deshabilitado durante trabajo exploratorio
- Configurar `allowedTools` en settings.json, nunca usar `--dangerously-skip-permissions`

## TodoWrite

Usar `TodoWrite` para tareas multi-paso: documenta progreso en tiempo real y expone gaps antes de que causen problemas.
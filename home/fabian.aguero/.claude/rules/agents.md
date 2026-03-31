# Agent Orchestration

## Agentes Disponibles

### code-reviewer
Evalúa código escrito o modificado.
- Activar: inmediatamente después de completar una feature o fix
- Prioridad: resolver problemas CRITICAL y HIGH antes de commitear

### tdd-guide
Guía el proceso de TDD para nuevas features o bugs.
- Activar: al iniciar cualquier feature nueva o al resolver un bug
- Sigue el ciclo: RED → GREEN → IMPROVE

### security-reviewer
Analiza vulnerabilidades de seguridad.
- Activar: ante cualquier sospecha de vulnerabilidad
- Activar: antes de exponer un endpoint nuevo al exterior

## Patrones de Uso

**Operaciones independientes → ejecutar en paralelo:**
```
Task 1: code-reviewer analiza TicketService
Task 2: security-reviewer analiza TicketController
```

**Tareas dependientes → secuencial:**
```
1. tdd-guide escribe tests
2. implementar código
3. code-reviewer revisa
```

## Reglas

- SIEMPRE usar ejecución paralela para tareas independientes
- Delegar revisiones al agente correspondiente, no hacerlas manualmente
- Para problemas complejos, usar múltiples agentes con perspectivas diferentes
- Los agentes tienen scope limitado: deben enfocarse en su especialidad
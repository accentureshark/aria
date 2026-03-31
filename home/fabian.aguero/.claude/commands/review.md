Activa el agente `code-reviewer` para revisar los cambios recientes del repositorio.

1. Ejecuta `git diff HEAD` para ver todos los cambios no commiteados
2. Si no hay cambios staged, ejecuta `git diff HEAD~1` para ver el último commit
3. Delega la revisión completa al agente `code-reviewer`
4. Lista los problemas encontrados por severidad (CRITICAL → LOW)
5. Propón correcciones concretas para CRITICAL y HIGH
6. Indica el veredicto final: APROBADO o REQUIERE CAMBIOS
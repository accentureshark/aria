Activa el agente `security-reviewer` para analizar el código en busca de vulnerabilidades.

Argumentos opcionales: $ARGUMENTS (archivo o directorio específico a revisar)

1. Si se especifica $ARGUMENTS, revisar solo ese archivo/directorio
2. Si no, revisar todos los archivos modificados: `git diff --name-only HEAD`
3. Delega el análisis completo al agente `security-reviewer`
4. El agente revisará: secrets hardcodeados, SQL injection, exposición de datos, validación de inputs
5. Lista las vulnerabilidades por severidad (CRITICAL → LOW)
6. Para CRITICAL: detener todo y corregir antes de continuar
7. Reporte final: LIMPIO o VULNERABILIDADES ENCONTRADAS
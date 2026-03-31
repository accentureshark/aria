# Security Guidelines

## Checklist Antes de Commitear

- [ ] Sin credenciales, API keys ni secrets hardcodeados
- [ ] Todos los inputs validados con Jakarta Validation
- [ ] Queries usando parámetros JPA (nunca concatenación de strings)
- [ ] Mensajes de error sin información sensible (stack traces, rutas internas)
- [ ] Sin datos de usuarios en logs

## Manejo de Secrets

NUNCA:
```java
String apiKey = "sk-proj-xxxxx"; // PROHIBIDO
```

SIEMPRE:
```java
@Value("${app.api.key}")
private String apiKey; // Desde application.properties o variable de entorno
```

## Prevención de Vulnerabilidades Comunes

**SQL Injection**: Usar Spring Data JPA / JPQL con parámetros nombrados. Nunca concatenar strings en queries.

**Exposición de datos**: Los DTOs de respuesta nunca deben incluir campos sensibles (contraseñas, tokens, datos internos de infraestructura).

**Validación**: Validar en el borde de entrada (DTOs). No confiar en datos que vienen del cliente.

## Protocolo ante Vulnerabilidad Detectada

1. Detener el trabajo actual
2. Escalar al agente `security-reviewer`
3. Resolver vulnerabilidades críticas antes de continuar
4. Revocar secrets comprometidos inmediatamente
5. Auditar el codebase por problemas similares
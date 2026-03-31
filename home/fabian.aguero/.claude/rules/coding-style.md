# Coding Style — Java / Spring Boot

## Principios Clave

**Inmutabilidad**: Preferir objetos inmutables. Usar `final` en campos, evitar setters innecesarios. Nunca mutar parámetros de entrada.

**Organización de archivos**:
- 200-400 líneas por clase, máximo 800
- Una responsabilidad por clase (SRP)
- Organizar por feature/dominio, no por tipo de archivo

**Nomenclatura**:
- Clases: `PascalCase` (ej. `TicketService`)
- Métodos y variables: `camelCase` (ej. `findById`)
- Constantes: `UPPER_SNAKE_CASE`
- Packages: `lowercase` (ej. `com.accenture.aria.service`)

## Manejo de Errores

- Usar excepciones específicas, nunca capturar `Exception` genérica sin causa
- Propagar excepciones de negocio con mensajes claros
- Usar `@RestControllerAdvice` para respuestas de error consistentes
- Nunca exponer stack traces al cliente

## Validación de Input

- Usar anotaciones Jakarta Validation en DTOs (`@NotBlank`, `@Email`, `@Size`)
- Validar en el borde del sistema (controller/DTO), no en el servicio
- Retornar 400 con detalles del campo inválido

## Checklist Antes de Commitear

- [ ] Sin `System.out.println` ni logs de debug
- [ ] Funciones bajo 50 líneas
- [ ] Sin números mágicos (usar constantes)
- [ ] Manejo de errores implementado
- [ ] Sin lógica duplicada
- [ ] DTOs separados de entidades JPA
# Testing — JUnit 5 + Mockito + Newman

## Cobertura Mínima: 80%

## Tipos de Tests Requeridos

| Tipo | Framework | Qué cubre |
|------|-----------|-----------|
| Unitarios | JUnit 5 + Mockito | Servicios, mappers, utilidades |
| Integración | Newman/Postman | APIs REST end-to-end |

## TDD — Proceso Obligatorio para Features Nuevas

1. **RED**: Escribir test que falla
2. **GREEN**: Implementar el mínimo código para que pase
3. **IMPROVE**: Refactorizar manteniendo los tests verdes
4. **VERIFY**: Confirmar cobertura >= 80%

## Convenciones de Tests Unitarios

```java
// Nomenclatura: metodo_escenario_resultadoEsperado
@Test
void findById_existingId_returnsSprint() { ... }

@Test
void findById_nonExistingId_returnsEmpty() { ... }
```

- Usar `@Mock` e `@InjectMocks`, nunca mocks de base de datos real
- Un assert por test (cuando sea posible)
- Tests independientes entre sí (sin estado compartido)
- Cubrir: happy path + casos de error + edge cases

## Tests de Integración (Newman)

- Cubrir todos los endpoints del recurso
- Incluir: 201/200/204 (happy path) + 400/404/409 (errores)
- Ejecutar con: `cd postman && npm run test:<recurso>`
- Generar reporte HTML como evidencia

## Cuando los Tests Fallan

1. Consultar el agente `tdd-guide`
2. Revisar aislamiento del test
3. Validar mocks implementados correctamente
4. Corregir la implementación, NUNCA el test (salvo que el test esté mal escrito)
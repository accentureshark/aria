# Postman + Newman (CRUD Casuistica)

Estas colecciones cubren casos felices y errores de las entidades actuales:

- `Person` (`/api/persons`)
- `Ticket` (`/api/tickets`)

## Archivos

- `Aria-Persons-CRUD-Casuistica.postman_collection.json`
- `Aria-Tickets-CRUD-Casuistica.postman_collection.json`
- `Aria-local.postman_environment.json`
- `package.json` (scripts para Newman)

## Casuistica cubierta

### Persons

- Crear persona valida (`201`)
- Crear duplicado por unique (`409`)
- Validacion request (`400`)
- Listar (`200`)
- Buscar por id/email/username/department (`200`)
- Actualizar (`200`)
- Desactivar (`200`)
- Eliminar (`204`)
- Eliminar nuevamente y consultar borrado (`404`)

### Tickets

- Crear ticket minimo con defaults (`201`, `OPEN`, `MEDIUM`)
- Crear ticket con reporter/assignee por ID (`201`)
- Validacion request (`400`)
- Referencia a persona inexistente (`404`)
- Listar y obtener por id (`200`)
- Actualizar ticket (`200`)
- Actualizar ticket inexistente (`404`)
- Eliminar ticket (`204`) y eliminar nuevamente (`404`)

## Ejecutar con Newman

1) Levantar la app Spring Boot en `http://localhost:8080`

2) Desde la carpeta `postman`, instalar dependencias y correr tests

```powershell
Set-Location "C:\Users\fabian.aguero\dev\aria\postman"
npm install
npm run test:all
```

Opcional por coleccion:

```powershell
npm run test:persons
npm run test:tickets
```


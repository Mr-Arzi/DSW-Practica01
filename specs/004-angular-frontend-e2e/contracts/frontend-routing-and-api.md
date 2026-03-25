# Frontend Interface Contract

## 1. UI Routing Contract

### Public Route
- `GET /login`
  - Access: Public
  - Purpose: Capturar correo y contrasena

### Protected Routes
- `GET /empleados`
  - Access: Authenticated (`ADMIN` or `LECTOR`)
  - Behavior: Mostrar listado y acciones segun rol

- `GET /departamentos`
  - Access: Authenticated (`ADMIN` or `LECTOR`)
  - Behavior: Mostrar listado y acciones segun rol

### Navigation Rules
- Login exitoso redirige a `/empleados`.
- Usuario no autenticado en ruta protegida redirige a `/login`.
- Logout limpia sesion y redirige a `/login`.

## 2. Backend API Consumption Contract (`/api/v1`)

All protected calls MUST include:
- Header: `Authorization: Basic <base64(email:password)>`
- Header: `Content-Type: application/json` (when body exists)

### Authentication Endpoint
- `POST /api/v1/auth/login`
  - Request: `{ "email": string, "password": string }`
  - Success: `200`
  - Failure: `400` (validacion) | `401` (credenciales invalidas)

### Empleados Endpoints
- `GET /api/v1/empleados`
- `POST /api/v1/empleados`
- `PUT /api/v1/empleados/{clave}`
- `DELETE /api/v1/empleados/{clave}`

Expected status families:
- Read: `200`
- Create: `201`
- Update: `200`
- Delete: `204`
- Errors: `400`, `401`, `403`, `404`, `409`

### Departamentos Endpoints
- `GET /api/v1/departamentos`
- `POST /api/v1/departamentos`
- `PUT /api/v1/departamentos/{id}`
- `DELETE /api/v1/departamentos/{id}`

Expected status families:
- Read: `200`
- Create: `201`
- Update: `200`
- Delete: `204`
- Errors: `400`, `401`, `403`, `404`, `409`

## 3. Role-Based UI Contract

- `ADMIN`:
  - Can see and execute CRUD actions in empleados/departamentos.
- `LECTOR`:
  - Can view list/detail screens.
  - Must not see enabled create/edit/delete actions.

## 4. Cypress E2E Contract

Minimum required scenarios:
1. Login exitoso.
2. Login con credenciales incorrectas.
3. Navegacion entre modulos.
4. Visualizacion de lista de empleados.
5. Creacion de empleado.
6. Visualizacion de lista de departamentos.
7. Creacion de departamento.

Test data policy:
- Suite prepara o asegura datos al inicio.
- Suite limpia datos de prueba al finalizar cuando aplique.

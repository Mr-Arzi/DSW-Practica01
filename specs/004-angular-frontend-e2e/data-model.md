# Data Model: Frontend Angular con Pruebas E2E

## Entity: SesionAutenticada

### Fields
- `email` (string, required)
- `basicAuthHeader` (string, required, derivado de `email:password` en Base64)
- `isAuthenticated` (boolean, required)
- `role` (enum: `ADMIN` | `LECTOR`, required para control UI)

### Validation Rules
- `email` debe tener formato valido de correo.
- `basicAuthHeader` debe existir para llamadas protegidas.
- `isAuthenticated=true` solo si login fue exitoso.

### State Transitions
- `unauthenticated` -> `authenticated` al login exitoso.
- `authenticated` -> `unauthenticated` al logout, credenciales invalidas, o respuesta 401/403 terminal.

## Entity: EmpleadoViewModel

### Fields
- `clave` (string)
- `nombre` (string)
- `direccion` (string)
- `telefono` (string)
- `departamentoId` (number)
- `departamentoNombre` (string)
- `activo` (boolean)
- `email` (string)

### Validation Rules
- Campos requeridos para alta/edicion deben respetar validaciones del backend.
- Errores de validacion backend se reflejan en UI por campo.

## Entity: DepartamentoViewModel

### Fields
- `id` (number)
- `clave` (string)
- `nombre` (string)

### Validation Rules
- `clave` y `nombre` son requeridos para alta/edicion.
- Duplicidad o conflictos de integridad deben mostrarse como error de negocio en UI.

## Entity: NavigationState

### Fields
- `currentRoute` (enum: `login` | `empleados` | `departamentos`)
- `menuItems` (array)
- `canWrite` (boolean)

### Validation Rules
- `currentRoute` no puede ser `empleados/departamentos` si `isAuthenticated=false`.
- `canWrite=false` para rol `LECTOR`.

## Entity: E2EScenario

### Fields
- `name` (string)
- `preconditions` (array)
- `steps` (array)
- `expectedResults` (array)

### Validation Rules
- Cada escenario debe ser reproducible sin dependencia de datos manuales previos.
- Flujos minimos requeridos por FR-014 deben existir como casos separados.

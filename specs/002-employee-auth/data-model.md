# Data Model: Employee Authentication via Email & Password

## Entity: Empleado (extended)

### Fields
- `prefijoClave` (string, required, fixed `EMP-`)
- `numeroClave` (integer, required, auto-generated)
- `clave` (string derived, unique, format `EMP-<n>`)
- `nombre` (string, required, max 100)
- `direccion` (string, required, max 100)
- `telefono` (string, required, regex `^[0-9]{1,100}$`)
- `activo` (boolean, required, default `true`)
- `email` (string, required for authenticated employees, normalized lowercase)
- `password_hash` (string, required for authenticated employees, bcrypt hash)

### Validation Rules
- `email` must be syntactically valid and unique for active authenticated employees.
- `password_hash` must never store plaintext passwords.
- Login can succeed only when `activo=true`, `email` exists, and password hash matches.
- For invalid email/password combinations, return generic unauthorized response.

## Entity: LoginRequest (DTO)
- `email` (string, required)
- `password` (string, required)

## Entity: LoginSuccessResponse (DTO)
- `message` (string, fixed value `Authentication successful`)

## Entity: LoginErrorResponse (DTO)
- `error` (string, fixed value `Invalid email or password`)

## Relationships
- `LoginRequest.email` maps to a single `Empleado.email`.
- Credential validation compares `LoginRequest.password` against `Empleado.password_hash`.

## State Transitions
- Valid credentials: request processed, no persisted state change required.
- Invalid credentials: request rejected with `401`, no persisted state change.

## Migration Impact
- Extend `empleado` schema with `email` and `password_hash`.
- Add lookup index/constraint to support deterministic auth by email.
- Legacy rows without credentials remain out of auth scope in this feature.

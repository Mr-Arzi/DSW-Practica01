# Data Model: CRUD de Departamentos

## Entity: Departamento

### Fields
- `id` (long, primary key, auto-generated)
- `clave` (string, required, unique, normalized uppercase)
- `nombre` (string, required, non-blank)

### Validation Rules
- `clave` no puede ser vacia.
- `clave` se normaliza a MAYUSCULAS antes de persistencia.
- `clave` debe ser unica case-insensitive.
- `nombre` no puede ser vacio ni contener solo espacios.

## Entity: Empleado (extended)

### Additional Field
- `departamento_id` (foreign key, required, references `departamentos.id`)

### Validation Rules
- Todo empleado debe referenciar un departamento valido.
- No se permite referencia a departamento inexistente.

## Relationships
- `Departamento` 1 -> N `Empleado`
- `Empleado` N -> 1 `Departamento`

## State Transitions
- Create departamento: nuevo registro activo para asociacion de empleados.
- Update departamento: modifica `clave`/`nombre` respetando unicidad y validacion.
- Delete departamento:
  - permitido solo si cantidad de empleados asociados = 0.
  - rechazado con conflicto si cantidad > 0.

## Migration Impact
- Crear tabla `departamentos` con constraint de unicidad de `clave`.
- Insert idempotente del departamento `SIN_ASIGNAR`.
- Agregar `departamento_id` a `empleados`.
- Backfill de empleados existentes hacia `SIN_ASIGNAR`.
- Imponer `NOT NULL` y FK en `empleados.departamento_id` tras backfill.

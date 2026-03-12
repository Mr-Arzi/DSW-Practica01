# Data Model: CRUD de Empleados

## Entidad: Empleado

### Campos
- `prefijoClave` (string, parte de PK compuesta lógica, valor fijo `EMP-`, requerido)
- `numeroClave` (number, parte de PK compuesta lógica, autonumérico, requerido)
- `clave` (string derivada única para exposición, formato `EMP-<autonumérico>`, requerido)
- `nombre` (string, max 100, requerido)
- `direccion` (string, max 100, requerido)
- `telefono` (string, solo dígitos, max 100, requerido)
- `activo` (boolean, requerido, default `true`)
- `createdAt` (timestamp, requerido)
- `updatedAt` (timestamp, requerido)

## Entidad: PaginaciónEmpleadosResponse (DTO de salida)

### Campos
- `page` (number, requerido, mínimo 1, default 1)
- `limit` (number, requerido, mínimo 1, máximo 100, default 10)
- `total` (number, requerido, mínimo 0)
- `data` (array de `Empleado`, requerido)

## Reglas de validación
- `prefijoClave` debe ser siempre `EMP-`.
- `numeroClave` debe ser entero positivo y generado automáticamente.
- `clave` expuesta debe cumplir regex `^EMP-[0-9]+$`.
- `nombre` longitud entre 1 y 100 (tras normalización de espacios de borde).
- `direccion` longitud entre 1 y 100 (tras normalización de espacios de borde).
- `telefono` debe cumplir regex `^[0-9]{1,100}$`.
- No se permite crear duplicados por combinación (`prefijoClave`, `numeroClave`) ni por `clave` derivada.
- No se permite actualizar ni consultar por clave registros con `activo=false` dentro del catálogo funcional.
- Todas las rutas funcionales deben estar bajo `/api/v1`.
- Cualquier solicitud a endpoint fuera de `/api/v1` debe responder `404 Not Found`.
- Todos los endpoints expuestos por el sistema requieren autenticación HTTP Basic.
- Solicitudes sin credenciales válidas en cualquier endpoint expuesto deben responder `401 Unauthorized`.
- Las credenciales iniciales (`admin/admin123`) deben cargarse desde configuración externa o variables de entorno, sin hardcode en código fuente.
- `page < 1`, `limit < 1` o `limit > 100` son inválidos y deben responder error de validación (`400`).
- Si la página solicitada no contiene resultados, la respuesta debe mantener `page`, `limit`, `total` y devolver `data: []` con `200`.

## Relaciones
- No existen relaciones con otras entidades en el alcance de esta feature.
- `PaginaciónEmpleadosResponse.data` contiene una lista de entidades `Empleado` proyectadas al DTO de salida.

## Transiciones de estado
- Alta: `activo` inicia en `true` y se genera automáticamente `numeroClave` para construir `clave`.
- Baja lógica: `activo` cambia de `true` a `false`.
- Actualización: permitida solo cuando `activo=true`.
- Operación de baja sobre `activo=false`: no aplicable, sin cambios de estado.

## Índices sugeridos
- PK lógica: (`prefijoClave`, `numeroClave`).
- Unique derivado: `clave`.
- Índice filtrado o compuesto para listados de activos: (`activo`, `clave`).

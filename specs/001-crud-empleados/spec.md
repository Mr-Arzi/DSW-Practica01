# Feature Specification: CRUD de Empleados

**Feature Branch**: `001-crud-empleados`  
**Created**: 2026-02-25  
**Status**: Draft  
**Input**: User description: "Crea un CRUD de empleados con los campos clave, nombre, direccion y telefono. Donde clave sea el PK y nombre, direccion y telefono sea de 100 caracteres"

## Clarifications

### Session 2026-02-25

- Q: ¿Qué formato debe aceptar `telefono`? → A: Solo dígitos (`0-9`), hasta 100 caracteres.

### Session 2026-02-26

- Q: ¿Cómo debe comportarse la eliminación? → A: Baja lógica (`activo=false`), manteniendo el registro.
- Q: ¿Cómo deben tratarse empleados inactivos en consulta/actualización? → A: No disponibles para `GET by clave` ni `PUT`.
- Q: ¿Cómo se definen los permisos por rol? → A: `ADMIN` crea/actualiza/elimina y `LECTOR` solo consulta.
- Q: ¿Cómo debe definirse la clave del empleado? → A: Prefijo fijo `EMP-` + autonumérico como PK compuesta lógica.

### Session 2026-03-05

- Q: ¿Cómo deben exponerse las rutas de API? → A: Todas bajo `/api/v1`, sin endpoints fuera de versión.
- Q: ¿Qué comportamiento de autenticación se requiere? → A: HTTP Basic obligatorio con `admin`/`admin123` inicial y respuesta `401` sin credenciales válidas.
- Q: ¿Cómo deben responder los endpoints de colección? → A: Con paginación por `page` y `limit` y estructura `{page, limit, total, data}`.

### Session 2026-03-06

- Q: ¿Cómo convivirán credenciales iniciales obligatorias y RBAC existente? → A: Se mantiene RBAC `ADMIN`/`LECTOR`; `admin/admin123` es la credencial inicial obligatoria mínima.
- Q: ¿Qué endpoints cubre la autenticación obligatoria? → A: Todos los endpoints expuestos por el sistema, incluyendo `/api/v1/**` y cualquier otra ruta publicada.
- Q: ¿Cómo responder cuando `page` excede el total disponible? → A: `200 OK` con `data: []` y metadatos de paginación consistentes.
- Q: ¿Qué límite máximo debe aceptar `limit`? → A: Máximo `100`; si se excede, responder `400 Bad Request`.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Registrar empleado (Priority: P1)

Como usuario autorizado, quiero registrar un empleado con nombre, dirección y
teléfono para que el sistema genere su clave y así iniciar el catálogo de empleados.

**Why this priority**: Sin alta de empleados no existe información base para
consultar, editar ni eliminar.

**Independent Test**: Puede probarse creando un empleado nuevo y verificando
que queda disponible en una consulta posterior por su clave.

**Acceptance Scenarios**:

1. **Given** un usuario autenticado, **When** envía los datos válidos de un
  empleado, **Then** el sistema crea el empleado, genera una clave con formato
  `EMP-<autonumérico>` y confirma el registro.
2. **Given** altas concurrentes de empleados, **When** el sistema genera las
  claves, **Then** cada empleado recibe una clave única sin colisiones.
3. **Given** un usuario autenticado, **When** intenta registrar nombre,
  dirección o teléfono con más de 100 caracteres, **Then** el sistema rechaza
  la solicitud e indica el campo inválido.

---

### User Story 2 - Consultar empleados (Priority: P2)

Como usuario autorizado, quiero consultar el listado completo y el detalle por
clave para revisar la información almacenada.

**Why this priority**: Después de crear datos, la consulta permite validar la
integridad del catálogo y habilita operación diaria.

**Independent Test**: Puede probarse solicitando lista y detalle de empleados
existentes, verificando que los datos devueltos coinciden con los registrados.

**Acceptance Scenarios**:

1. **Given** empleados existentes, **When** el usuario consulta el listado,
  **Then** el sistema devuelve la página solicitada de empleados disponibles
  junto con metadatos `page`, `limit` y `total`.
2. **Given** una clave existente, **When** el usuario consulta por clave,
   **Then** el sistema devuelve el empleado correspondiente.
3. **Given** una clave inexistente o inactiva, **When** el usuario consulta por
  clave, **Then** el sistema responde que el empleado no existe en catálogo
  activo.
4. **Given** una consulta de colección, **When** el usuario envía `page` y
  `limit` o no los envía, **Then** el sistema responde resultados paginados con
  `page`, `limit`, `total` y `data`, aplicando por defecto `page=1` y `limit=10`.

---

### User Story 3 - Actualizar y eliminar empleados (Priority: P3)

Como usuario autorizado, quiero actualizar y eliminar empleados para mantener
el catálogo vigente y sin datos obsoletos.

**Why this priority**: Completa el ciclo CRUD y asegura mantenimiento continuo
de la información ya registrada.

**Independent Test**: Puede probarse editando datos de un empleado existente y
eliminándolo después, verificando que ya no se encuentre disponible.

**Acceptance Scenarios**:

1. **Given** una clave existente, **When** el usuario actualiza nombre,
  dirección o teléfono con datos válidos, **Then** el sistema guarda los
  cambios y retorna el registro actualizado.
2. **Given** una clave existente, **When** el usuario elimina el empleado,
  **Then** el sistema marca el empleado como inactivo, confirma la operación y
  deja de mostrarlo en el catálogo activo.
3. **Given** una clave inexistente, **When** el usuario intenta actualizar o
  eliminar, **Then** el sistema informa que no existe el empleado objetivo.
4. **Given** una clave inactiva, **When** el usuario intenta actualizar,
  **Then** el sistema rechaza la operación por no disponible en catálogo
  activo.

---

### Edge Cases

- Nombre, dirección o teléfono con exactamente 100 caracteres deben aceptarse.
- Nombre, dirección o teléfono con 101 o más caracteres deben rechazarse.
- Cualquier `clave` que no cumpla el patrón `EMP-<autonumérico>` debe rechazarse.
- El sistema debe evitar colisiones de `clave` ante altas concurrentes.
- `telefono` con caracteres no numéricos debe rechazarse.
- El sistema debe manejar espacios al inicio o final en `nombre` y `direccion`
  sin romper la validación de longitud.
- Intentos de crear, actualizar o eliminar sin autenticación válida deben ser
  rechazados.
- Usuarios con rol `LECTOR` intentando crear, actualizar o eliminar deben ser
  rechazados por permisos insuficientes.
- Intentar eliminar un empleado ya inactivo debe responder como operación no
  aplicable sin modificar datos.
- Intentar consultar o actualizar un empleado inactivo debe responder como no
  disponible en catálogo activo.
- Intentos concurrentes de alta con la misma clave deben terminar con un único
  registro creado y el resto de operaciones rechazadas.
- Cualquier endpoint fuera del prefijo `/api/v1` debe responder `404 Not Found`.
- Solicitudes sin header `Authorization` deben responder `401 Unauthorized`.
- Solicitudes con credenciales inválidas en HTTP Basic deben responder `401 Unauthorized`.
- Cualquier endpoint expuesto por el sistema debe requerir autenticación HTTP Basic.
- Valores inválidos de paginación (`page < 1` o `limit < 1`) deben responder error de validación.
- Valores de `limit > 100` deben responder `400 Bad Request`.
- Cuando `page` excede el total disponible, la respuesta debe ser `200` con
  `data: []` y metadatos consistentes.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST permitir crear empleados con los campos
  `nombre`, `direccion` y `telefono`, generando automáticamente la `clave`.
- **FR-002**: El sistema MUST tratar `clave` como identificador único y clave
  primaria compuesta lógica del empleado, formada por prefijo fijo `EMP-` y
  un número autonumérico correlativo.
- **FR-002a**: El sistema MUST garantizar unicidad de la `clave` generada y
  evitar colisiones en escenarios concurrentes.
- **FR-003**: El sistema MUST validar que `nombre`, `direccion` y `telefono`
  tengan longitud máxima de 100 caracteres.
- **FR-003a**: El sistema MUST validar que `telefono` contenga únicamente
  dígitos (`0-9`).
- **FR-004**: El sistema MUST permitir consultar todos los empleados y también
  consultar un empleado por `clave`, considerando solo el catálogo activo.
- **FR-005**: El sistema MUST permitir actualizar `nombre`, `direccion` y
  `telefono` de un empleado activo identificado por `clave`.
- **FR-006**: El sistema MUST permitir eliminar empleados por `clave` mediante
  baja lógica, marcando el registro como inactivo (`activo=false`).
- **FR-007**: El sistema MUST responder con errores claros cuando el empleado
  no exista o cuando los datos no cumplan las restricciones definidas.
- **FR-008**: El sistema MUST requerir autenticación para todas las operaciones
  de CRUD de empleados bajo `/api/v1/empleados`.
- **FR-009**: El sistema MUST autorizar por rol: `ADMIN` para crear,
  actualizar y eliminar; `LECTOR` únicamente para consultas.
- **FR-010**: El sistema MUST exponer todos los endpoints únicamente bajo el
  prefijo versionado `/api/v1`.
- **FR-011**: El sistema MUST NO exponer endpoints fuera de rutas versionadas
  de API y cualquier solicitud fuera del prefijo `/api/v1` MUST responder
  `404 Not Found`.
- **FR-012**: El sistema MUST mantener una estructura de rutas que permita
  incorporar futuras versiones (por ejemplo `/api/v2`) sin romper compatibilidad
  con `/api/v1`.
- **FR-013**: El sistema MUST requerir autenticación HTTP Basic en todos los
  endpoints expuestos por el sistema, incluyendo `/api/v1/**` y cualquier otra
  ruta publicada.
- **FR-014**: El sistema MUST configurar credenciales iniciales del sistema con
  `username=admin` y `password=admin123`, manteniendo el esquema de roles
  existente (`ADMIN`/`LECTOR`) definido en la especificación, y MUST obtener
  dichas credenciales desde variables de entorno o configuración externa (sin
  hardcode en el código fuente).
- **FR-015**: El sistema MUST responder `401 Unauthorized` cuando falte el
  header `Authorization` o las credenciales sean inválidas en cualquier
  endpoint expuesto por el sistema, y MUST permitir el acceso cuando las
  credenciales sean válidas.
- **FR-016**: El sistema MUST soportar paginación en todos los endpoints que
  devuelvan colecciones mediante parámetros `page` y `limit`, con valores por
  defecto `page=1` y `limit=10`, y con límite máximo `limit=100`.
- **FR-017**: El sistema MUST responder colecciones paginadas con el formato
  `{ "page": number, "limit": number, "total": number, "data": [] }`.
- **FR-018**: El sistema MUST responder `200 OK` con `data: []` cuando la
  página solicitada no contenga resultados, preservando `page`, `limit` y
  `total` en la respuesta.
- **FR-019**: El sistema MUST responder `400 Bad Request` cuando `limit` sea
  mayor a `100`.

### Constitution Alignment Requirements *(mandatory)*

- **CA-001**: La solución MUST cumplir el estándar de stack y versiones
  establecido por la constitución vigente del proyecto.
- **CA-002**: La solución MUST definir el comportamiento de control de acceso
  para todas las operaciones CRUD de empleados.
- **CA-003**: La solución MUST mantener persistencia relacional consistente y
  describir el impacto de datos de esta funcionalidad.
- **CA-004**: La solución MUST ser ejecutable de forma reproducible en entorno
  local según el estándar operativo del proyecto.
- **CA-005**: La solución MUST mantener la documentación de contrato API
  sincronizada con los cambios de endpoints.
- **CA-006**: La solución MUST exponer rutas de API solo bajo `/api/v1` y
  mantener estrategia de versionado compatible para evoluciones futuras.
- **CA-007**: La solución MUST garantizar HTTP Basic obligatorio y respuesta
  `401 Unauthorized` para solicitudes no autenticadas o con autenticación inválida
  en todos los endpoints expuestos.

### Key Entities *(include if feature involves data)*

- **Empleado**: Representa una persona registrada en el catálogo interno.
  Atributos clave: `prefijoClave` (valor fijo `EMP-`), `numeroClave`
  (autonumérico), `clave` (valor derivado único), `nombre` (máx. 100),
  `direccion` (máx. 100), `telefono` (solo dígitos, máx. 100), `activo`
  (indica si pertenece al catálogo activo).

## Assumptions

- `clave` no es provista por el usuario y se genera automáticamente con
  prefijo fijo `EMP-` y secuencia autonumérica.
- La validación de longitud aplica al valor final recibido para cada campo.
- `telefono` no permite separadores ni prefijos; solo caracteres numéricos.
- El CRUD está orientado a un único catálogo de empleados sin jerarquías ni
  relaciones adicionales en esta entrega.
- Las consultas funcionales de este CRUD exponen únicamente empleados activos.
- Los endpoints de colección deben aceptar obligatoriamente `page` y `limit` con valores por
  defecto `1` y `10`, y devolver metadatos de paginación.
- La ruta base versionada del sistema para esta especificación es `/api/v1`.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de operaciones de alta con datos válidos crea
  correctamente un empleado nuevo.
- **SC-002**: El 100% de operaciones que excedan 100 caracteres en nombre,
  dirección o teléfono son rechazadas con mensaje de validación.
- **SC-002a**: El 100% de operaciones con `telefono` que incluya caracteres no
  numéricos son rechazadas con mensaje de validación.
- **SC-003**: El 100% de consultas por clave existente devuelve exactamente un
  empleado y por clave inexistente devuelve resultado no encontrado.
- **SC-003a**: El 100% de claves generadas por el sistema cumple el patrón
  `EMP-<autonumérico>` y mantiene unicidad.
- **SC-004**: El 100% de operaciones de actualización y eliminación sobre claves
  inexistentes retorna error de no encontrado sin modificar el catálogo.
- **SC-004a**: El 100% de consultas y actualizaciones sobre claves inactivas
  retorna resultado no disponible en catálogo activo.
- **SC-005**: El 100% de operaciones de eliminación válidas conservan el
  registro y lo dejan fuera del catálogo activo.
- **SC-006**: El 100% de solicitudes de modificación hechas por `LECTOR`
  son rechazadas por autorización.
- **SC-007**: El 100% de endpoints funcionales del sistema se exponen bajo
  `/api/v1`, sin endpoints fuera de versión.
- **SC-008**: El 100% de solicitudes sin credenciales o con credenciales
  HTTP Basic inválidas responden `401 Unauthorized` en todos los endpoints
  expuestos.
- **SC-009**: El 100% de endpoints de colección aceptan `page` y `limit`, y
  cuando no se envían aplican `page=1` y `limit=10`.
- **SC-010**: El 100% de respuestas de colección usan la estructura paginada
  con campos `page`, `limit`, `total` y `data`.
- **SC-011**: El 100% de solicitudes con `page` fuera de resultados retorna
  `200 OK` con `data: []` y metadatos de paginación consistentes.
- **SC-012**: El 100% de solicitudes con `limit > 100` retorna `400 Bad Request`.
- **SC-013**: El 100% de pruebas de regresión de contratos de `/api/v1` se mantiene
  sin cambios cuando se habilita una nueva versión de rutas (por ejemplo `/api/v2`).

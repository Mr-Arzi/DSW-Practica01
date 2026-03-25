# Feature Specification: CRUD de Departamentos

**Feature Branch**: `003-departamentos-crud`  
**Created**: 19 de marzo de 2026  
**Status**: Draft  
**Input**: User description: "Nueva funcionalidad: CRUD de departamentos"

## Clarifications

### Session 2026-03-19

- Q: ¿Como migrar empleados existentes al nuevo campo obligatorio de departamento? -> A: Crear un departamento por defecto (`SIN_ASIGNAR`) y asociar empleados existentes a ese departamento.
- Q: ¿Como manejar mayusculas/minusculas en la `clave` de departamento? -> A: Normalizar `clave` a MAYUSCULAS y validar unicidad case-insensitive.
- Q: ¿Que autorizacion aplica por operacion del CRUD de departamentos? -> A: `POST/PUT/DELETE` solo `ADMIN`; `GET` para `ADMIN` y `LECTOR`.
- Q: ¿Que codigos HTTP aplicar para errores de validacion y negocio? -> A: `400` validacion, `404` no encontrado, `409` duplicidad/conflicto de integridad.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Crear Departamento (Priority: P1)

Como administrador del sistema, quiero registrar un nuevo departamento con clave y nombre para mantener actualizada la estructura organizacional.

**Why this priority**: Sin la capacidad de crear departamentos, no existe base para asignar empleados ni para operar el resto del flujo de gestión.

**Independent Test**: Puede probarse de forma independiente enviando una solicitud de alta de departamento y validando que se persiste correctamente con identificador único.

**Acceptance Scenarios**:

1. **Given** que no existe un departamento con clave `FIN`, **When** se crea un departamento con clave `FIN` y nombre `Finanzas`, **Then** el sistema registra el departamento y responde confirmando la creación.
2. **Given** que ya existe un departamento con clave `FIN`, **When** se intenta crear otro departamento con la misma clave, **Then** el sistema rechaza la operación con error de duplicidad.

---

### User Story 2 - Consultar Departamentos (Priority: P1)

Como administrador, quiero consultar la lista de departamentos y los datos de un departamento específico para navegar y revisar la estructura organizacional.

**Why this priority**: La visibilidad de departamentos es esencial para usar la información creada y para la operación diaria.

**Independent Test**: Puede probarse de forma independiente consultando el listado y la consulta por identificador, verificando respuestas correctas para casos existentes e inexistentes.

**Acceptance Scenarios**:

1. **Given** que existen departamentos registrados, **When** se consulta la lista de departamentos, **Then** el sistema devuelve todos los departamentos disponibles.
2. **Given** que existe un departamento con identificador válido, **When** se consulta por su identificador, **Then** el sistema devuelve la información del departamento solicitado.
3. **Given** que no existe un departamento con identificador solicitado, **When** se consulta por ese identificador, **Then** el sistema devuelve respuesta de no encontrado.

---

### User Story 3 - Actualizar Departamento (Priority: P2)

Como administrador, quiero actualizar los datos de un departamento para corregir o mantener vigente su información.

**Why this priority**: La actualización es importante para mantener calidad de datos, pero depende de que los departamentos ya existan.

**Independent Test**: Puede probarse de forma independiente modificando nombre o clave de un departamento y verificando que los cambios se guardan respetando reglas de negocio.

**Acceptance Scenarios**:

1. **Given** que existe un departamento, **When** se actualiza su nombre con un valor válido no vacío, **Then** el sistema guarda y devuelve la información actualizada.
2. **Given** que se intenta actualizar la clave a una clave ya utilizada por otro departamento, **When** se confirma la operación, **Then** el sistema rechaza la actualización por duplicidad.

---

### User Story 4 - Eliminar Departamento con Restricción de Integridad (Priority: P1)

Como administrador, quiero eliminar departamentos que ya no se usan, siempre que no tengan empleados asociados, para mantener el catálogo limpio sin romper integridad de datos.

**Why this priority**: Es una regla crítica de negocio para proteger consistencia entre departamentos y empleados.

**Independent Test**: Puede probarse de forma independiente intentando eliminar un departamento sin empleados asociados (éxito) y otro con empleados asociados (rechazo).

**Acceptance Scenarios**:

1. **Given** que un departamento no tiene empleados asociados, **When** se elimina ese departamento, **Then** el sistema elimina el registro correctamente.
2. **Given** que un departamento tiene uno o más empleados asociados, **When** se intenta eliminar ese departamento, **Then** el sistema rechaza la operación informando que existen empleados relacionados.

---

### Edge Cases

- ¿Qué ocurre cuando se intenta crear o actualizar un departamento con nombre vacío o solo espacios? → La solicitud se rechaza por validación.
- ¿Qué ocurre cuando la clave del departamento tiene variaciones de mayúsculas/minúsculas para un valor existente (por ejemplo `fin` y `FIN`)? → Se normaliza a MAYUSCULAS y se rechaza como duplicado lógico.
- ¿Qué ocurre cuando se intenta eliminar un departamento inexistente? → El sistema responde con no encontrado.
- ¿Qué ocurre cuando se intenta reasignar empleados a un departamento inexistente desde operaciones de empleados? → El sistema rechaza la operación por referencia inválida.
- ¿Qué código HTTP se devuelve al intentar eliminar un departamento con empleados asociados? → `409 Conflict`.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST permitir crear un departamento con `clave` y `nombre`.
- **FR-002**: El sistema MUST garantizar que la `clave` de departamento sea única y rechazar duplicados.
- **FR-002A**: El sistema MUST normalizar la `clave` a MAYUSCULAS antes de persistir y aplicar unicidad case-insensitive (`fin` y `FIN` se consideran la misma clave).
- **FR-003**: El sistema MUST validar que el `nombre` del departamento no sea vacío.
- **FR-004**: El sistema MUST permitir consultar la lista de departamentos.
- **FR-005**: El sistema MUST permitir consultar un departamento por su identificador.
- **FR-006**: El sistema MUST permitir actualizar `clave` y/o `nombre` de un departamento existente respetando validaciones de unicidad y nombre no vacío.
- **FR-007**: El sistema MUST permitir eliminar un departamento solo cuando no existan empleados asociados.
- **FR-008**: El sistema MUST rechazar la eliminación de un departamento con empleados asociados.
- **FR-009**: El sistema MUST actualizar el modelo de empleado para que cada empleado pertenezca a un único departamento.
- **FR-010**: El sistema MUST reflejar la relación uno-a-muchos entre departamento y empleados en operaciones de lectura y escritura.
- **FR-011**: El sistema MUST devolver respuestas de error claras para validación, duplicidad, no encontrado y conflicto por asociaciones.
- **FR-011A**: El sistema MUST mapear errores de forma consistente: `400 Bad Request` para validación, `404 Not Found` para recursos inexistentes, y `409 Conflict` para duplicidad de clave o conflictos de integridad (incluyendo eliminación con empleados asociados).
- **FR-012**: El sistema MUST crear un departamento por defecto con clave `SIN_ASIGNAR` durante la migración de datos y asociar a este departamento todos los empleados existentes antes de exigir la referencia obligatoria.
- **FR-013**: El sistema MUST aplicar control de acceso por rol en departamentos: operaciones de escritura (`POST`, `PUT`, `DELETE`) solo para `ADMIN`; operaciones de lectura (`GET`) para `ADMIN` y `LECTOR`.

### Constitution Alignment Requirements *(mandatory)*

- **CA-001**: Backend implementation MUST target Spring Boot 3.x with Java 17.
- **CA-002**: API security MUST define HTTP Basic authentication behavior,
  credential source, initial credentials (`admin` / `admin123`), and `401 Unauthorized`
  behavior for missing/invalid credentials.
- **CA-002A**: La configuración de seguridad para endpoints de departamentos MUST reflejar permisos por método HTTP: `GET` (`ADMIN`,`LECTOR`) y `POST/PUT/DELETE` (`ADMIN`), manteniendo `401/403` según corresponda.
- **CA-002B**: El contrato API MUST documentar explícitamente respuestas `400/404/409` para endpoints de departamentos según reglas de validación e integridad.
- **CA-003**: Persistent data changes MUST describe PostgreSQL schema impact and
  migration strategy.
- **CA-003A**: La estrategia de migración MUST incluir creación idempotente del departamento `SIN_ASIGNAR` y backfill de empleados existentes para mantener integridad referencial al introducir `departamento_id` obligatorio.
- **CA-004**: Local execution requirements MUST define Docker (and when needed,
  Docker Compose) services and environment variables.
- **CA-005**: API changes MUST include OpenAPI/Swagger documentation updates.
- **CA-006**: API routes MUST be versioned and exposed under `/api/v1` only.

### Key Entities *(include if feature involves data)*

- **Departamento**: Unidad organizacional identificada por `id`, con `clave` única y `nombre`. Representa el catálogo de áreas de la organización.
- **Empleado**: Entidad existente que incorpora referencia obligatoria a un único `Departamento` para indicar pertenencia organizacional.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de intentos de crear departamentos con clave duplicada son rechazados con error de conflicto.
- **SC-002**: El 100% de intentos de crear o actualizar departamentos con nombre vacío son rechazados por validación.
- **SC-003**: El 100% de eliminaciones de departamentos con empleados asociados son rechazadas y no alteran datos.
- **SC-004**: Usuarios administradores pueden completar el flujo de alta, consulta, actualización y eliminación (sin empleados asociados) en menos de 3 minutos por caso.
- **SC-005**: El 95% de consultas de departamentos (lista y por identificador) responde en menos de 1 segundo bajo carga operativa normal.
- **SC-006**: La documentación API de departamentos queda disponible y consistente con los comportamientos de éxito y error definidos.

## Assumptions

1. La gestión de departamentos será utilizada por usuarios administradores autenticados.
2. La clave de departamento será tratada con una política uniforme para evitar duplicados lógicos.
2.1 La política uniforme de clave consiste en normalizar a MAYUSCULAS y validar unicidad sin distinción de mayúsculas/minúsculas.
3. La referencia de departamento en empleados aplicará a empleados existentes mediante estrategia de migración controlada usando el departamento `SIN_ASIGNAR`.
4. No se incluye en este alcance una jerarquía de departamentos (padre-hijo).

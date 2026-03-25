# Research: CRUD de Departamentos

## Decision 1: Estrategia de unicidad de clave
- Decision: Normalizar `clave` a MAYUSCULAS y validar unicidad case-insensitive.
- Rationale: Evita duplicados logicos (`fin`/`FIN`) y simplifica reglas de negocio y consultas.
- Alternatives considered: Case-sensitive estricto; comparacion case-insensitive sin normalizar persistencia.

## Decision 2: Modelo de relacion con empleados
- Decision: Cada empleado pertenece a un unico departamento mediante FK obligatoria `departamento_id`; un departamento puede tener multiples empleados.
- Rationale: Cumple FR-009/FR-010 y mantiene integridad referencial a nivel de BD.
- Alternatives considered: Relacion opcional; tabla intermedia N:N.

## Decision 3: Migracion de empleados existentes
- Decision: Crear departamento idempotente `SIN_ASIGNAR` y ejecutar backfill de empleados existentes antes de imponer obligatoriedad de FK.
- Rationale: Evita bloqueo de despliegue y mantiene consistencia de datos durante la transicion.
- Alternatives considered: Bloquear despliegue hasta asignacion manual; permitir FK nullable indefinidamente.

## Decision 4: Politica de eliminacion
- Decision: Rechazar eliminacion de departamento cuando tenga empleados asociados, devolviendo `409 Conflict`.
- Rationale: Preserva integridad de negocio y evita empleados huerfanos.
- Alternatives considered: Eliminacion en cascada; reasignacion automatica silenciosa a `SIN_ASIGNAR`.

## Decision 5: Politica de autorizacion
- Decision: `GET` de departamentos para `ADMIN` y `LECTOR`; `POST/PUT/DELETE` solo para `ADMIN`.
- Rationale: Mantiene consistencia con principio de minimo privilegio y patrones del proyecto.
- Alternatives considered: CRUD completo solo ADMIN; rol dedicado nuevo.

## Decision 6: Mapeo de errores HTTP
- Decision: `400` validacion, `404` no encontrado, `409` duplicidad o conflicto de integridad.
- Rationale: Contrato claro, testeable y alineado con expectativas REST del proyecto.
- Alternatives considered: `400` para todo; `422` para errores de negocio.

## Decision 7: Superficie API
- Decision: Exponer endpoints versionados bajo `/api/v1/departamentos` para CRUD completo.
- Rationale: Cumple CA-006 y mantiene consistencia con recursos existentes.
- Alternatives considered: Rutas no versionadas; nombres de ruta mixtos.

## Decision 8: Documentacion y validacion operativa
- Decision: Actualizar OpenAPI con esquemas de request/response y escenarios `400/404/409`; validar localmente con Docker Compose y curl.
- Rationale: Cumple CA-005 y garantiza verificabilidad funcional antes de implementacion completa.
- Alternatives considered: Documentacion solo en codigo; validacion manual sin contrato.

# Tasks: CRUD de Empleados

**Input**: Design documents from `/specs/001-crud-empleados/`
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/openapi.yaml, quickstart.md

**Tests**: No se incluyen tareas de pruebas explícitas porque la especificación no exige enfoque TDD.

**Organization**: Tasks are grouped by user story so each story is independently implementable and testable.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Alinear estructura base del backend y configuración inicial para API versionada.

- [X] T001 Verificar dependencias Spring Boot y Java 17 en DSW02-Practica01/pom.xml
- [X] T002 Definir estructura de paquetes api/application/domain/infrastructure/security en DSW02-Practica01/src/main/java/com/example/empleados/
- [X] T003 [P] Ajustar configuración base de datasource/flyway/springdoc en DSW02-Practica01/src/main/resources/application.yml
- [X] T004 [P] Verificar runtime de contenedores backend+postgres en DSW02-Practica01/docker-compose.yml

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura común obligatoria antes de historias de usuario.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete.

- [X] T005 Configurar migración de tabla empleados y secuencia en DSW02-Practica01/src/main/resources/db/migration/V1__create_empleados_table.sql
- [X] T006 Implementar modelo de identidad compuesta de empleado en DSW02-Practica01/src/main/java/com/example/empleados/domain/EmpleadoId.java
- [X] T007 Implementar entidad Empleado con soft delete en DSW02-Practica01/src/main/java/com/example/empleados/domain/Empleado.java
- [X] T008 Implementar repositorio de empleados activos en DSW02-Practica01/src/main/java/com/example/empleados/infrastructure/EmpleadoRepository.java
- [X] T009 Implementar excepción de no encontrado en DSW02-Practica01/src/main/java/com/example/empleados/application/exception/ResourceNotFoundException.java
- [X] T010 [P] Implementar excepción de conflicto en DSW02-Practica01/src/main/java/com/example/empleados/application/exception/ConflictException.java
- [X] T011 [P] Implementar excepción de clave inválida en DSW02-Practica01/src/main/java/com/example/empleados/application/exception/InvalidClaveException.java
- [X] T012 Implementar manejo global de errores HTTP 400/401/404/409 en DSW02-Practica01/src/main/java/com/example/empleados/api/ApiExceptionHandler.java
- [X] T013 Implementar configuración de seguridad HTTP Basic con credenciales iniciales `admin/admin123` cargadas desde variables de entorno/configuración (sin hardcode) en DSW02-Practica01/src/main/java/com/example/empleados/security/SecurityConfig.java
- [X] T014 Configurar reglas de autorización RBAC (`ADMIN`/`LECTOR`) para operaciones de negocio en DSW02-Practica01/src/main/java/com/example/empleados/security/SecurityConfig.java
- [X] T015 Garantizar respuesta `401 Unauthorized` para falta/invalidación de credenciales en todos los endpoints expuestos en DSW02-Practica01/src/main/java/com/example/empleados/security/SecurityConfig.java
- [X] T016 Configurar OpenAPI para `basicAuth` y API versionada en DSW02-Practica01/src/main/java/com/example/empleados/api/OpenApiConfig.java
- [X] T039 Verificar extensibilidad de versionado API (estructura de router versionado, separación de controladores por versión y compatibilidad hacia atrás de contratos v1) en DSW02-Practica01/src/main/java/com/example/empleados/api/ y specs/001-crud-empleados/contracts/openapi.yaml
- [X] T040 Verificar comportamiento `404 Not Found` para cualquier endpoint fuera del prefijo `/api/v1` en DSW02-Practica01/src/main/java/com/example/empleados/security/SecurityConfig.java y DSW02-Practica01/src/main/java/com/example/empleados/api/ApiExceptionHandler.java

**Checkpoint**: Base técnica lista para implementar historias por prioridad.

---

## Phase 3: User Story 1 - Registrar empleado (Priority: P1) 🎯 MVP

**Goal**: Registrar empleados con clave generada automáticamente bajo rutas versionadas y autenticación obligatoria.

**Independent Test**: Con usuario `admin`, crear empleado vía `POST /api/v1/empleados` y validar `201` con clave `EMP-<autonumérico>`; sin credenciales o inválidas debe responder `401`.

### Implementation for User Story 1

- [X] T017 [US1] Crear DTO de alta sin `clave` en DSW02-Practica01/src/main/java/com/example/empleados/api/dto/EmpleadoCreateRequest.java
- [X] T018 [P] [US1] Crear DTO de respuesta de empleado en DSW02-Practica01/src/main/java/com/example/empleados/api/dto/EmpleadoResponse.java
- [X] T019 [US1] Implementar servicio de alta con generación de `EMP-<autonumérico>` en DSW02-Practica01/src/main/java/com/example/empleados/application/EmpleadoService.java
- [X] T020 [US1] Implementar endpoint `POST /api/v1/empleados` en DSW02-Practica01/src/main/java/com/example/empleados/api/EmpleadoController.java
- [X] T021 [US1] Asegurar mapeo de errores de validación/duplicidad en alta en DSW02-Practica01/src/main/java/com/example/empleados/api/ApiExceptionHandler.java

**Checkpoint**: Alta funcional de empleados en API versionada.

---

## Phase 4: User Story 2 - Consultar empleados (Priority: P2)

**Goal**: Consultar catálogo activo con paginación y detalle por clave activa en `/api/v1`.

**Independent Test**: Con usuario `lector`, consultar `GET /api/v1/empleados?page=1&limit=10` y `GET /api/v1/empleados/{clave}`; validar estructura `{page, limit, total, data}` y comportamiento de paginación.

### Implementation for User Story 2

- [X] T022 [P] [US2] Crear DTO de respuesta paginada de empleados en DSW02-Practica01/src/main/java/com/example/empleados/api/dto/PaginatedEmpleadoResponse.java
- [X] T023 [P] [US2] Implementar validación de parámetros `page`/`limit` con `limit<=100` en DSW02-Practica01/src/main/java/com/example/empleados/api/EmpleadoController.java
- [X] T024 [US2] Implementar lógica de listado paginado de activos en DSW02-Practica01/src/main/java/com/example/empleados/application/EmpleadoService.java
- [X] T025 [US2] Implementar comportamiento `200` con `data: []` cuando `page` no tiene resultados en DSW02-Practica01/src/main/java/com/example/empleados/application/EmpleadoService.java
- [X] T026 [US2] Implementar endpoint `GET /api/v1/empleados` en DSW02-Practica01/src/main/java/com/example/empleados/api/EmpleadoController.java
- [X] T027 [US2] Implementar endpoint `GET /api/v1/empleados/{clave}` en DSW02-Practica01/src/main/java/com/example/empleados/api/EmpleadoController.java
- [X] T028 [US2] Ajustar manejo de errores de paginación inválida (`400`) en DSW02-Practica01/src/main/java/com/example/empleados/api/ApiExceptionHandler.java

**Checkpoint**: Consulta versionada y paginada funcional.

---

## Phase 5: User Story 3 - Actualizar y eliminar empleados (Priority: P3)

**Goal**: Actualizar y eliminar lógicamente empleados activos en rutas versionadas.

**Independent Test**: Con usuario `admin`, actualizar y eliminar por `clave` en `/api/v1`; validar que inactivos no son actualizables y que eliminación repetida responde conflicto.

### Implementation for User Story 3

- [X] T029 [P] [US3] Crear DTO de actualización en DSW02-Practica01/src/main/java/com/example/empleados/api/dto/EmpleadoUpdateRequest.java
- [X] T030 [US3] Implementar lógica de actualización para empleados activos en DSW02-Practica01/src/main/java/com/example/empleados/application/EmpleadoService.java
- [X] T031 [US3] Implementar lógica de baja lógica con validación de ya inactivo en DSW02-Practica01/src/main/java/com/example/empleados/application/EmpleadoService.java
- [X] T032 [US3] Implementar endpoint `PUT /api/v1/empleados/{clave}` en DSW02-Practica01/src/main/java/com/example/empleados/api/EmpleadoController.java
- [X] T033 [US3] Implementar endpoint `DELETE /api/v1/empleados/{clave}` en DSW02-Practica01/src/main/java/com/example/empleados/api/EmpleadoController.java
- [X] T034 [P] [US3] Ajustar respuesta de conflicto para eliminación de inactivo en DSW02-Practica01/src/main/java/com/example/empleados/api/ApiExceptionHandler.java

**Checkpoint**: Ciclo CRUD completo en `/api/v1`.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre técnico y consistencia integral entre implementación y documentación.

- [X] T035 [P] Sincronizar anotaciones Swagger con rutas `/api/v1` y auth en DSW02-Practica01/src/main/java/com/example/empleados/api/EmpleadoController.java
- [X] T036 [P] Verificar esquema `basicAuth` y respuestas en DSW02-Practica01/src/main/java/com/example/empleados/api/OpenApiConfig.java
- [X] T037 Actualizar guía operativa y ejemplos de consumo en specs/001-crud-empleados/quickstart.md
- [X] T038 Verificar consistencia final del contrato en specs/001-crud-empleados/contracts/openapi.yaml

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: Sin dependencias.
- **Phase 2 (Foundational)**: Depende de Phase 1 y bloquea historias.
- **Phase 3 (US1)**: Depende de Phase 2.
- **Phase 4 (US2)**: Depende de Phase 2 y reutiliza base de US1.
- **Phase 5 (US3)**: Depende de Phase 2 y reutiliza entidades/servicios previos.
- **Phase 6 (Polish)**: Depende de historias completadas.

### User Story Dependency Graph

- **US1 (P1)** → MVP funcional.
- **US2 (P2)** → puede iniciar tras Foundational; recomendado tras US1 por reutilización de DTOs/servicio.
- **US3 (P3)** → puede iniciar tras Foundational; recomendado tras US2 para consistencia de reglas activas/inactivas.

### Within Each User Story

- DTOs y validaciones primero.
- Lógica de servicio después.
- Endpoints y manejo de errores al final.

---

## Parallel Opportunities

- **Phase 1**: T003 y T004.
- **Phase 2**: T010 y T011.
- **US1**: T017 y T018.
- **US2**: T022 y T023.
- **US3**: T029 y T034.
- **Polish**: T035 y T036.

---

## Parallel Example: User Story 1

```bash
Task: "T017 [US1] Crear DTO de alta en DSW02-Practica01/src/main/java/com/example/empleados/api/dto/EmpleadoCreateRequest.java"
Task: "T018 [US1] Crear DTO de respuesta en DSW02-Practica01/src/main/java/com/example/empleados/api/dto/EmpleadoResponse.java"
```

## Parallel Example: User Story 2

```bash
Task: "T022 [US2] Crear DTO paginado en DSW02-Practica01/src/main/java/com/example/empleados/api/dto/PaginatedEmpleadoResponse.java"
Task: "T023 [US2] Implementar validación page/limit en DSW02-Practica01/src/main/java/com/example/empleados/api/EmpleadoController.java"
```

## Parallel Example: User Story 3

```bash
Task: "T029 [US3] Crear DTO de actualización en DSW02-Practica01/src/main/java/com/example/empleados/api/dto/EmpleadoUpdateRequest.java"
Task: "T034 [US3] Ajustar conflicto de eliminación inactiva en DSW02-Practica01/src/main/java/com/example/empleados/api/ApiExceptionHandler.java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Setup.
2. Completar Foundational.
3. Completar US1.
4. Validar alta versionada con autenticación obligatoria.

### Incremental Delivery

1. Setup + Foundational.
2. Entregar US1 (alta en `/api/v1`).
3. Entregar US2 (consulta paginada).
4. Entregar US3 (actualización + baja lógica).
5. Cerrar con Polish.

### Suggested MVP Scope

- **MVP recomendado**: Phase 1 + Phase 2 + Phase 3 (US1).

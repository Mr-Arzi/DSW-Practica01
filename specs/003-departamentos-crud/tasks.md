# Tasks: CRUD de Departamentos

**Input**: Design documents from `/specs/003-departamentos-crud/`
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/, quickstart.md

---

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to
- Tests are not included because the specification did not explicitly request TDD/test-first tasks.

---

## Phase 1: Setup

**Purpose**: Verify baseline environment and current project readiness.

- [X] T001 Verify current branch and feature assets exist for `003-departamentos-crud` in `specs/003-departamentos-crud/`
- [X] T002 [P] Verify Docker runtime starts correctly from `DSW02-Practica01/docker-compose.yml`
- [X] T003 [P] Verify dependencies for JPA, validation, security, Flyway, and OpenAPI are present in `DSW02-Practica01/pom.xml`

**Checkpoint**: Baseline runtime and dependencies confirmed.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Data model, persistence, employee relation, and security foundation required by all stories.

**CRITICAL**: No user story implementation should start before this phase is complete.

- [X] T004 Create Flyway migration `V3__create_departamentos_and_link_empleados.sql` in `DSW02-Practica01/src/main/resources/db/migration/` to create `departamentos` table, insert idempotent `SIN_ASIGNAR`, add `empleados.departamento_id`, backfill existing employees, and enforce FK + `NOT NULL`
- [X] T005 Create `Departamento` JPA entity in `DSW02-Practica01/src/main/java/com/example/empleados/domain/Departamento.java` with fields `id`, `clave`, `nombre`
- [X] T006 [P] Create `DepartamentoRepository` in `DSW02-Practica01/src/main/java/com/example/empleados/infrastructure/DepartamentoRepository.java` with lookups for normalized unique `clave`
- [X] T007 Update `Empleado` entity in `DSW02-Practica01/src/main/java/com/example/empleados/domain/Empleado.java` to include mandatory `@ManyToOne` reference to `Departamento`
- [X] T008 [P] Update `EmpleadoRepository` in `DSW02-Practica01/src/main/java/com/example/empleados/infrastructure/EmpleadoRepository.java` with `countByDepartamentoId(Long departamentoId)`
- [X] T009 Update `EmpleadoCreateRequest` in `DSW02-Practica01/src/main/java/com/example/empleados/api/dto/EmpleadoCreateRequest.java` to require `departamentoId`
- [X] T010 [P] Update `EmpleadoUpdateRequest` in `DSW02-Practica01/src/main/java/com/example/empleados/api/dto/EmpleadoUpdateRequest.java` to include `departamentoId`
- [X] T011 [P] Update `EmpleadoResponse` in `DSW02-Practica01/src/main/java/com/example/empleados/api/dto/EmpleadoResponse.java` to expose department reference fields
- [X] T012 Update `EmpleadoService` in `DSW02-Practica01/src/main/java/com/example/empleados/application/EmpleadoService.java` to resolve and persist `departamentoId` on create/update
- [X] T013 Update security rules in `DSW02-Practica01/src/main/java/com/example/empleados/security/SecurityConfig.java` to allow `GET /api/v1/departamentos/**` for `ADMIN,LECTOR` and `POST/PUT/DELETE` only for `ADMIN`

**Checkpoint**: Department schema, employee linkage, and role security are in place.

---

## Phase 3: User Story 1 - Crear Departamento (Priority: P1)

**Goal**: Admin can create a department with unique normalized key and non-empty name.

**Independent Test**: `POST /api/v1/departamentos` with valid payload returns `201`; duplicate key returns `409`.

- [X] T014 [P] [US1] Create `DepartamentoCreateRequest` DTO in `DSW02-Practica01/src/main/java/com/example/empleados/api/dto/DepartamentoCreateRequest.java` with explicit validation for non-empty `nombre` (`@NotBlank`) and non-empty `clave`
- [X] T015 [P] [US1] Create `DepartamentoResponse` DTO in `DSW02-Practica01/src/main/java/com/example/empleados/api/dto/DepartamentoResponse.java`
- [X] T016 [US1] Implement `create(DepartamentoCreateRequest)` in `DSW02-Practica01/src/main/java/com/example/empleados/application/DepartamentoService.java` with uppercase `clave` normalization, duplicate detection, and defensive trim/blank validation for `nombre`
- [X] T017 [US1] Create `DepartamentoController` in `DSW02-Practica01/src/main/java/com/example/empleados/api/DepartamentoController.java` with `POST /api/v1/departamentos`
- [X] T018 [US1] Return `409` on duplicate `clave` by handling repository conflict in `DSW02-Practica01/src/main/java/com/example/empleados/api/ApiExceptionHandler.java`

**Checkpoint**: Department creation flow is independently functional.

---

## Phase 4: User Story 2 - Consultar Departamentos (Priority: P1)

**Goal**: Admin/Lector can list departments and fetch by id.

**Independent Test**: `GET /api/v1/departamentos` returns `200`; `GET /api/v1/departamentos/{id}` returns `200` or `404`.

- [X] T019 [US2] Implement `listAll()` in `DSW02-Practica01/src/main/java/com/example/empleados/application/DepartamentoService.java`
- [X] T020 [US2] Implement `findById(Long id)` in `DSW02-Practica01/src/main/java/com/example/empleados/application/DepartamentoService.java` with not-found handling
- [X] T021 [US2] Add `GET /api/v1/departamentos` and `GET /api/v1/departamentos/{id}` in `DSW02-Practica01/src/main/java/com/example/empleados/api/DepartamentoController.java`
- [X] T022 [US2] Map missing department to `404` in `DSW02-Practica01/src/main/java/com/example/empleados/api/ApiExceptionHandler.java`

**Checkpoint**: Department read operations are independently functional.

---

## Phase 5: User Story 4 - Eliminar Departamento con Restriccion de Integridad (Priority: P1)

**Goal**: Admin can delete only departments without associated employees.

**Independent Test**: `DELETE` without associations returns `204`; with associations returns `409`.

- [X] T023 [US4] Implement `deleteById(Long id)` in `DSW02-Practica01/src/main/java/com/example/empleados/application/DepartamentoService.java` using `EmpleadoRepository.countByDepartamentoId`
- [X] T024 [US4] Add `DELETE /api/v1/departamentos/{id}` in `DSW02-Practica01/src/main/java/com/example/empleados/api/DepartamentoController.java`
- [X] T025 [US4] Map delete conflict with associated employees to `409` in `DSW02-Practica01/src/main/java/com/example/empleados/api/ApiExceptionHandler.java`

**Checkpoint**: Deletion integrity rule is independently functional.

---

## Phase 6: User Story 3 - Actualizar Departamento (Priority: P2)

**Goal**: Admin can update department key/name with normalization and uniqueness rules.

**Independent Test**: `PUT /api/v1/departamentos/{id}` returns `200`; duplicate key returns `409`; invalid input returns `400`.

- [X] T026 [P] [US3] Create `DepartamentoUpdateRequest` DTO in `DSW02-Practica01/src/main/java/com/example/empleados/api/dto/DepartamentoUpdateRequest.java` with explicit validation for non-empty `nombre` (`@NotBlank`) and non-empty `clave`
- [X] T027 [US3] Implement `update(Long id, DepartamentoUpdateRequest)` in `DSW02-Practica01/src/main/java/com/example/empleados/application/DepartamentoService.java` including defensive trim/blank validation for `nombre`, uppercase normalization for `clave`, and uniqueness checks
- [X] T028 [US3] Add `PUT /api/v1/departamentos/{id}` in `DSW02-Practica01/src/main/java/com/example/empleados/api/DepartamentoController.java`
- [X] T029 [US3] Ensure duplicate key and missing resource update paths return `409/404` via `DSW02-Practica01/src/main/java/com/example/empleados/api/ApiExceptionHandler.java`

**Checkpoint**: Department update flow is independently functional.

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Align contract/docs, validate runtime behavior, and finalize feature quality.

- [X] T030 [P] Add OpenAPI annotations (`@Operation`, `@ApiResponse`, `@Tag`, security) in `DSW02-Practica01/src/main/java/com/example/empleados/api/DepartamentoController.java` for `200/201/204/400/404/409/401/403`
- [X] T031 Update feature contract in `specs/003-departamentos-crud/contracts/openapi.yaml` to match implemented DTOs and status codes exactly
- [X] T032 [P] Update `specs/003-departamentos-crud/quickstart.md` command payloads/ids if implementation details differ from assumptions
- [X] T033 Verify Swagger output at `/api/v1/v3/api-docs` includes `/api/v1/departamentos` endpoints with expected role/access semantics
- [X] T034 Run Maven validation in `DSW02-Practica01/` with `mvn test` and fix compile/runtime regressions
- [X] T035 Rebuild and validate end-to-end with Docker in `DSW02-Practica01/docker-compose.yml` covering create/list/get/update/delete-conflict paths

---

## Dependencies & Execution Order

### Phase Dependencies

- Setup (Phase 1): no dependencies.
- Foundational (Phase 2): depends on Setup and blocks all user stories.
- User Story phases (3-6): depend on Foundational.
- Polish (Phase 7): depends on completion of all targeted user stories.

### User Story Dependencies

- **US1 (P1)**: Depends on Phase 2; independent from US2/US3/US4.
- **US2 (P1)**: Depends on Phase 2; independent from US1/US3/US4.
- **US4 (P1)**: Depends on Phase 2 and repository count method from foundational tasks.
- **US3 (P2)**: Depends on Phase 2; can be implemented after any P1 story.

### Parallel Opportunities

- Phase 1: T002 and T003.
- Phase 2: T006, T008, T010, T011 in parallel after T004/T005 are started.
- US1: T014 and T015 in parallel.
- US3: T026 can run in parallel with service preparation.
- Phase 7: T030 and T032 can run in parallel.

---

## Parallel Example: Foundational + US1

```bash
# Foundational parallel work:
T006: DepartamentoRepository
T008: countByDepartamentoId in EmpleadoRepository
T010: EmpleadoUpdateRequest departamentoId
T011: EmpleadoResponse with department fields

# US1 parallel work:
T014: DepartamentoCreateRequest
T015: DepartamentoResponse
```

---

## Implementation Strategy

### MVP First

1. Complete Phase 1 + Phase 2.
2. Deliver US1 (create department).
3. Validate with curl and Swagger before expanding scope.

### Incremental Delivery

1. Add US2 (read operations) after US1.
2. Add US4 (safe delete with integrity guard).
3. Add US3 (update).
4. Finish with Phase 7 polish and full Docker validation.

### Team Parallelization

1. One developer handles migration + domain model (T004-T007).
2. Another handles employee DTO/service adaptation (T009-T012).
3. After foundation, split by user stories (US1/US2/US4/US3) in parallel.

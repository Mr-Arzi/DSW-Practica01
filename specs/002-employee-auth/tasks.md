# Tasks: Employee Authentication via Email & Password

**Input**: Design documents from `/specs/002-employee-auth/`
**Prerequisites**: plan.md ✓, spec.md ✓, research.md ✓, data-model.md ✓, contracts/ ✓

---

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to
- No test tasks generated (not explicitly requested in spec)

---

## Phase 1: Setup

**Purpose**: Verify existing project baseline is ready to receive auth feature changes.

- [X] T001 Verify Docker Compose stack builds and app starts cleanly in DSW02-Practica01/
- [X] T002 [P] Confirm `spring-boot-starter-security` and bcrypt support are already included in DSW02-Practica01/pom.xml

**Checkpoint**: Project baseline confirmed — foundational changes can begin.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Schema, entity, repository and security config changes that ALL user stories depend on.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete.

- [X] T003 Create Flyway migration `V2__add_email_password_hash_to_empleados.sql` in `DSW02-Practica01/src/main/resources/db/migration/` adding nullable `email` (varchar unique) and `password_hash` (varchar) columns to `empleado` table
- [X] T004 Update `Empleado` JPA entity in `DSW02-Practica01/src/main/java/com/example/empleados/domain/Empleado.java` to add `email` and `passwordHash` fields
- [X] T005 [P] Add `findByEmailAndActivo` lookup method to `DSW02-Practica01/src/main/java/com/example/empleados/infrastructure/EmpleadoRepository.java`
- [X] T006 Update `SecurityConfig` in `DSW02-Practica01/src/main/java/com/example/empleados/security/SecurityConfig.java` to permit `POST /api/v1/auth/login` without HTTP Basic authentication using `.securityMatcher` or `requestMatchers` permitAll
- [X] T007 [P] Confirm `PasswordEncoder` bcrypt bean is declared in `DSW02-Practica01/src/main/java/com/example/empleados/security/SecurityConfig.java` with work factor ≥12 (e.g., `new BCryptPasswordEncoder(12)`); Spring Security default is 10 — update to 12 if lower to satisfy SC-003/FR-006

**Checkpoint**: Schema migrated, entity updated, repo extended, security config permits login — user story phases can now proceed.

---

## Phase 3: User Story 4 — Password Security (Priority: P1) 🎯

**Goal**: Ensure passwords are stored as bcrypt hashes when new employees are created with credentials. This is prerequisite infrastructure for US1.

**Independent Test**: Inspect `empleado` table after creating an employee with email/password — `password_hash` column must contain a bcrypt-format string (`$2a$...`), never plaintext.

- [X] T008 [P] [US4] Add `email` and `password` optional fields to `EmpleadoCreateRequest` record in `DSW02-Practica01/src/main/java/com/example/empleados/api/dto/EmpleadoCreateRequest.java`
- [X] T009 [US4] Update `EmpleadoService.create()` in `DSW02-Practica01/src/main/java/com/example/empleados/application/EmpleadoService.java` to hash password with `PasswordEncoder` when `password` is present before persisting `passwordHash` on entity
- [X] T010 [US4] Update `EmpleadoResponse` DTO in `DSW02-Practica01/src/main/java/com/example/empleados/api/dto/EmpleadoResponse.java` to expose `email` field (never expose `passwordHash`)

**Checkpoint**: New employees can be created with email/password; DB stores only bcrypt hash. US4 independently testable via DB inspection.

---

## Phase 4: User Story 1 — Login with Valid Credentials (Priority: P1) 🎯 MVP

**Goal**: Employees with valid email/password receive `200 OK` with `{"message":"Authentication successful"}`.

**Independent Test**: `POST /api/v1/auth/login` with correct email and password returns `200 OK` and confirmation message.

- [X] T011 [P] [US1] Create `LoginRequest` DTO record in `DSW02-Practica01/src/main/java/com/example/empleados/api/dto/LoginRequest.java` with `@NotBlank @Email String email` and `@NotBlank String password`
- [X] T012 [P] [US1] Create `LoginSuccessResponse` DTO record in `DSW02-Practica01/src/main/java/com/example/empleados/api/dto/LoginSuccessResponse.java` with `String message`
- [X] T013 [US1] Implement `AuthService` in `DSW02-Practica01/src/main/java/com/example/empleados/application/AuthService.java` with `authenticate(LoginRequest)` method that looks up active employee by email, checks password with `PasswordEncoder.matches()`, and returns `LoginSuccessResponse` or throws exception
- [X] T014 [US1] Implement `AuthController` in `DSW02-Practica01/src/main/java/com/example/empleados/api/AuthController.java` with `@PostMapping("/api/v1/auth/login")`, `@Validated`, OpenAPI `@Operation`/`@ApiResponse` annotations and `security: []` annotation
- [X] T015 [US1] Annotate `AuthController` with `@Tag` and `@SecurityRequirement(name="")` (no auth) and add `@ApiResponse(responseCode="200")` in `DSW02-Practica01/src/main/java/com/example/empleados/api/AuthController.java`

**Checkpoint**: `POST /api/v1/auth/login` with valid credentials returns `200 OK`. US1 independently testable with curl.

---

## Phase 5: User Story 2 + User Story 3 — Login Failures (Priority: P2)

**Goal**: Requests with unknown email or wrong password both return `401 Unauthorized` with `{"error":"Invalid email or password"}` — without leaking which field is wrong.

**Independent Test**: `POST /api/v1/auth/login` with unknown email OR wrong password returns `401` and generic error message.

- [X] T016 [P] [US2] Create `InvalidCredentialsException` in `DSW02-Practica01/src/main/java/com/example/empleados/application/exception/InvalidCredentialsException.java` extending `RuntimeException`
- [X] T017 [US2] Update `AuthService.authenticate()` in `DSW02-Practica01/src/main/java/com/example/empleados/application/AuthService.java` to throw `InvalidCredentialsException` when email is not found in active employees (same exception as wrong password — no enumeration)
- [X] T018 [US3] Update `AuthService.authenticate()` in `DSW02-Practica01/src/main/java/com/example/empleados/application/AuthService.java` to throw `InvalidCredentialsException` when password does not match stored hash
- [X] T019 [P] [US2] Create `LoginErrorResponse` DTO record in `DSW02-Practica01/src/main/java/com/example/empleados/api/dto/LoginErrorResponse.java` with `String error`
- [X] T020 [US2] Update `ApiExceptionHandler` in `DSW02-Practica01/src/main/java/com/example/empleados/api/ApiExceptionHandler.java` to handle `InvalidCredentialsException` and return `ResponseEntity<LoginErrorResponse>` with HTTP `401` and body `{"error":"Invalid email or password"}`
- [X] T021 [P] [US3] Add `@ApiResponse(responseCode="401")` to `AuthController.login()` in `DSW02-Practica01/src/main/java/com/example/empleados/api/AuthController.java`

**Checkpoint**: Both invalid email and wrong password return `401` with generic message. US2+US3 independently testable.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Validate contract, security posture and end-to-end integration across all stories.

- [X] T022 [P] Add `@ApiResponse(responseCode="400")` for validation errors to `AuthController` in `DSW02-Practica01/src/main/java/com/example/empleados/api/AuthController.java`
- [X] T026 [FR-007] Audit `AuthService`, `AuthController`, and `ApiExceptionHandler` to confirm that `loginRequest.password` and any plaintext password value are **never** included in log statements (`log.debug/info/warn/error`), exception messages, or stack traces; suppress if found
- [ ] T027 [SC-004] Run basic concurrency smoke test on `POST /api/v1/auth/login` using `ab -n 200 -c 50 -p /tmp/login.json -T application/json http://localhost:8080/api/v1/auth/login`; confirm zero non-2xx/4xx errors and p95 < 500ms
- [X] T023 Verify `SecurityConfig` in `DSW02-Practica01/src/main/java/com/example/empleados/security/SecurityConfig.java` permits only `POST /api/v1/auth/login` as public; all other `/api/v1/**` endpoints still require HTTP Basic
- [X] T024 [P] Verify Swagger UI at `http://localhost:8080/api/v1/swagger-ui.html` shows `POST /api/v1/auth/login` with correct request/response schemas and no auth lock icon on that endpoint
- [X] T025 Rebuild Docker image and run end-to-end validation via commands in `specs/002-employee-auth/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — start immediately
- **Foundational (Phase 2)**: Depends on Phase 1 — **BLOCKS all user stories**
- **US4 (Phase 3)**: Depends on Phase 2 — bcrypt + entity required before login logic
- **US1 (Phase 4)**: Depends on Phase 3 — requires hashed passwords and entity fields
- **US2+US3 (Phase 5)**: Depends on Phase 4 — error branches of same service method
- **Polish (Phase 6)**: Depends on all prior phases

### User Story Dependencies

- **US4 (P1)**: Depends on Foundational — no story dependencies
- **US1 (P1)**: Depends on US4 (needs PasswordEncoder path and entity fields)
- **US2 (P2)**: Shares AuthService with US1 — add error branch after US1 service exists
- **US3 (P2)**: Shares AuthService with US1 — add error branch after US1 service exists

### Parallel Opportunities Per Phase

- **Phase 2**: T005, T007 can run in parallel with T003/T004
- **Phase 3**: T008 can run in parallel with T009
- **Phase 4**: T011, T012 can run in parallel before T013
- **Phase 5**: T016, T019 can run in parallel before T017/T018/T020

---

## Parallel Example: Phase 4 (US1)

```bash
# Run in parallel (different files, no deps):
T011: Create LoginRequest DTO
T012: Create LoginSuccessResponse DTO

# After T011 + T012:
T013: Implement AuthService.authenticate()

# After T013:
T014: Implement AuthController
T015: Add OpenAPI annotations to AuthController
```

---

## Implementation Strategy

### MVP First (US4 + US1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational — schema, entity, repo, security config
3. Complete Phase 3: US4 — bcrypt hashing on create
4. Complete Phase 4: US1 — login success path
5. **STOP and VALIDATE**: `curl -X POST http://localhost:8080/api/v1/auth/login -d '{"email":"...","password":"..."}'` → `200`

### Incremental Delivery

1. Phase 1 + 2 → Foundation ready
2. Phase 3 → Passwords stored safely (US4 ✓)
3. Phase 4 → Login success path works (US1 ✓ — MVP!)
4. Phase 5 → Failure paths covered (US2+US3 ✓)
5. Phase 6 → Polish, contract verified

---

## Notes

- `email` / `password_hash` columns in DB migration are nullable to avoid breaking existing rows — authentication flow only works for employees that have both populated (per spec Assumption 3)
- `passwordHash` must NEVER appear in any response DTO (`EmpleadoResponse` or similar)
- The `POST /api/v1/auth/login` endpoint is the ONLY public route exception on `/api/v1/**`
- `InvalidCredentialsException` MUST be thrown for both unknown-email and wrong-password paths (same exception type prevents credential enumeration)
- Commit after each phase checkpoint for clean rollback points

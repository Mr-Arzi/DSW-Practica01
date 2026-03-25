# Tasks: Frontend Angular con Pruebas E2E

**Input**: Design documents from `/specs/004-angular-frontend-e2e/`
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/, quickstart.md

---

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to
- Cypress tests are included because the feature specification explicitly requests end-to-end testing.

---

## Phase 1: Setup

**Purpose**: Inicializar workspace Angular y base de tooling para desarrollo y pruebas.

- [X] T001 Initialize Angular 18 workspace in `frontend/angular.json` and `frontend/package.json`
- [X] T002 [P] Configure strict TypeScript and environment files in `frontend/tsconfig.json` and `frontend/src/environments/environment.ts`
- [X] T003 [P] Add npm scripts for app and Cypress execution in `frontend/package.json`
- [X] T004 Create base app bootstrapping files in `frontend/src/main.ts` and `frontend/src/app/app.component.ts`

**Checkpoint**: Proyecto frontend ejecutable con estructura base.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura compartida obligatoria para autenticacion, rutas protegidas, API client y shell de navegacion.

**CRITICAL**: No user story implementation should start before this phase is complete.

- [X] T005 Implement authentication session store with `sessionStorage` in `frontend/src/app/core/auth/auth-session.service.ts`
- [X] T006 [P] Implement Basic Auth HTTP interceptor in `frontend/src/app/core/interceptors/basic-auth.interceptor.ts`
- [X] T007 [P] Implement auth guard for protected routes in `frontend/src/app/core/guards/auth.guard.ts`
- [X] T008 Configure global HTTP providers/interceptors in `frontend/src/app/app.config.ts`
- [X] T009 [P] Create shared API base service for `/api/v1` in `frontend/src/app/core/api/api-base.service.ts`
- [X] T010 Implement global unauthorized handling and redirect in `frontend/src/app/core/interceptors/api-error.interceptor.ts`
- [X] T011 Create role capability policy (`ADMIN`/`LECTOR`) in `frontend/src/app/core/auth/role-policy.service.ts`
- [X] T012 Create application shell with menu and logout action in `frontend/src/app/shared/layout/app-shell.component.ts`
- [X] T013 Define top-level app routes (`/login`, `/empleados`, `/departamentos`) in `frontend/src/app/app.routes.ts`



**Checkpoint**: Sesion, seguridad de rutas, interceptor Basic y shell de navegacion operativos.

---

## Phase 3: User Story 1 - Inicio de sesion y acceso protegido (Priority: P1)

**Goal**: Empleado se autentica con correo/contrasena, se redirige a empleados y se bloquean rutas protegidas sin sesion.

**Independent Test**: Login valido redirige a `/empleados`; login invalido muestra error y mantiene `/login`; acceso directo a rutas protegidas sin sesion redirige a `/login`.

- [X] T014 [P] [US1] Create login request/response models in `frontend/src/app/features/login/login.models.ts`
- [X] T015 [P] [US1] Implement auth API service against `POST /api/v1/auth/login` in `frontend/src/app/features/login/login-api.service.ts`
- [X] T016 [US1] Implement login page component and form validation in `frontend/src/app/features/login/login-page.component.ts`
- [X] T017 [US1] Implement login template and error UX in `frontend/src/app/features/login/login-page.component.html`
- [X] T018 [US1] Wire successful login persistence and redirect to `/empleados` in `frontend/src/app/features/login/login.facade.ts`
- [X] T019 [US1] Add route guard redirect behavior for unauthenticated access in `frontend/src/app/app.routes.ts`



**Checkpoint**: Flujo de login y proteccion de rutas funciona de forma independiente.

---

## Phase 4: User Story 2 - Navegacion entre modulos autenticados (Priority: P1)

**Goal**: Usuario autenticado navega entre empleados y departamentos sin reautenticarse y puede cerrar sesion.

**Independent Test**: Tras login, navegar por menu entre `/empleados` y `/departamentos` mantiene sesion; logout limpia sesion y redirige a `/login`.

- [X] T020 [P] [US2] Implement shell layout template with module navigation in `frontend/src/app/shared/layout/app-shell.component.html`
- [X] T021 [US2] Implement shell logic for active route and logout in `frontend/src/app/shared/layout/app-shell.component.ts`
- [X] T022 [US2] Implement logout use case (clear `sessionStorage` + redirect) in `frontend/src/app/core/auth/logout.service.ts`
- [X] T023 [US2] Integrate shell layout with protected feature routes in `frontend/src/app/app.routes.ts`

**Checkpoint**: Navegacion autenticada y logout son funcionales de forma independiente.

---

## Phase 5: User Story 3 - Gestion de empleados en frontend (Priority: P1)

**Goal**: Usuario autenticado puede listar, crear, editar y eliminar empleados segun permisos de rol.

**Independent Test**: Modulo `/empleados` permite listar y ejecutar CRUD para `ADMIN`; `LECTOR` ve solo lectura con acciones de escritura deshabilitadas/ocultas.

- [X] T024 [P] [US3] Create employee DTO/view models in `frontend/src/app/features/empleados/empleados.models.ts`
- [X] T025 [P] [US3] Implement employees API service (`GET/POST/PUT/DELETE /api/v1/empleados`) in `frontend/src/app/features/empleados/empleados-api.service.ts`
- [X] T026 [US3] Implement employees list page container in `frontend/src/app/features/empleados/empleados-page.component.ts`
- [X] T027 [US3] Implement employees table/list UI in `frontend/src/app/features/empleados/empleados-page.component.html`
- [X] T028 [US3] Implement employee create/edit form component in `frontend/src/app/features/empleados/empleado-form.component.ts`
- [X] T029 [US3] Implement employee delete action and refresh workflow in `frontend/src/app/features/empleados/empleados-actions.service.ts`
- [X] T030 [US3] Apply role-based UI restrictions (`ADMIN` CRUD, `LECTOR` read-only) in `frontend/src/app/features/empleados/empleados-page.component.ts`



**Checkpoint**: Gestion de empleados es funcional e independiente por rol.

---

## Phase 6: User Story 4 - Gestion de departamentos en frontend (Priority: P1)

**Goal**: Usuario autenticado puede listar, crear, editar y eliminar departamentos segun permisos de rol.

**Independent Test**: Modulo `/departamentos` permite listar y ejecutar CRUD para `ADMIN`; `LECTOR` ve solo lectura con acciones de escritura deshabilitadas/ocultas.

- [X] T031 [P] [US4] Create department DTO/view models in `frontend/src/app/features/departamentos/departamentos.models.ts`
- [X] T032 [P] [US4] Implement departments API service (`GET/POST/PUT/DELETE /api/v1/departamentos`) in `frontend/src/app/features/departamentos/departamentos-api.service.ts`
- [X] T033 [US4] Implement departments list page container in `frontend/src/app/features/departamentos/departamentos-page.component.ts`
- [X] T034 [US4] Implement departments table/list UI in `frontend/src/app/features/departamentos/departamentos-page.component.html`
- [X] T035 [US4] Implement department create/edit form component in `frontend/src/app/features/departamentos/departamento-form.component.ts`
- [X] T036 [US4] Implement department delete action and conflict feedback handling in `frontend/src/app/features/departamentos/departamentos-actions.service.ts`
- [X] T037 [US4] Apply role-based UI restrictions (`ADMIN` CRUD, `LECTOR` read-only) in `frontend/src/app/features/departamentos/departamentos-page.component.ts`



**Checkpoint**: Gestion de departamentos es funcional e independiente por rol.

---

## Phase 7: User Story 5 - Pruebas E2E con Cypress (Priority: P1)

**Goal**: Suite E2E valida flujos criticos de autenticacion, navegacion y operaciones principales en empleados/departamentos.

**Independent Test**: Ejecutar Cypress en limpio y obtener casos verdes para los 7 escenarios minimos requeridos.

- [X] T038 [P] [US5] Configure Cypress project and base settings in `frontend/cypress.config.ts`
- [X] T039 [P] [US5] Implement Cypress custom commands for login and data setup in `frontend/cypress/support/commands.ts`
- [X] T040 [US5] Implement E2E spec for successful/failed login in `frontend/cypress/e2e/auth/login.cy.ts`
- [X] T041 [US5] Implement E2E spec for module navigation in `frontend/cypress/e2e/navigation/modules.cy.ts`
- [X] T042 [US5] Implement E2E spec for employee list/create flows in `frontend/cypress/e2e/empleados/empleados.cy.ts`
- [X] T043 [US5] Implement E2E spec for department list/create flows in `frontend/cypress/e2e/departamentos/departamentos.cy.ts`


**Checkpoint**: Cobertura E2E minima requerida ejecuta y valida los flujos principales.

---

## Phase 8: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad, documentacion y verificacion integral.

- [X] T045 [P] Update frontend runbook and scripts documentation in `frontend/README.md`
- [X] T046 Verify quickstart flow end-to-end in `specs/004-angular-frontend-e2e/quickstart.md`
- [X] T047 [P] Run Angular quality checks (`lint/test/build`) via `frontend/package.json` scripts
- [X] T048 Run Cypress headless suite and capture evidence in `frontend/cypress/results/`

- [X] T050 [P] Add explicit runtime verification of backend Docker services (app + db) in `specs/004-angular-frontend-e2e/quickstart.md` and execution notes in `frontend/README.md`
- [X] T051 [P] Add PostgreSQL dependency verification task (connectivity + expected data availability for frontend flows) in `specs/004-angular-frontend-e2e/quickstart.md`
- [X] T052 Add explicit non-JWT compliance check ensuring no Bearer token storage/usage in `frontend/src/app/core/interceptors/basic-auth.interceptor.ts` and `frontend/src/app/core/auth/auth-session.service.ts`
- [X] T053 [US3] Implement recoverable UI handling for API network and 5xx errors in `frontend/src/app/features/empleados/empleados-page.component.ts`
- [X] T054 [US4] Implement recoverable UI handling for API network and 5xx errors in `frontend/src/app/features/departamentos/departamentos-page.component.ts`
- [X] T055 [US5] Add Cypress assertions for deterministic data lifecycle (bootstrap + cleanup) in `frontend/cypress/support/e2e.ts` and related specs
- [X] T056 [P] Add performance validation task for protected-route navigation <=1s and initial list render <=2s, documenting measurement method in `specs/004-angular-frontend-e2e/quickstart.md`
- [X] T057 [US1] Add explicit validation of initial credentials (`admin`/`admin123`) and expected `401 Unauthorized` behavior in `frontend/cypress/e2e/auth/login.cy.ts` and `frontend/cypress/e2e/auth/unauthorized.cy.ts`


---

## Dependencies & Execution Order

### Phase Dependencies

- Setup (Phase 1): no dependencies.
- Foundational (Phase 2): depends on Setup and blocks all user stories.
- User Story phases (3-7): depend on Foundational.
- Polish (Phase 8): depends on completion of all targeted user stories.

### User Story Dependencies

- **US1 (P1)**: Depends on Phase 2; no dependency on other stories.
- **US2 (P1)**: Depends on US1 session/login behavior and Phase 2 shell foundations.
- **US3 (P1)**: Depends on US1 auth and US2 navigation.
- **US4 (P1)**: Depends on US1 auth and US2 navigation.
- **US5 (P1)**: Depends on US1-US4 implementations to validate full functional flows.

### Parallel Opportunities

- Phase 1: T002 and T003.
- Phase 2: T006, T007, T009.
- US1: T014 and T015.
- US3: T024 and T025.
- US4: T031 and T032.
- US5: T038 and T039.
- Phase 8: T045 and T047.

---

## Parallel Example: US3 + US4

```bash
# US3 parallel tasks:
T024: Empleado view models
T025: Empleados API service

# US4 parallel tasks:
T031: Departamento view models
T032: Departamentos API service
```

---

## Implementation Strategy

### MVP First

1. Complete Phase 1 + Phase 2.
2. Deliver US1 (login + protected routes).
3. Add US2 navigation and logout.
4. Validate end-to-end manually before expanding CRUD modules.

### Incremental Delivery

1. Add US3 (empleados) with role-aware UI.
2. Add US4 (departamentos) with role-aware UI.
3. Add US5 Cypress suite for required flows.
4. Finish with Phase 8 polish and quality execution.

### Team Parallelization

1. One developer handles auth/session/routing foundations (T005-T013).
2. Another developer implements empleados feature (T024-T030).
3. Another developer implements departamentos and Cypress (T031-T044).

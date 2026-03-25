# Implementation Plan: Frontend Angular con Pruebas E2E

**Branch**: `004-angular-frontend-e2e` | **Date**: 2026-03-19 | **Spec**: `/specs/004-angular-frontend-e2e/spec.md`
**Input**: Feature specification from `/specs/004-angular-frontend-e2e/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Construir un frontend web en Angular (TypeScript) para login, navegacion protegida, y gestion de empleados/departamentos consumiendo la API existente bajo `/api/v1` con HTTP Basic en cada solicitud. La sesion se mantiene en `sessionStorage`, se aplica comportamiento por rol (`ADMIN` CRUD, `LECTOR` solo lectura), y se valida el flujo principal con suite E2E en Cypress.

## Technical Context

**Language/Version**: TypeScript 5.x + Angular 18 (estable)  
**Primary Dependencies**: Angular Router, Angular HttpClient, RxJS, Cypress 13.x  
**Storage**: `sessionStorage` para estado de sesion frontend; backend mantiene PostgreSQL existente  
**Testing**: Cypress E2E + Angular test tooling (`ng test` opcional)  
**Target Platform**: Navegadores modernos (Chrome/Edge/Firefox), entorno local Linux
**Project Type**: Web application (frontend Angular + backend Spring Boot existente)  
**Performance Goals**: Navegacion entre rutas protegidas <= 1s en entorno local y render inicial de listados <= 2s con dataset operativo (trazado en FR-020 y FR-021 de `spec.md`)

 
**Constraints**: Sin JWT en esta fase; todas las llamadas a API protegida via HTTP Basic; consumo exclusivo de `/api/v1`; rol `LECTOR` sin acciones de escritura  
**Scale/Scope**: Frontend unico con tres rutas principales (`/login`, `/empleados`, `/departamentos`) y 7 escenarios E2E minimos

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Stack Gate: PASS - backend permanece en Spring Boot 3.x/Java 17 y frontend en Angular 18 + TypeScript.
- Frontend Gate: PASS - se define frontend web Angular estable y compatible.
- Security Gate: PASS - login, rutas protegidas y logout definidos con HTTP Basic.
- Auth Gate: PASS - llamadas protegidas incluyen credenciales y contemplan manejo de 401/403.
- Frontend Auth Gate: PASS - credenciales derivadas de login persistidas en `sessionStorage`.
- Versioning Gate: PASS - consumo API limitado a rutas bajo `/api/v1`.
- Data Gate: PASS - sin cambios de esquema; se reutiliza persistencia PostgreSQL existente.
- Runtime Gate: PASS - frontend local + backend Docker Compose documentado; tareas explicitas de    verificacion runtime y dependencia PostgreSQL incluidas en backlog (T050, T051).

- API Contract Gate: PASS - consumo y rutas documentadas en contrato de frontend.
- E2E Gate: PASS - Cypress cubrira autenticacion, empleados y departamentos.
- Separation Gate: PASS - capas frontend separadas por rutas, servicios e interceptor.

### Post-Design Constitution Re-Check

- Stack Gate: PASS
- Frontend Gate: PASS
- Security Gate: PASS
- Auth Gate: PASS
- Frontend Auth Gate: PASS
- Versioning Gate: PASS
- Data Gate: PASS
- Runtime Gate: PASS
- API Contract Gate: PASS
- E2E Gate: PASS
- Separation Gate: PASS

## Project Structure

### Documentation (this feature)

```text
specs/004-angular-frontend-e2e/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── frontend-routing-and-api.md
└── tasks.md
```

### Source Code (repository root)
```text
DSW02-Practica01/
├── src/main/java/com/example/empleados/
├── src/main/resources/
├── pom.xml
└── docker-compose.yml

frontend/
├── src/app/
│   ├── core/
│   │   ├── auth/
│   │   ├── guards/
│   │   └── interceptors/
│   ├── features/
│   │   ├── login/
│   │   ├── empleados/
│   │   └── departamentos/
│   └── shared/
├── cypress/
│   ├── e2e/
│   ├── fixtures/
│   └── support/
├── package.json
└── angular.json
```

**Structure Decision**: Se adopta estructura web app: backend existente en `DSW02-Practica01/` y nuevo frontend Angular aislado en `frontend/`, con Cypress integrado en el mismo proyecto frontend para pruebas e2e.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | N/A | N/A |

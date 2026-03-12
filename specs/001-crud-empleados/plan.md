# Implementation Plan: CRUD de Empleados

**Branch**: `001-crud-empleados` | **Date**: 2026-03-09 | **Spec**: `/specs/001-crud-empleados/spec.md`
**Input**: Feature specification from `/specs/001-crud-empleados/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar y mantener el CRUD de empleados versionado bajo `/api/v1`, con `clave`
autogenerada (`EMP-<autonumérico>`), baja lógica, validaciones de negocio y
paginación obligatoria de colecciones (`page`, `limit`, `total`, `data`). La
solución usa Spring Boot 3 + Java 17, PostgreSQL + Flyway, seguridad HTTP Basic
obligatoria en todos los endpoints expuestos (respuesta `401` sin credenciales
válidas), y respuesta `404` para cualquier endpoint fuera del prefijo versionado.
Las credenciales iniciales (`admin/admin123`) se gestionan por configuración
externa o variables de entorno, no hardcodeadas, manteniendo RBAC
(`ADMIN`/`LECTOR`), contrato OpenAPI/Swagger sincronizado y ejecución
reproducible con Docker Compose.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.x (Web, Validation, Security), Spring Data JPA, Flyway, springdoc-openapi  
**Storage**: PostgreSQL 16  
**Testing**: JUnit 5 + Spring Boot Test (`mvn test`)  
**Target Platform**: Linux server (contenedor Docker)  
**Project Type**: Web service (API REST backend)  
**Performance Goals**: Responder operaciones CRUD y listados paginados con comportamiento estable para catálogos de tamaño pequeño/medio  
**Constraints**: Endpoints solo bajo `/api/v1`; cualquier endpoint fuera del prefijo versionado responde `404`; HTTP Basic obligatorio en endpoints expuestos; credenciales iniciales por configuración/entorno; `limit <= 100`; baja lógica (`activo=false`)  
**Scale/Scope**: Un servicio backend monolítico con un catálogo de empleados y dos roles de acceso (`ADMIN`, `LECTOR`)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Stack Gate: Uses Spring Boot 3.x and Java 17 for backend implementation.
- Security Gate: Defines HTTP Basic authentication scope for all exposed endpoints and externalized credentials (no hardcoded secrets).
- Auth Gate: Enforces HTTP Basic authentication on all endpoints with 401 for missing/invalid credentials.
- Versioning Gate: Exposes API endpoints only under `/api/v1` (no unversioned endpoints).
- Data Gate: Uses PostgreSQL with explicit schema/migration approach.
- Runtime Gate: Defines Docker/Docker Compose strategy for local parity.
- API Contract Gate: Confirms OpenAPI/Swagger updates for any API surface changes.
- Separation Gate: Documents controller/service/repository responsibilities.

## Project Structure

### Documentation (this feature)

```text
specs/001-crud-empleados/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)
```text
DSW02-Practica01/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── src/
    └── main/
        ├── java/com/example/empleados/
        │   ├── api/
        │   ├── application/
        │   ├── domain/
        │   ├── infrastructure/
        │   └── security/
        └── resources/
            ├── application.yml
            └── db/migration/

specs/001-crud-empleados/
├── spec.md
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/openapi.yaml
└── tasks.md
```

**Structure Decision**: Se usa un proyecto backend único (`DSW02-Practica01`) con arquitectura en capas (`api`, `application`, `domain`, `infrastructure`, `security`) y artefactos Speckit separados en `specs/001-crud-empleados`.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | N/A | N/A |

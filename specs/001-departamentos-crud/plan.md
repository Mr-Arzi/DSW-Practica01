# Implementation Plan: CRUD de Departamentos

**Branch**: `001-departamentos-crud` | **Date**: 2026-03-19 | **Spec**: `/specs/001-departamentos-crud/spec.md`
**Input**: Feature specification from `/specs/001-departamentos-crud/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar CRUD completo de departamentos con relación 1:N hacia empleados, aplicando validaciones de negocio (clave única case-insensitive normalizada a MAYUSCULAS, nombre no vacío, bloqueo de eliminación con empleados asociados), control de acceso por rol (`GET`: ADMIN/LECTOR, escritura: ADMIN), migración de datos con departamento por defecto `SIN_ASIGNAR`, y documentación OpenAPI para todos los endpoints versionados bajo `/api/v1`.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.3.x (Web, Validation, Security, Data JPA), Flyway, springdoc-openapi 2.6.0  
**Storage**: PostgreSQL 16 (tablas `departamentos` y `empleados`)  
**Testing**: JUnit 5 + Spring Boot Test + Maven Surefire (`mvn test`)  
**Target Platform**: Linux containerizado con Docker Compose  
**Project Type**: Web service (API REST backend monolítico)  
**Performance Goals**: p95 de consultas de departamentos < 1s bajo carga operativa normal (SC-005)  
**Constraints**: API versionada en `/api/v1`; seguridad HTTP Basic; mapeo de errores `400/404/409`; migraciones solo por Flyway; integridad referencial obligatoria entre empleado y departamento  
**Scale/Scope**: Catálogo organizacional interno con operaciones CRUD de departamentos y adaptación de empleados existentes al nuevo modelo

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Stack Gate: PASS — mantiene Spring Boot 3.x + Java 17.
- Security Gate: PASS — define autenticación HTTP Basic con permisos por método y rol.
- Auth Gate: PASS — no hay endpoints públicos nuevos; se mantiene `401/403` según credenciales/permisos.
- Versioning Gate: PASS — endpoints se definirán bajo `/api/v1/departamentos`.
- Data Gate: PASS — cambios de esquema y backfill documentados vía Flyway.
- Runtime Gate: PASS — ejecución local con Docker Compose (`backend` + `postgres`).
- API Contract Gate: PASS — se generará contrato OpenAPI para operaciones y errores.
- Separation Gate: PASS — controladores, servicios, repositorios y entidades separados por capa existente.

### Post-Design Constitution Re-Check

- Stack Gate: PASS
- Security Gate: PASS
- Auth Gate: PASS
- Versioning Gate: PASS
- Data Gate: PASS
- Runtime Gate: PASS
- API Contract Gate: PASS
- Separation Gate: PASS

## Project Structure

### Documentation (this feature)

```text
specs/001-departamentos-crud/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── openapi.yaml
└── tasks.md
```

### Source Code (repository root)

```text
DSW02-Practica01/
├── pom.xml
├── docker-compose.yml
└── src/
    └── main/
        ├── java/com/example/empleados/
        │   ├── api/
        │   ├── api/dto/
        │   ├── application/
        │   ├── application/exception/
        │   ├── domain/
        │   ├── infrastructure/
        │   └── security/
        └── resources/
            ├── application.yml
            └── db/migration/
```

**Structure Decision**: Se mantiene la estructura monolítica backend existente en `DSW02-Practica01`, agregando artefactos de feature en `specs/001-departamentos-crud` y extendiendo capas `domain/application/api/infrastructure` para el agregado `Departamento` y su relación con `Empleado`.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | N/A | N/A |

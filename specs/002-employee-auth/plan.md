# Implementation Plan: Employee Authentication via Email & Password

**Branch**: `002-employee-auth` | **Date**: 2026-03-12 | **Spec**: `/specs/002-employee-auth/spec.md`
**Input**: Feature specification from `/specs/002-employee-auth/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Agregar autenticación de empleados por correo y contraseña con endpoint `POST /api/v1/auth/login` que valida credenciales contra empleados activos y responde solo confirmación (`200`) o rechazo (`401`) sin emitir token/sesión. La solución mantiene el stack Spring Boot 3 + Java 17, persistencia PostgreSQL con Flyway para añadir `email` y `password_hash`, hashing bcrypt para contraseñas y documentación OpenAPI actualizada bajo rutas versionadas `/api/v1`.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.3.x (Web, Validation, Security, Data JPA), Flyway, PostgreSQL driver, springdoc-openapi  
**Storage**: PostgreSQL 16 (tabla `empleado`)  
**Testing**: JUnit 5 + Spring Boot Test + Maven Surefire (`mvn test`)  
**Target Platform**: Linux server containerizado con Docker
**Project Type**: Web service (API REST backend monolítico)  
**Performance Goals**: p95 login < 500 ms; soporte de al menos 50 intentos de login/segundo según spec  
**Constraints**: Endpoint bajo `/api/v1`; contraseñas hasheadas (no plaintext); sin JWT/sesión en respuesta; `401` para credenciales inválidas; cambios de esquema solo vía Flyway  
**Scale/Scope**: Extensión del servicio actual de empleados, sin registro de usuarios ni reset de contraseña en este alcance

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Stack Gate: PASS — se mantiene Spring Boot 3.x + Java 17.
- Security Gate: PASS — se define excepción pública explícita solo para `POST /api/v1/auth/login`; resto de endpoints mantienen HTTP Basic.
- Auth Gate: PASS — endpoints protegidos continúan con HTTP Basic y `401` sin credenciales válidas; login responde `401` para credenciales inválidas.
- Versioning Gate: PASS — endpoint nuevo se expone únicamente en `/api/v1/auth/login`.
- Data Gate: PASS — impacto de datos definido (`email`, `password_hash`) y migrado con Flyway.
- Runtime Gate: PASS — ejecución local en Docker Compose (app + PostgreSQL).
- API Contract Gate: PASS — contrato OpenAPI incluirá request/response de login.
- Separation Gate: PASS — responsabilidades en capas `api`/`application`/`infrastructure`/`security`.

### Post-Design Constitution Re-Check

- Stack Gate: PASS
- Security Gate: PASS (excepción pública documentada para login)
- Auth Gate: PASS
- Versioning Gate: PASS
- Data Gate: PASS
- Runtime Gate: PASS
- API Contract Gate: PASS
- Separation Gate: PASS

## Project Structure

### Documentation (this feature)

```text
specs/002-employee-auth/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── openapi.yaml
└── tasks.md             # generado por /speckit.tasks
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
        │   │   └── dto/
        │   ├── application/
        │   │   └── exception/
        │   ├── domain/
        │   ├── infrastructure/
        │   └── security/
        └── resources/
            ├── application.yml
            └── db/migration/

specs/002-employee-auth/
├── spec.md
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
└── contracts/openapi.yaml
```

**Structure Decision**: Se mantiene la estructura backend existente en `DSW02-Practica01` con arquitectura por capas y se agregan artefactos de planificación/documentación en `specs/002-employee-auth`.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | N/A | N/A |


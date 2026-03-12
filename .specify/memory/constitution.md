<!--
Sync Impact Report
- Version change: 1.0.0 → 1.1.0
- Modified principles:
	- None
- Added sections:
	- VI. API Versioning Is Mandatory
	- VII. Authentication Is Mandatory
- Removed sections:
	- None
- Templates requiring updates:
	- ✅ updated: .specify/templates/plan-template.md
	- ✅ updated: .specify/templates/spec-template.md
	- ✅ updated: .specify/templates/tasks-template.md
	- ⚠ pending: .specify/templates/commands/*.md (directory not present)
	- ⚠ pending: README.md and docs/quickstart.md (files not present)
- Follow-up TODOs:
	- Create command templates directory if custom speckit command docs are required.
	- Add repository runtime docs to reflect this constitution when docs are created.
-->

# DSW-Practica02 Constitution

## Core Principles

### I. API Backend First (Spring Boot 3)
All backend services MUST be implemented with Spring Boot 3 and Java 17. New server-side
features MUST be delivered as REST endpoints with clear request/response contracts, and
MUST keep controller, service, and persistence responsibilities separated. This project is
backend-focused; UI work is out of scope unless explicitly requested for API validation.
Rationale: a single modern stack reduces maintenance cost and migration risk.

### II. Security by Default (HTTP Basic)
Every exposed API endpoint MUST be protected with Spring Security using HTTP Basic
authentication by default, except health or explicit public endpoints justified in the
specification. Credentials MUST NOT be hardcoded and MUST be provided via environment
configuration. Authorization rules MUST follow least privilege, and security-relevant
events SHOULD be logged for traceability.
Rationale: enforcing a secure baseline avoids insecure defaults and deployment leaks.

### III. PostgreSQL as Source of Truth
Persistent application data MUST be stored in PostgreSQL. Schema evolution MUST be
managed through versioned migrations, and local/test/prod environments MUST target the
same database engine family. SQL or ORM mappings MUST be deterministic and reviewable.
Rationale: consistent relational storage prevents environment drift and data integrity bugs.

### IV. Dockerized Runtime Parity
Application and database runtime MUST be containerized with Docker, and local execution
MUST be reproducible through Docker Compose or an equivalent project-standard command.
Service configuration MUST come from environment variables so the same image can run
across environments without code changes. Any feature requiring external services MUST
document its container dependencies.
Rationale: runtime parity reduces "works on my machine" failures.

### V. Contract-Driven Documentation (Swagger)
All REST APIs MUST be documented through OpenAPI/Swagger and kept synchronized with
the running code. Pull requests that add or change endpoints MUST include updated API
documentation artifacts or generation configuration. Documented examples for auth,
request payloads, and response codes MUST be accurate.
Rationale: up-to-date contracts accelerate integration and reduce ambiguity for consumers.

### VI. API Versioning Is Mandatory
All APIs MUST be versioned. The current version MUST be exposed under the `/api/v1`
prefix. No endpoint MAY exist outside a versioned API path.
Rationale: explicit API versioning prevents contract drift and enables controlled evolution.

### VII. Authentication Is Mandatory
All endpoints MUST require HTTP Basic authentication. Initial credentials MUST be:
username `admin`, password `admin123`. Requests without valid authentication MUST
respond with HTTP `401 Unauthorized`.
Rationale: a uniform auth gate protects every endpoint and enforces consistent behavior.

## Security and Infrastructure Standards

- Java version MUST be 17 for build and runtime.
- Spring Boot version MUST be 3.x across modules.
- PostgreSQL connection settings MUST be externalized (`SPRING_DATASOURCE_*`).
- Docker images MUST be buildable in CI without manual machine-specific steps.
- Secrets MUST be injected from environment or secret stores, never committed.
- API error handling MUST avoid leaking sensitive internals.
- APIs MUST be exposed under `/api/v1` and MUST NOT ship unversioned routes.
- Requests without valid HTTP Basic credentials MUST return HTTP 401 Unauthorized.

## Delivery Workflow and Quality Gates

- Every feature spec MUST include authentication impact, data impact, and API
	documentation impact.
- Every implementation plan MUST pass a Constitution Check covering stack compliance,
	security baseline, mandatory API versioning, persistence strategy, container runtime,
	and Swagger coverage.
- Every task breakdown MUST include explicit tasks for security config, PostgreSQL
	integration, Docker setup, and OpenAPI documentation when API changes exist.
- PR review MUST fail if constitution checks are unmet without a documented exception.

## Governance

This constitution overrides conflicting local development conventions for this repository.
Amendments require: (1) a documented proposal, (2) explicit update of related templates,
and (3) a migration note for any affected active specs.

Versioning policy for this constitution follows semantic versioning:
- MAJOR: removal or incompatible redefinition of a principle or governance rule.
- MINOR: new principle or materially expanded mandatory guidance.
- PATCH: clarifications, wording, and non-semantic refinements.

Compliance review is mandatory at plan approval, task generation, and pull request review.
Any temporary exception MUST include scope, owner, expiration date, and remediation plan.

**Version**: 1.1.0 | **Ratified**: 2026-02-25 | **Last Amended**: 2026-03-05

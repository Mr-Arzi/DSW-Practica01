# Research: Employee Authentication via Email & Password

## Decision 1: Authentication response model
- Decision: Return confirmation-only payload on success (`{"message":"Authentication successful"}`), without JWT/session artifacts.
- Rationale: User clarified Q1 as option C; keeps scope aligned to credential validation only.
- Alternatives considered: JWT issuance; server-side session.

## Decision 2: Password hashing strategy
- Decision: Use Spring Security `PasswordEncoder` with bcrypt for employee password hashing and verification.
- Rationale: Meets security requirement to avoid plaintext and uses battle-tested built-in implementation.
- Alternatives considered: Argon2 (strong but adds migration/config overhead), PBKDF2 (acceptable but less aligned with current Spring defaults).

## Decision 3: Employee lookup scope
- Decision: Authenticate only employees that are active and have populated `email` and `password_hash`.
- Rationale: Aligns with existing active-catalog behavior and avoids authenticating logically deleted records.
- Alternatives considered: Allow inactive employees; authenticate any record with matching email.

## Decision 4: Legacy employee seeding
- Decision: Keep legacy employee email/password seeding out of scope for this feature.
- Rationale: User clarified Q2 as option C; avoids adding onboarding/migration workflows not requested.
- Alternatives considered: Auto-generated emails/passwords in migration; manual seed file import.

## Decision 5: Endpoint security posture
- Decision: Expose `POST /api/v1/auth/login` as explicit public endpoint and keep HTTP Basic required for existing protected endpoints.
- Rationale: Login must be callable without prior credentials, while preserving current protection model for business APIs.
- Alternatives considered: Require HTTP Basic for login (circular auth); convert all endpoints to employee login model in this feature.

## Decision 6: Schema migration approach
- Decision: Add nullable-safe migration path with new columns on `empleado` table (`email`, `password_hash`) and unique index on `email` for active records strategy.
- Rationale: Maintains PostgreSQL/Flyway governance and enables deterministic lookup by email.
- Alternatives considered: New credentials table; application-managed schema updates without Flyway.

## Decision 7: API contract and error behavior
- Decision: Standardize login responses to `200` on valid credentials and `401` with `{"error":"Invalid email or password"}` for invalid credentials.
- Rationale: Matches spec and prevents credential enumeration.
- Alternatives considered: Distinct errors for unknown email vs wrong password; `404` for unknown email.

## Decision 8: Testing strategy
- Decision: Add service-level credential validation tests and controller integration tests for login success/failure using Spring Boot test stack.
- Rationale: Provides confidence for auth behavior without changing entire existing auth framework.
- Alternatives considered: Unit tests only; manual verification only.

# Feature Specification: Employee Authentication via Email & Password

**Feature Branch**: `002-employee-auth`  
**Created**: 12 de marzo de 2026  
**Status**: Draft  
**Input**: User description: "autenticación de empleados mediante correo y contraseña"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Employee Login with Valid Credentials (Priority: P1)

An employee with a registered email and password attempts to log in to access the system. Upon providing correct credentials, the system authenticates them and confirms successful authentication.

**Why this priority**: This is the core feature—without successful login, the system cannot serve its purpose. It unlocks all protected endpoints for authenticated users.

**Independent Test**: Can be fully tested by submitting valid email/password to `POST /api/v1/auth/login` and verifying a successful response, delivering immediate user value.

**Acceptance Scenarios**:

1. **Given** an employee with email `empleado@empresa.com` and password `SecurePass123`, **When** they POST to `/api/v1/auth/login` with those credentials, **Then** the system responds with `200 OK` and message `"Authentication successful"`.
2. **Given** an employee exists in the system, **When** they log in with correct email and password, **Then** the endpoint confirms credential validity and does not return token or session information.

---

### User Story 2 - Login Fails with Invalid Email (Priority: P2)

An employee (or attacker) attempts to log in using an email that is not registered in the system. The system rejects the attempt with an appropriate error.

**Why this priority**: Security requirement—prevents unauthorized access and provides clear feedback on invalid credentials.

**Independent Test**: Can be tested by submitting a non-existent email and any password to `POST /api/v1/auth/login` and verifying `401 Unauthorized` response.

**Acceptance Scenarios**:

1. **Given** an employee attempts login with email `nonexistent@empresa.com`, **When** no employee exists with that email, **Then** the system responds with `401 Unauthorized` and message `"Invalid email or password"`.
2. **Given** any syntactically invalid email format (e.g., `notanemail`), **When** a login request is submitted, **Then** Bean Validation rejects the request before auth begins and the system responds with `400 Bad Request` (format check, not credential check). Only syntactically valid emails that fail credential lookup receive `401`.

---

### User Story 3 - Login Fails with Incorrect Password (Priority: P2)

An employee attempts to log in with a registered email but an incorrect password. The system rejects the attempt without compromising security.

**Why this priority**: Security critical—prevents unauthorized access and maintains password integrity.

**Independent Test**: Can be tested by submitting a valid email and wrong password to `POST /api/v1/auth/login` and verifying `401 Unauthorized` response.

**Acceptance Scenarios**:

1. **Given** an employee with email `empleado@empresa.com` exists, **When** they submit a password that does not match the stored password, **Then** the system responds with `401 Unauthorized` and message `"Invalid email or password"`.
2. **Given** multiple failed password attempts, **When** all use registered emails with incorrect passwords, **Then** all receive `401 Unauthorized` without timing variations that could leak password validity.

---

### User Story 4 - Password Security Requirement (Priority: P1)

The system stores all passwords using secure cryptographic hashing. No plaintext passwords are stored or transmitted in logs.

**Why this priority**: Fundamental security requirement—failure here compromises all user accounts and violates security best practices.

**Independent Test**: Can be verified by inspecting the database schema and confirming that the `password_hash` column contains only hashed values, and by verifying that hashing is applied consistently on all password storage/comparison operations.

**Acceptance Scenarios**:

1. **Given** an employee is created or password is updated, **When** the password is persisted to PostgreSQL, **Then** the database stores a cryptographic hash (e.g., bcrypt) not plaintext.
2. **Given** a login attempt with a password, **When** the system compares it to the stored hash, **Then** the comparison uses secure algorithm (timing-safe comparison).

---

### Edge Cases

- What happens when login request is missing `email` or `password` fields? → `400 Bad Request`
- What happens when email exists but has no password_hash (migration edge case)? → `401 Unauthorized`
- What happens if the same email appears multiple times in the database (data corruption)? → Resolved: UNIQUE constraint on `email` added in V2 Flyway migration prevents new duplicates. Any pre-migration duplicates are DBA responsibility to clean before deploy. Application code may rely on `findByEmailAndActivo` returning at most one result post-migration.
- What happens on concurrent login requests from the same user? → Both succeed/fail independently based on credentials

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST expose a `POST /api/v1/auth/login` endpoint that accepts email and password.
- **FR-002**: System MUST validate that the employee exists by searching for the provided email in the active employees list.
- **FR-003**: System MUST compare the provided password against the stored password hash using a timing-safe algorithm.
- **FR-004**: System MUST respond with `200 OK` and message `{"message":"Authentication successful"}` when credentials are valid.
- **FR-005**: System MUST respond with `401 Unauthorized` and message `{"error":"Invalid email or password"}` when email does not exist or password is incorrect.
- **FR-006**: System MUST store all employee passwords using bcrypt (or equivalent OWASP-recommended hashing algorithm) with a minimum work factor appropriate to 2026 standards.
- **FR-007**: System MUST NOT log plaintext passwords in any logs, debug output, or error messages.
- **FR-008**: System MUST validate that `email` and `password` fields are present in the request body and not empty.
- **FR-009**: Upon successful authentication, the endpoint MUST return only a confirmation payload (`{"message":"Authentication successful"}`) and MUST NOT return JWT, session identifier, or similar authentication artifacts.
- **FR-010**: System MUST ensure that all password-related database schema changes are applied via Flyway migrations (non-rollback).

### Constitution Alignment Requirements *(mandatory)*

- **CA-001**: Backend implementation targets Spring Boot 3.x with Java 17.
- **CA-002**: Authentication endpoint (`/api/v1/auth/login`) is distinct from HTTP Basic Auth for management endpoints; it allows employees to log in with their registered email/password; endpoint is unprotected (no auth required to POST); it only validates credentials and returns confirmation (no token/session issuance).
- **CA-003**: Persistent data changes include adding `email` (unique, **nullable** for legacy rows) and `password_hash` (**nullable** for legacy rows) columns to the `empleado` table via Flyway V2 migration. Nullable columns prevent migration failure on existing rows without credentials. Authentication requires both fields to be non-null at runtime (query scope enforced in `AuthService`). Legacy employee email/password seeding is out of scope for this feature.
- **CA-004**: Docker Compose setup includes PostgreSQL; environment variables (if any) for password hashing configuration are documented.
- **CA-005**: OpenAPI/Swagger documentation updated to include `POST /api/v1/auth/login` with request/response schemas and example payloads.
- **CA-006**: Endpoint is routed under `/api/v1/auth/login`; requests to `/api/auth/login` return `404`.

### Key Entities *(include if feature involves data)*

- **Empleado (Employee)**: Extended with `email` (string, unique, **nullable at DB level** — required at application level for authenticated employees) and `password_hash` (string, **nullable at DB level** — required at application level). Legacy rows without these fields remain out of auth scope. Relationships: one-to-one with login credentials. Attributes: `clave`, `nombre`, `direccion`, `telefono`, `activo`, `email`, `password_hash`.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Employees can authenticate successfully and receive a response in under 500 milliseconds for 95th percentile of requests.
- **SC-002**: 100% of login requests with invalid email or incorrect password receive `401 Unauthorized` response without leaking which field is invalid.
- **SC-003**: Zero plaintext passwords found in the database; all stored passwords use bcrypt with work factor ≥12 (verifiable via inspection of password_hash values in PostgreSQL).
- **SC-004**: System handles at least 50 concurrent login attempts per second without failures or noticeable (>100ms) latency increase.
- **SC-005**: All password comparisons use timing-safe algorithms; no timing attacks are possible that could leak password validity.
- **SC-006**: OpenAPI/Swagger documentation is complete and accurate; endpoints are discoverable and testable from Swagger UI.

---

## Assumptions

1. Employees are uniquely identified by email (one employee == one email).
2. Password hashing library (bcrypt) is already available in Spring Security dependencies.
3. Existing employees without email/password are out of scope for this feature; the authentication flow applies only to employees that already have both fields populated.
4. Successful login only confirms credentials and does not create or return token/session artifacts.
5. The `/api/v1/auth/login` endpoint does not require HTTP Basic Auth to POST; it is publicly accessible to allow initial employee login.

---

## Open Questions

1. **Password Reset**: Out of scope for this feature. Deferred to a future feature when employee self-service credential management is explicitly requested.

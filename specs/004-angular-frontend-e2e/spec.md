# Feature Specification: Frontend Web Angular con Pruebas E2E

**Feature Branch**: `004-angular-frontend-e2e`  
**Created**: 19 de marzo de 2026  
**Status**: Draft  
**Input**: User description: "Nueva funcionalidad: desarrollo de frontend con Angular y pruebas end-to-end con Cypress."

## Clarifications

### Session 2026-03-19

- Q: Que estrategia de credenciales HTTP Basic usara el frontend tras login? -> A: Reutilizar el mismo correo/contrasena ingresado en Login para construir `Authorization: Basic` en todas las llamadas a `/api/v1` durante la sesion activa.
- Q: Donde se conservaran las credenciales Basic durante la sesion del navegador? -> A: Persistir credenciales de sesion en `sessionStorage` durante la sesion activa del navegador.
- Q: Como debe comportarse el frontend segun roles de backend (ADMIN/LECTOR)? -> A: Aplicar comportamiento por rol: `ADMIN` con CRUD completo y `LECTOR` solo consulta, ocultando o deshabilitando acciones de escritura.
- Q: Como se gestionaran datos de prueba para Cypress? -> A: Cypress debe preparar o asegurar sus datos de prueba al inicio y limpiar al finalizar cuando aplique.
- Q: Se incluye logout explicito en esta fase? -> A: Si, incluir accion de logout para limpiar `sessionStorage` y redirigir a Login.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Inicio de sesion y acceso protegido (Priority: P1)

Como empleado, quiero autenticarme desde una pantalla de login para acceder de manera segura a los modulos de gestion del sistema.

**Why this priority**: Sin autenticacion no se puede usar ningun modulo funcional, por lo que este flujo habilita todo el valor del frontend.

**Independent Test**: Se prueba enviando credenciales validas e invalidas desde la UI; debe permitir acceso con credenciales validas y rechazar con mensaje de error cuando son incorrectas.

**Acceptance Scenarios**:

1. **Given** que el usuario esta en la pantalla de login y provee credenciales validas, **When** confirma el inicio de sesion, **Then** el sistema lo redirige al modulo de empleados.
2. **Given** que el usuario ingresa credenciales invalidas, **When** intenta iniciar sesion, **Then** el sistema mantiene la pantalla de login y muestra feedback de autenticacion fallida.
3. **Given** que el usuario no esta autenticado, **When** intenta abrir rutas de empleados o departamentos, **Then** el sistema bloquea el acceso y lo dirige al login.

---

### User Story 2 - Navegacion entre modulos autenticados (Priority: P1)

Como empleado autenticado, quiero navegar entre los modulos de empleados y departamentos sin volver a autenticarme para trabajar de forma continua.

**Why this priority**: La navegacion continua define la experiencia base del frontend operativo y evita friccion durante tareas administrativas.

**Independent Test**: Se valida iniciando sesion una sola vez y navegando entre modulos mediante menu; no debe forzar nuevo login durante la misma sesion activa.

**Acceptance Scenarios**:

1. **Given** que el usuario ya inicio sesion, **When** usa el menu para ir de empleados a departamentos, **Then** el sistema muestra el modulo destino sin pedir credenciales nuevamente.
2. **Given** que el usuario esta autenticado, **When** vuelve al modulo anterior desde el menu, **Then** mantiene estado de acceso permitido en ambos modulos.

---

### User Story 3 - Gestion de empleados desde frontend (Priority: P1)

Como usuario autenticado con permisos, quiero visualizar y gestionar empleados desde la interfaz web para operar el catalogo de personal.

**Why this priority**: La gestion de empleados es uno de los procesos principales del sistema y debe estar disponible en la primera entrega funcional.

**Independent Test**: Se valida el ciclo completo en UI: listar, crear, editar y eliminar empleados, verificando que cada accion refleje cambios en el listado.

**Acceptance Scenarios**:

1. **Given** que existen empleados en el sistema, **When** el usuario abre el modulo de empleados, **Then** visualiza la lista actual de empleados.
2. **Given** que el usuario completa el formulario de alta con datos validos, **When** confirma la creacion, **Then** el nuevo empleado aparece en la lista.
3. **Given** que el usuario selecciona un empleado existente, **When** actualiza sus datos, **Then** el sistema refleja los cambios en la vista.
4. **Given** que el usuario solicita eliminar un empleado, **When** confirma la accion, **Then** el empleado deja de aparecer en la lista activa.

---

### User Story 4 - Gestion de departamentos desde frontend (Priority: P1)

Como usuario autenticado con permisos, quiero visualizar y gestionar departamentos desde la interfaz web para mantener la estructura organizacional.

**Why this priority**: El modulo de departamentos es un flujo principal requerido y debe convivir con empleados dentro de la misma experiencia web.

**Independent Test**: Se valida el ciclo completo en UI: listar, crear, editar y eliminar departamentos, verificando comportamiento esperado para operaciones exitosas y rechazadas por reglas de negocio.

**Acceptance Scenarios**:

1. **Given** que existen departamentos, **When** el usuario abre el modulo de departamentos, **Then** visualiza la lista actual de departamentos.
2. **Given** que el usuario registra un departamento con datos validos, **When** confirma la creacion, **Then** el departamento aparece en la lista.
3. **Given** que el usuario modifica un departamento existente, **When** guarda cambios validos, **Then** la lista muestra los datos actualizados.
4. **Given** que el usuario intenta eliminar un departamento, **When** existen restricciones de negocio para la operacion, **Then** el sistema muestra el resultado correspondiente sin perder consistencia visual.

---

### User Story 5 - Cobertura E2E de flujos criticos (Priority: P1)

Como equipo de desarrollo, queremos pruebas end-to-end automatizadas para garantizar que los flujos criticos del frontend funcionen tras cambios futuros.

**Why this priority**: La cobertura e2e reduce regresiones en login, navegacion y operaciones principales, acelerando entregas seguras.

**Independent Test**: Se ejecuta la suite e2e y se confirma que todos los escenarios criticos definidos se ejecutan y finalizan exitosamente.

**Acceptance Scenarios**:

1. **Given** una build funcional del sistema, **When** se ejecuta la suite e2e, **Then** se valida login exitoso y login fallido.
2. **Given** un usuario autenticado en pruebas, **When** se ejecutan escenarios de navegacion, **Then** se valida cambio entre modulos sin nueva autenticacion.
3. **Given** los modulos disponibles, **When** se ejecutan escenarios de empleados y departamentos, **Then** se valida listado y creacion en ambos dominios.

### Edge Cases


- Que ocurre cuando el backend no esta disponible durante el login o una operacion de gestion? El sistema MUST mostrar error recuperable, permitir reintento manual sin recargar toda la aplicacion y conservar estado de formulario cuando aplique.
- Que ocurre cuando una ruta protegida se abre directamente por URL sin sesion valida? Debe redirigir al login.
- Que ocurre cuando credenciales validas expiran o dejan de ser aceptadas durante una sesion activa? Debe forzar reautenticacion de forma controlada.
- Que ocurre cuando una operacion de creacion o actualizacion recibe validacion fallida desde API? Debe mostrarse feedback de error sin perder datos de formulario no enviados.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST proveer una interfaz web con una pantalla de login para ingreso de correo electronico y contrasena.
- **FR-002**: El sistema MUST validar credenciales de login contra `POST /api/v1/auth/login`, enviando correo y contrasena en el cuerpo de la solicitud y procesando respuestas de exito/autenticacion fallida segun contrato backend.

- **FR-003**: El sistema MUST redirigir al modulo de empleados despues de una autenticacion exitosa.
- **FR-004**: El sistema MUST rechazar credenciales incorrectas mostrando feedback claro en login.
- **FR-005**: El sistema MUST proveer rutas separadas para Login, Gestion de empleados y Gestion de departamentos.
- **FR-006**: El sistema MUST proteger las rutas de empleados y departamentos para acceso exclusivo de usuarios autenticados.
- **FR-007**: El sistema MUST incluir un menu de navegacion para cambiar entre modulos de empleados y departamentos.
- **FR-008**: El sistema MUST mantener el estado autenticado al cambiar entre modulos sin requerir nuevo login durante la sesion activa.
- **FR-009**: El frontend MUST permitir listar, crear, editar y eliminar empleados.
- **FR-010**: El frontend MUST permitir listar, crear, editar y eliminar departamentos.
- **FR-011**: Todas las solicitudes del frontend al backend MUST consumir rutas bajo el prefijo `/api/v1`.
- **FR-012**: Todas las solicitudes a endpoints protegidos bajo `/api/v1` MUST incluir encabezado `Authorization: Basic <base64(usuario:contrasena)>`.
- **FR-013**: La solucion MUST evitar uso de JWT o manejo de tokens en esta fase.
- **FR-014**: El sistema MUST incluir pruebas end-to-end que validen como minimo: login exitoso, login fallido, navegacion entre modulos, listado de empleados, creacion de empleado, listado de departamentos y creacion de departamento.
- **FR-015**: El frontend MUST reutilizar el mismo correo/contrasena capturado en login para construir el encabezado HTTP Basic en todas las llamadas a `/api/v1` durante la sesion activa.
- **FR-016**: El frontend MUST persistir credenciales de sesion para HTTP Basic en `sessionStorage` y MUST eliminarlas al cerrar sesion o al recibir `401 Unauthorized`

- **FR-017**: El frontend MUST aplicar comportamiento por rol de backend: usuarios `ADMIN` con operaciones CRUD completas y usuarios `LECTOR` con acceso de solo lectura, ocultando o deshabilitando acciones de escritura no permitidas.
- **FR-018**: La suite Cypress MUST inicializar datos requeridos al inicio de cada ejecucion y MUST limpiar las entidades creadas por la suite al finalizar cada spec, garantizando repetibilidad entre ejecuciones consecutivas.


- **FR-019**: El frontend MUST incluir accion de logout visible en navegacion, limpiar credenciales almacenadas en `sessionStorage` y redirigir al usuario a la ruta de Login.
- **FR-020**: La navegacion entre rutas protegidas (`/empleados` y `/departamentos`) MUST completar en <= 1s en entorno local de referencia.
- **FR-021**: El render inicial de listados de empleados y departamentos MUST completar en <= 2s en entorno local con dataset operativo.



### Constitution Alignment Requirements *(mandatory)*

- **CA-001**: Backend implementation MUST target Spring Boot 3.x with Java 17.
- **CA-002**: API security MUST define HTTP Basic authentication behavior,
  credential source, initial credentials (`admin` / `admin123`), and `401 Unauthorized`
  behavior for missing/invalid credentials.
- **CA-003**: Persistent data changes MUST describe PostgreSQL schema impact and
  migration strategy.
- **CA-004**: Local execution requirements MUST define Docker (and when needed,
  Docker Compose) services and environment variables.
- **CA-005**: API changes MUST include OpenAPI/Swagger documentation updates.
- **CA-006**: API routes MUST be versioned and exposed under `/api/v1` only.
- **CA-007**: The system MUST include a web frontend implemented in Angular using a modern stable version compatible with the current ecosystem.
- **CA-008**: Frontend API requests MUST use HTTP Basic authentication credentials for protected endpoints and consume backend endpoints under `/api/v1`.
- **CA-009**: The system MUST include Cypress end-to-end tests that cover authentication, employee management, and department management core flows.

### Key Entities *(include if feature involves data)*

- **SesionAutenticada**: Estado de usuario autenticado en frontend que habilita acceso a rutas protegidas y llamadas API durante la sesion.
- **VistaEmpleado**: Representacion en interfaz de los datos de empleado para operaciones de listado, alta, edicion y eliminacion.
- **VistaDepartamento**: Representacion en interfaz de los datos de departamento para operaciones de listado, alta, edicion y eliminacion.
- **EscenarioE2E**: Caso automatizado de extremo a extremo que valida un flujo de negocio observable en UI.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de intentos con credenciales validas en login permite acceso al sistema y redirecciona al modulo de empleados.
- **SC-002**: El 100% de intentos con credenciales invalidas en login permanece en pantalla de acceso mostrando error de autenticacion.
- **SC-003**: El 100% de accesos directos a rutas protegidas sin sesion autenticada son bloqueados.
- **SC-004**: Usuarios autenticados pueden cambiar entre los modulos de empleados y departamentos sin volver a autenticarse durante la sesion activa.
- **SC-005**: El 100% de operaciones CRUD visibles en frontend para empleados y departamentos reflejan su resultado esperado en la interfaz.
- **SC-006**: La suite e2e cubre y ejecuta exitosamente los 7 flujos minimos definidos de autenticacion, navegacion y gestion principal.

## Assumptions

1. El backend existente expone endpoints operativos de autenticacion, empleados y departamentos bajo `/api/v1`.
2. Los usuarios que usan frontend cuentan con credenciales validas preexistentes.
3. La autorizacion por rol se resuelve en backend y el frontend refleja respuestas de acceso permitido o denegado.
4. La primera entrega de e2e prioriza flujos principales y puede ampliar cobertura en iteraciones posteriores.


# Research: Frontend Angular con Pruebas E2E

## Decision 1: Arquitectura de frontend Angular
- Decision: Implementar frontend Angular con componentes standalone, Angular Router y servicios por dominio (`auth`, `empleados`, `departamentos`).
- Rationale: Reduce boilerplate de modulos, facilita separacion por feature y acelera mantenimiento.
- Alternatives considered: Arquitectura basada en NgModules clasicos; microfrontends.

## Decision 2: Manejo de autenticacion HTTP Basic
- Decision: Reutilizar correo/contrasena capturados en login para construir el header `Authorization: Basic ...` y persistirlo en `sessionStorage` durante sesion activa.
- Rationale: Cumple aclaraciones de producto, evita JWT en esta fase y permite sesion por pestana del navegador.
- Alternatives considered: `localStorage` (mayor superficie de riesgo), memoria volatile (se pierde al refrescar), JWT.

## Decision 3: Proteccion de rutas y estado de sesion
- Decision: Usar guard de rutas para bloquear `/empleados` y `/departamentos` sin sesion valida, y redirigir a `/login`.
- Rationale: Cumple FR-006 y estandariza control de acceso del lado cliente.
- Alternatives considered: Proteccion solo en componentes; proteccion solo por respuestas 401 del backend.

## Decision 4: Comportamiento por rol de backend
- Decision: Frontend aplicara visibilidad/acciones por rol efectivo (`ADMIN` CRUD, `LECTOR` solo lectura) ocultando o deshabilitando acciones no permitidas.
- Rationale: Cumple FR-017 y evita UX basada en errores recurrentes `403`.
- Alternatives considered: Mostrar CRUD para todos y depender de `403`; restringir todo a ADMIN.

## Decision 5: Integracion API existente
- Decision: Consumir exclusivamente endpoints backend bajo `/api/v1` mediante un cliente HTTP central con interceptor de Basic Auth y manejo estandar de errores.
- Rationale: Cumple CA-006/CA-008 y simplifica trazabilidad de llamadas.
- Alternatives considered: Llamadas directas dispersas por componente; clientes separados sin interceptor.

## Decision 6: Estrategia de pruebas E2E con Cypress
- Decision: Implementar suite Cypress con preparacion de datos al inicio de ejecucion y limpieza al finalizar cuando aplique.
- Rationale: Aumenta repetibilidad y estabilidad en CI/CD y ambientes compartidos.
- Alternatives considered: Reusar datos existentes del ambiente; cobertura solo de lectura.

## Decision 7: Flujos minimos cubiertos por E2E
- Decision: Incluir como minimo: login exitoso, login fallido, navegacion entre modulos, listado de empleados, creacion de empleado, listado de departamentos, creacion de departamento.
- Rationale: Cubre flujos principales definidos por FR-014/CA-009.
- Alternatives considered: Solo smoke login; cobertura completa de CRUD en primera iteracion.

## Decision 8: Compatibilidad y versiones tecnicas
- Decision: Angular moderno y estable (Angular 18 LTS), TypeScript 5.x y Cypress 13.x, integrados con backend Spring Boot 3/Java 17 existente.
- Rationale: Mantiene compatibilidad del ecosistema y soporte activo.
- Alternatives considered: Angular de version anterior sin soporte actual; frameworks frontend alternativos.

# Research: CRUD de Empleados

## Decisión 1: Stack backend
- Decision: Usar Spring Boot 3 con Java 17.
- Rationale: Está mandatado por la constitución y ofrece soporte estable para REST, validación y seguridad.
- Alternatives considered: Node.js/Express, Quarkus. Rechazadas por no cumplir el estándar del proyecto.

## Decisión 2: Versionamiento de API
- Decision: Exponer todos los endpoints funcionales exclusivamente bajo `/api/v1`.
- Rationale: Cumple la constitución vigente, habilita evolución futura (`/api/v2`) sin romper compatibilidad y define frontera clara de contrato con `404` fuera del prefijo versionado.
- Alternatives considered: Rutas sin versión; versionado en header. Rechazadas por menor claridad contractual en este alcance.

## Decisión 3: Seguridad de API
- Decision: Aplicar HTTP Basic obligatorio en todos los endpoints expuestos por el sistema con credencial inicial mínima `admin/admin123`, cargada por variables de entorno/configuración externa, manteniendo RBAC existente (`ADMIN`, `LECTOR`) para operaciones de negocio.
- Rationale: Cumple el principio constitucional de autenticación obligatoria, evita secretos hardcodeados y garantiza respuesta uniforme `401` sin credenciales válidas en cualquier ruta publicada.
- Alternatives considered: Limitar autenticación solo a endpoints funcionales; migrar a JWT/OAuth2. Rechazadas por incumplir constitución o añadir complejidad fuera de alcance.

## Decisión 4: Persistencia
- Decision: PostgreSQL como única fuente de verdad con migraciones Flyway.
- Rationale: Garantiza consistencia entre ambientes y versionado explícito de esquema.
- Alternatives considered: H2 en producción, MySQL. Rechazadas por no alinear con la constitución.

## Decisión 5: Estrategia de eliminación
- Decision: Baja lógica mediante campo `activo=false`.
- Rationale: Preserva trazabilidad de registros y satisface la aclaración aprobada en la spec.
- Alternatives considered: Eliminación física. Rechazada por pérdida de histórico y menor trazabilidad.

## Decisión 6: Validaciones de campos
- Decision: `clave` derivada con patrón `EMP-<autonumérico>`; `nombre` y `direccion` máx. 100; `telefono` solo dígitos máx. 100.
- Rationale: Sigue reglas explícitas de negocio y elimina ambigüedades de entrada.
- Alternatives considered: Restricción fija de longitud de teléfono (10-15), permitir separadores. Rechazadas por no estar en alcance.

## Decisión 7: Paginación de colecciones
- Decision: Soportar `page` y `limit` en endpoints de colección, con default `page=1`, `limit=10`, máximo `limit=100`; si `page` excede resultados, devolver `200` con `data: []`.
- Rationale: Alinea requerimientos funcionales, evita errores innecesarios de navegación y controla carga por request.
- Alternatives considered: `404`/`400` para página fuera de rango; sin límite máximo. Rechazadas por UX menos predecible o riesgos de performance.

## Decisión 8: Contrato de API y documentación
- Decision: Publicar contrato OpenAPI 3.0 y exponer Swagger UI con esquema HTTP Basic.
- Rationale: La constitución exige documentación de contrato sincronizada con endpoints.
- Alternatives considered: Documentación manual en Markdown. Rechazada por baja trazabilidad y mantenimiento.

## Decisión 9: Ejecución local y paridad
- Decision: Levantar app y PostgreSQL con Docker Compose y variables de entorno (`SPRING_DATASOURCE_*`).
- Rationale: Reduce desvíos entre desarrollo y despliegue, cumpliendo la constitución.
- Alternatives considered: Base de datos local fuera de contenedor. Rechazada por menor reproducibilidad.

## Decisión 10: Pruebas
- Decision: Usar pruebas unitarias, de integración (con PostgreSQL real en Testcontainers) y contract tests de endpoints críticos.
- Rationale: Cubre reglas de negocio, persistencia real y estabilidad del contrato API.
- Alternatives considered: Solo pruebas unitarias. Rechazada por bajo nivel de confianza para integración DB + seguridad.

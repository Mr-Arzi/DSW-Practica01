# Quickstart: CRUD de Empleados

## Prerrequisitos
- Java 17
- Maven 3.9+
- Docker y Docker Compose

## 1) Levantar backend + PostgreSQL con Docker

```bash
cd /home/roberto/Despliegue_Software/DSW-Practica02/DSW02-Practica01
docker compose up -d --build
```

Variables esperadas para la app:
- `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/empleados` (dentro de Docker)
- `SPRING_DATASOURCE_USERNAME=empleados_user`
- `SPRING_DATASOURCE_PASSWORD=empleados_pass`
- Credenciales iniciales HTTP Basic (`admin/admin123`) provistas por variables de entorno o configuración externa (no hardcodeadas en código fuente)

## 2) Verificar estado de contenedores

```bash
cd /home/roberto/Despliegue_Software/DSW-Practica02/DSW02-Practica01
docker compose ps
```

## 3) Verificar API y Swagger
- Swagger UI: `http://localhost:8080/api/v1/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/v1/v3/api-docs`
- Todos los endpoints expuestos requieren autenticación HTTP Basic (incluyendo rutas de documentación si están publicadas).

## 4) Credenciales de ejemplo (HTTP Basic)
- `admin` / `admin123` (rol `ADMIN`)
- `lector` / `lector123` (rol `LECTOR`)

## 5) Flujo mínimo de validación
1. Crear empleado con `admin` (POST `/api/v1/empleados`).
2. Consultar listado paginado con `lector` (GET `/api/v1/empleados?page=1&limit=10`).
3. Actualizar empleado con `admin` (PUT `/api/v1/empleados/{clave}`).
4. Eliminar lógicamente con `admin` (DELETE `/api/v1/empleados/{clave}`).
5. Verificar que ya no aparece en catálogo activo.

Ejemplo de alta (la `clave` se genera automáticamente):

```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/v1/empleados \
	-H "Content-Type: application/json" \
	-d '{"nombre":"Ana Pérez","direccion":"Calle 1","telefono":"5512345678"}'
```

Ejemplo de consulta de catálogo activo paginado:

```bash
curl -u lector:lector123 "http://localhost:8080/api/v1/empleados?page=1&limit=10"
```

## 6) Ejecutar pruebas

```bash
cd /home/roberto/Despliegue_Software/DSW-Practica02/DSW02-Practica01
mvn test
```

## 7) Detener contenedores

```bash
cd /home/roberto/Despliegue_Software/DSW-Practica02/DSW02-Practica01
docker compose down
```

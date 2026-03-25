# Quickstart: CRUD de Departamentos

## Prerequisites
- Docker + Docker Compose
- Credenciales HTTP Basic configuradas (`admin`/`admin123`, `lector`/`lector123`)

## 1) Start runtime

```bash
cd /home/roberto/Despliegue_Software/DSW-Practica02/DSW02-Practica01
docker compose up -d --build
```

## 2) Crear departamento (ADMIN)

```bash
curl -i -X POST http://localhost:8080/api/v1/departamentos \
  -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{"clave":"fin","nombre":"Finanzas"}'
```

Esperado:
- `201 Created`
- `clave` almacenada en MAYUSCULAS (`FIN`)

## 3) Listar departamentos (ADMIN/LECTOR)

```bash
curl -i http://localhost:8080/api/v1/departamentos -u lector:lector123
```

Esperado:
- `200 OK`

## 4) Consultar por id

```bash
curl -i http://localhost:8080/api/v1/departamentos/1 -u lector:lector123
```

Esperado:
- `200 OK` si existe
- `404 Not Found` si no existe

## 5) Validar duplicidad de clave

```bash
curl -i -X POST http://localhost:8080/api/v1/departamentos \
  -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{"clave":"FIN","nombre":"Finanzas 2"}'
```

Esperado:
- `409 Conflict`

## 6) Eliminar departamento con empleados asociados

1. Crear un empleado referenciando el departamento creado (usa el `id` devuelto por el paso 2):

```bash
curl -i -X POST http://localhost:8080/api/v1/empleados \
  -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Ana","direccion":"Calle 1","telefono":"12345","departamentoId":2,"email":"ana@example.com","password":"secret123"}'
```

2. Intentar eliminar ese departamento:

```bash
curl -i -X DELETE http://localhost:8080/api/v1/departamentos/2 -u admin:admin123
```

Esperado:
- `409 Conflict` cuando tenga empleados asociados

## 7) Swagger/OpenAPI checks
- Swagger UI: `http://localhost:8080/api/v1/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/v1/v3/api-docs`
- Verificar endpoints de `/api/v1/departamentos` con respuestas `400/404/409`.

## 8) Stop runtime

```bash
cd /home/roberto/Despliegue_Software/DSW-Practica02/DSW02-Practica01
docker compose down
```

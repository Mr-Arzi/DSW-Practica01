# Quickstart: Employee Authentication via Email & Password

## Prerequisites
- Docker + Docker Compose
- Java 17 + Maven (optional for local run)

## 1) Start runtime

```bash
cd /home/roberto/Despliegue_Software/DSW-Practica02/DSW02-Practica01
docker compose up -d --build
```

## 2) Validate health

```bash
cd /home/roberto/Despliegue_Software/DSW-Practica02/DSW02-Practica01
docker compose ps
```

## 3) Exercise login endpoint

```bash
curl -i -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"empleado@empresa.com","password":"SecurePass123"}'
```

Expected success:
- Status: `200 OK`
- Body: `{"message":"Authentication successful"}`

## 4) Validate invalid credentials behavior

```bash
curl -i -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"empleado@empresa.com","password":"wrong"}'
```

Expected failure:
- Status: `401 Unauthorized`
- Body: `{"error":"Invalid email or password"}`

## 5) Verify protected endpoint still requires HTTP Basic

```bash
curl -i http://localhost:8080/api/v1/empleados
```

Expected:
- Status: `401 Unauthorized`

## 6) Swagger/OpenAPI checks
- Swagger UI: `http://localhost:8080/api/v1/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/v1/v3/api-docs`
- Confirm `POST /api/v1/auth/login` appears with request and response examples.

## 7) Stop runtime

```bash
cd /home/roberto/Despliegue_Software/DSW-Practica02/DSW02-Practica01
docker compose down
```

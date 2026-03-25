# Quickstart: Frontend Angular con Pruebas E2E

## Prerequisites
- Node.js 20+
- npm 10+
- Docker + Docker Compose
- Backend Spring Boot disponible en `http://localhost:8080`

## 1) Levantar backend existente

```bash
cd /home/roberto/Despliegue_Software/DSW-Practica02/DSW02-Practica01
docker compose up -d --build
```

Verificar:

```bash
curl -i http://localhost:8080/api/v1/empleados -u admin:admin123
```

Verificar contenedores y dependencia PostgreSQL:

```bash
docker compose ps
docker exec -it empleados-postgres psql -U empleados_user -d empleados -c "SELECT 1;"
```

## 2) Crear frontend Angular (si no existe)

```bash
cd /home/roberto/Despliegue_Software/DSW-Practica02
npx -y @angular/cli@18 new frontend --routing --style=scss --strict
cd frontend
npm install
```

## 3) Configurar base API

- Definir entorno frontend con base URL `http://localhost:8080/api/v1`.
- Implementar interceptor HTTP para `Authorization: Basic ...` usando sesion en `sessionStorage`.

## 4) Ejecutar frontend

```bash
cd /home/roberto/Despliegue_Software/DSW-Practica02/frontend
npm start
```

Acceso esperado:
- Login: `http://localhost:4200/login`
- Empleados: `http://localhost:4200/empleados`
- Departamentos: `http://localhost:4200/departamentos`

## 5) Ejecutar pruebas E2E con Cypress

```bash
cd /home/roberto/Despliegue_Software/DSW-Practica02/frontend
npm run cypress:open
# o en headless
npm run cypress:run
```

Evidencia esperada:
- Screenshots en `frontend/cypress/results/screenshots/`.
- Resultado de run en consola con resumen de tests.

Flujos minimos a validar:
- Login exitoso
- Login fallido
- Navegacion entre modulos
- Listado y creacion de empleados
- Listado y creacion de departamentos

## 6) Logout esperado

- Accion de logout en menu.
- Limpia `sessionStorage`.
- Redirige a `/login`.

## 7) Performance checks (FR-020 / FR-021)

- Navegacion entre `/empleados` y `/departamentos`: <= 1s.
- Render inicial de listados: <= 2s.

Metodo de medicion:
1. Abrir Chrome DevTools > Performance.
2. Grabar desde click en menu hasta primer render completo del modulo destino.
3. Repetir 3 veces por flujo y registrar promedio.

## 8) Detener backend

```bash
cd /home/roberto/Despliegue_Software/DSW-Practica02/DSW02-Practica01
docker compose down
```

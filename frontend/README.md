# Frontend Angular

## Scripts

- `npm start`: inicia frontend en `http://localhost:4200`.
- `npm run build`: compila build de produccion.
- `npm run test`: ejecuta pruebas unitarias.
- `npm run lint`: ejecuta ESLint.
- `npm run cypress:open`: abre Cypress interactivo.
- `npm run cypress:run`: ejecuta suite E2E headless.

## Requisitos de runtime

1. Levantar backend y PostgreSQL desde `DSW02-Practica01`:

```bash
docker compose up -d
```

2. Verificar backend y DB activos:

```bash
docker compose ps
curl -i http://localhost:8080/api/v1/empleados -u admin:admin123
```

3. Verificar conectividad PostgreSQL (desde contenedor):

```bash
docker exec -it empleados-postgres psql -U empleados_user -d empleados -c "SELECT 1;"
```

## Performance checks (manual)

- Navegacion protegida (`/empleados` <-> `/departamentos`) <= 1s.
- Render inicial de listados <= 2s.

Metodo sugerido:

1. Abrir Chrome DevTools > Performance.
2. Medir click sobre menu y tiempo hasta paint de pantalla destino.
3. Repetir 3 veces y registrar promedio en `specs/004-angular-frontend-e2e/quickstart.md`.

## Nota E2E

La suite Cypress ya genera evidencia en `frontend/cypress/results/`.
Si no hay servidor frontend o backend levantado, la suite falla por conectividad.

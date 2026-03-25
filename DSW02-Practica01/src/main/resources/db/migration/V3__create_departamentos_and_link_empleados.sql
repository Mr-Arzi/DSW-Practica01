CREATE TABLE IF NOT EXISTS departamentos (
    id BIGSERIAL PRIMARY KEY,
    clave VARCHAR(50) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_departamentos_clave
    ON departamentos (clave);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'chk_departamentos_clave_upper'
    ) THEN
        ALTER TABLE departamentos
            ADD CONSTRAINT chk_departamentos_clave_upper
            CHECK (clave = UPPER(clave));
    END IF;
END
$$;

INSERT INTO departamentos (clave, nombre)
VALUES ('SIN_ASIGNAR', 'Sin Asignar')
ON CONFLICT (clave) DO UPDATE
SET nombre = EXCLUDED.nombre;

ALTER TABLE empleados
    ADD COLUMN IF NOT EXISTS departamento_id BIGINT;

UPDATE empleados e
SET departamento_id = d.id
FROM departamentos d
WHERE d.clave = 'SIN_ASIGNAR'
  AND e.departamento_id IS NULL;

ALTER TABLE empleados
    ALTER COLUMN departamento_id SET NOT NULL;

CREATE INDEX IF NOT EXISTS idx_empleados_departamento_id
    ON empleados (departamento_id);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_empleados_departamento'
    ) THEN
        ALTER TABLE empleados
            ADD CONSTRAINT fk_empleados_departamento
            FOREIGN KEY (departamento_id)
            REFERENCES departamentos (id);
    END IF;
END
$$;
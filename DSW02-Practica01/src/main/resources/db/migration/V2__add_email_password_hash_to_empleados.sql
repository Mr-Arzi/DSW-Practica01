ALTER TABLE empleados
    ADD COLUMN IF NOT EXISTS email VARCHAR(255),
    ADD COLUMN IF NOT EXISTS password_hash VARCHAR(255);

CREATE UNIQUE INDEX IF NOT EXISTS ux_empleados_email
    ON empleados (email)
    WHERE email IS NOT NULL;

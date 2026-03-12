CREATE SEQUENCE IF NOT EXISTS empleado_numero_clave_seq START 1 INCREMENT 1;

CREATE TABLE IF NOT EXISTS empleados (
    prefijo_clave VARCHAR(4) NOT NULL,
    numero_clave BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(100) NOT NULL,
    telefono VARCHAR(100) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (prefijo_clave, numero_clave),
    CONSTRAINT chk_prefijo_clave CHECK (prefijo_clave = 'EMP-'),
    CONSTRAINT chk_telefono CHECK (telefono ~ '^[0-9]{1,100}$')
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_empleados_clave_derivada
    ON empleados ((prefijo_clave || numero_clave::TEXT));

CREATE INDEX IF NOT EXISTS idx_empleados_activo_clave
    ON empleados (activo, prefijo_clave, numero_clave);

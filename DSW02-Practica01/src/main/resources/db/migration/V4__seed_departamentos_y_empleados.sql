INSERT INTO departamentos (clave, nombre)
VALUES
    ('RH', 'Recursos Humanos'),
    ('TI', 'Tecnologia de Informacion'),
    ('FIN', 'Finanzas'),
    ('OPE', 'Operaciones'),
    ('MKT', 'Marketing')
ON CONFLICT (clave) DO UPDATE
SET nombre = EXCLUDED.nombre;

INSERT INTO empleados (
    prefijo_clave,
    numero_clave,
    nombre,
    direccion,
    telefono,
    activo,
    email,
    password_hash,
    departamento_id
)
VALUES
    ('EMP-', 1001, 'Ana Perez', 'Av. Norte 100', '555100001', TRUE, 'ana.perez@example.com', NULL, (SELECT id FROM departamentos WHERE clave = 'RH')),
    ('EMP-', 1002, 'Luis Gomez', 'Av. Sur 101', '555100002', TRUE, 'luis.gomez@example.com', NULL, (SELECT id FROM departamentos WHERE clave = 'TI')),
    ('EMP-', 1003, 'Marta Ruiz', 'Av. Este 102', '555100003', TRUE, 'marta.ruiz@example.com', NULL, (SELECT id FROM departamentos WHERE clave = 'FIN')),
    ('EMP-', 1004, 'Carlos Diaz', 'Av. Oeste 103', '555100004', TRUE, 'carlos.diaz@example.com', NULL, (SELECT id FROM departamentos WHERE clave = 'OPE')),
    ('EMP-', 1005, 'Sofia Torres', 'Calle Central 104', '555100005', TRUE, 'sofia.torres@example.com', NULL, (SELECT id FROM departamentos WHERE clave = 'MKT')),
    ('EMP-', 1006, 'Jorge Castillo', 'Calle Sol 105', '555100006', TRUE, 'jorge.castillo@example.com', NULL, (SELECT id FROM departamentos WHERE clave = 'RH')),
    ('EMP-', 1007, 'Elena Vargas', 'Calle Luna 106', '555100007', TRUE, 'elena.vargas@example.com', NULL, (SELECT id FROM departamentos WHERE clave = 'TI')),
    ('EMP-', 1008, 'Ricardo Mena', 'Calle Mar 107', '555100008', TRUE, 'ricardo.mena@example.com', NULL, (SELECT id FROM departamentos WHERE clave = 'FIN')),
    ('EMP-', 1009, 'Paula Rios', 'Calle Rio 108', '555100009', TRUE, 'paula.rios@example.com', NULL, (SELECT id FROM departamentos WHERE clave = 'OPE')),
    ('EMP-', 1010, 'Diego Leon', 'Calle Bosque 109', '555100010', TRUE, 'diego.leon@example.com', NULL, (SELECT id FROM departamentos WHERE clave = 'MKT'))
ON CONFLICT (email) WHERE email IS NOT NULL DO UPDATE
SET
    nombre = EXCLUDED.nombre,
    direccion = EXCLUDED.direccion,
    telefono = EXCLUDED.telefono,
    activo = EXCLUDED.activo,
    departamento_id = EXCLUDED.departamento_id,
    updated_at = CURRENT_TIMESTAMP;

SELECT setval(
    'empleado_numero_clave_seq',
    (SELECT COALESCE(MAX(numero_clave), 1) FROM empleados),
    true
);

-- H2 existente: conservar filas y admitir el nuevo estado final.
-- En una base nueva JPA crea la tabla con el enum actualizado.
ALTER TABLE IF EXISTS salidas_programadas
    ALTER COLUMN estado ENUM('CANCELADA', 'COMPLETADA', 'EN_CURSO', 'NO_REALIZADA', 'PROGRAMADA') NOT NULL;

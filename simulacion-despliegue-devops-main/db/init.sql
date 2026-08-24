-- Esquema de la base de datos "calculator".
-- IMPORTANTE: los nombres de columna deben coincidir EXACTAMENTE con los de
-- la entidad JPA com.example.entity.Operacion. Hibernate arranca con
-- ddl-auto=validate, por lo que cualquier desincronizacion aborta el backend
-- al iniciar (en vez de fallar en runtime con un 500 por cada operacion).
CREATE TABLE IF NOT EXISTS operaciones (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    tipo_operacion VARCHAR(255) NOT NULL,
    operando_a     DOUBLE       NOT NULL,
    operando_b     DOUBLE       NOT NULL,
    resultado      DOUBLE       NOT NULL,
    fecha_creacion DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    oauth2_provider VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    foto VARCHAR(500),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_usuario_oauth2_provider ON usuario(oauth2_provider);

CREATE TABLE categoria (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(1000),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_categoria_nombre ON categoria(nombre);

CREATE TABLE producto (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio NUMERIC(10, 2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    categoria_id BIGINT NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON DELETE RESTRICT
);

CREATE INDEX idx_producto_categoria_id ON producto(categoria_id);
CREATE INDEX idx_producto_nombre ON producto(nombre);
CREATE INDEX idx_producto_stock ON producto(stock);

CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    tipo_entidad VARCHAR(50) NOT NULL,
    id_entidad BIGINT NOT NULL,
    operacion VARCHAR(20) NOT NULL,
    usuario_email VARCHAR(255),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    datos_antes TEXT,
    datos_despues TEXT,
    descripcion VARCHAR(1000),
    replicado_firebase BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_audit_log_tipo_entidad ON audit_log(tipo_entidad);
CREATE INDEX idx_audit_log_usuario_email ON audit_log(usuario_email);
CREATE INDEX idx_audit_log_timestamp ON audit_log(timestamp);
CREATE INDEX idx_audit_log_replicado_firebase ON audit_log(replicado_firebase);


INSERT INTO categoria (nombre, descripcion, fecha_creacion, fecha_actualizacion) VALUES
('Electrónica', 'Productos electrónicos y gadgets', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ropa', 'Prendas de vestir y accesorios', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Libros', 'Libros físicos y digitales', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Deportes', 'Artículos deportivos y fitness', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Hogar', 'Artículos para el hogar', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO producto (nombre, descripcion, precio, stock, categoria_id, fecha_creacion, fecha_actualizacion) VALUES
('Laptop Dell XPS 13', 'Laptop ultradelgada con procesador Intel Core i7', 1299.99, 5, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Monitor Samsung 27"', 'Monitor 4K con HDR', 399.99, 8, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Teclado Mecánico RGB', 'Teclado mecánico con iluminación RGB', 129.99, 15, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Auriculares Sony WH-1000XM4', 'Auriculares con cancelación de ruido', 349.99, 10, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Webcam Logitech 4K', 'Cámara web 4K para videoconferencias', 79.99, 20, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Camiseta de Algodón', 'Camiseta básica de algodón 100%', 19.99, 50, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jeans Premium', 'Pantalones jeans de calidad premium', 59.99, 30, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Zapatillas Nike', 'Zapatillas deportivas Nike Air Max', 129.99, 25, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Gorro de Lana', 'Gorro de lana para invierno', 24.99, 40, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Chaqueta de Cuero', 'Chaqueta de cuero genuino', 199.99, 10, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('El Quijote de Cervantes', 'Novela clásica de la literatura española', 29.99, 15, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('1984 de George Orwell', 'Novela de ciencia ficción distópica', 19.99, 20, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('El Principito', 'Cuento juvenil de Antoine de Saint-Exupéry', 14.99, 35, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Harry Potter y la Piedra Filosofal', 'Primera novela de la saga Harry Potter', 24.99, 25, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Cien Años de Soledad', 'Novela del realismo mágico de García Márquez', 34.99, 12, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mancuernas Ajustables', 'Set de mancuernas con pesos ajustables', 89.99, 8, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tapete de Yoga', 'Tapete antideslizante para yoga y pilates', 49.99, 20, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Banda Elástica de Resistencia', 'Set de 5 bandas elásticas', 29.99, 30, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Pelota de Fútbol', 'Balón oficial tamaño 5', 34.99, 15, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Cuerda para Saltar', 'Cuerda de saltar ajustable', 19.99, 25, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Lámpara LED de Escritorio', 'Lámpara LED con 3 modos de luz', 39.99, 18, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Almohada de Memory Foam', 'Almohada ergonómica de memory foam', 59.99, 22, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Cortinas Blackout', 'Cortinas oscurecentes para dormitorio', 44.99, 14, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Organizador de Escritorio', 'Set de organizadores para escritorio', 29.99, 30, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Espejo de Pared', 'Espejo decorativo para pared', 69.99, 10, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


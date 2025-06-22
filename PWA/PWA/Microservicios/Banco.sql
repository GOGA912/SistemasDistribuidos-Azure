BEGIN;

-- Tabla Clientes
CREATE TABLE IF NOT EXISTS "Clientes" (
    "id" SERIAL PRIMARY KEY,
    "nombre" TEXT NOT NULL,
    "correo" TEXT NOT NULL,
    "direccion" TEXT NOT NULL,
    "sexo" TEXT
);

-- Tabla Cuentas
CREATE TABLE IF NOT EXISTS "Cuentas" (
    "numero" TEXT PRIMARY KEY,
    "titular_id" INTEGER NOT NULL,
    "saldo" REAL NOT NULL,
    "nip" INTEGER NOT NULL,
    "estado" INTEGER NOT NULL,
    FOREIGN KEY("titular_id") REFERENCES "Clientes"("id")
);

-- Tabla Movimientos
CREATE TABLE IF NOT EXISTS "Movimientos" (
    "id" SERIAL PRIMARY KEY,
    "cuenta" TEXT NOT NULL,
    "tipo" TEXT NOT NULL,
    "monto" REAL NOT NULL,
    "fecha" TEXT NOT NULL,
    FOREIGN KEY("cuenta") REFERENCES "Cuentas"("numero")
);

-- Tabla Transferencias
CREATE TABLE IF NOT EXISTS "Transferencias" (
    "id" SERIAL PRIMARY KEY,
    "cuenta_origen" TEXT NOT NULL,
    "cuenta_destino" TEXT NOT NULL,
    "monto" REAL NOT NULL,
    "fecha" TEXT NOT NULL,
    FOREIGN KEY("cuenta_origen") REFERENCES "Cuentas"("numero"),
    FOREIGN KEY("cuenta_destino") REFERENCES "Cuentas"("numero")
);

-- Insertar Clientes
INSERT INTO "Clientes" (id, nombre, correo, direccion, sexo) VALUES
(1,'Juan Perez','juan.perez@email.com','Calle Reforma #123','H'),
(2,'Jesus Gonzalez','jesus.gonzalez@email.com','Av. Insurgentes #456','H'),
(3,'Santiago Muñoz','santiago.munoz@email.com','Col. Roma Norte #789','H'),
(4,'Rodrigo Peralta','rodrigo.peralta@email.com','Calle Juárez #101','H'),
(5,'Ernesto Caballero','ernesto.caballero@email.com','Blvd. Independencia #202','H');

-- Insertar Cuentas
INSERT INTO "Cuentas" VALUES
('1234567890',1,10200.0,1234,1),
('2468024680',2,6350.0,2468,1),
('3692581470',3,6348.3,3692,1),
('4826048260',4,1.8,4826,1),
('6284062840',5,1000000.1,6284,1);

-- Insertar Movimientos (solo primeros 5 aquí como ejemplo, tú puedes pegar los demás)
INSERT INTO "Movimientos" (id, cuenta, tipo, monto, fecha) VALUES
(1,'1234567890','Deposito',1500.0,'2025-01-05'),
(2,'1234567890','Retiro',200.0,'2025-01-07'),
(3,'2468024680','Deposito',5000.0,'2025-01-10'),
(4,'3692581470','Retiro',500.0,'2025-01-12'),
(5,'4826048260','Deposito',3513.5,'2025-01-15');

-- Pega aquí el resto de los INSERTs de "Movimientos" y "Transferencias"
-- (puedes pegar los que ya tienes directamente, son válidos)

-- Insertar Transferencias
INSERT INTO "Transferencias" (id, cuenta_origen, cuenta_destino, monto, fecha) VALUES
(1,'1234567890','2468024680',500.0,'2024-01-10'),
(2,'3692581470','6284062840',1000.0,'2024-01-12'),
(3,'2468024680','4826048260',0.8,'2024-01-15'),
(4,'2468024680','1234567890',1000.0,'2025-04-17 04:24:25'),
(5,'1234567890','2468024680',1850.0,'2025-04-17 04:24:55'),
(6,'1234567890','2468024680',300.0,'2025-04-17 05:19:52'),
(7,'2468024680','1234567890',3000.0,'2025-04-17 05:19:52'),
(8,'1234567890','2468024680',1000.0,'2025-05-08 05:33:38'),
(9,'2468024680','1234567890',2000.0,'2025-05-08 05:34:05');

COMMIT;

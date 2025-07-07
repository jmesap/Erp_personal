-- Creación de la base de datos
CREATE DATABASE BD_ERP_Josemesa;
USE BD_ERP_Josemesa;

-- Creación de la tabla Usuario
CREATE TABLE Usuario (
    UsuarioID INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Contraseña VARCHAR(255) NOT NULL,
    Rol ENUM('Administrador', 'Empleado') NOT NULL
);

-- Creación de la tabla Cliente
CREATE TABLE Cliente (
    ClienteID INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Teléfono VARCHAR(15),
    Dirección VARCHAR(255)
);

-- Creación de la tabla Proyecto
CREATE TABLE Proyecto (
    ProyectoID INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Tipo ENUM('Desarrollo', 'Traducción') NOT NULL,
    FechaInicio DATE NOT NULL,
    FechaFin DATE,
    ClienteID INT,
    FOREIGN KEY (ClienteID) REFERENCES Cliente(ClienteID)
);

-- Creación de la tabla Presupuesto
CREATE TABLE Presupuesto (
    PresupuestoID INT AUTO_INCREMENT PRIMARY KEY,
    ProyectoID INT NOT NULL,
    ClienteID INT NOT NULL,
    TotalPresupuesto DECIMAL(10, 2) NOT NULL,
    FechaCreacion DATE NOT NULL,
    Estado ENUM('Pendiente', 'Aprobado', 'Rechazado') NOT NULL,
    FOREIGN KEY (ProyectoID) REFERENCES Proyecto(ProyectoID),
    FOREIGN KEY (ClienteID) REFERENCES Cliente(ClienteID)
);

-- Creación de la tabla Factura
CREATE TABLE Factura (
    FacturaID INT AUTO_INCREMENT PRIMARY KEY,
    ProyectoID INT NOT NULL,
    ClienteID INT NOT NULL,
    TotalFactura DECIMAL(10, 2) NOT NULL,
    FechaEmision DATE NOT NULL,
    Estado ENUM('Pendiente', 'Pagada', 'Cancelada') NOT NULL,
    FOREIGN KEY (ProyectoID) REFERENCES Proyecto(ProyectoID),
    FOREIGN KEY (ClienteID) REFERENCES Cliente(ClienteID)
);

-- Creación de la tabla Informe
CREATE TABLE Informe (
    InformeID INT AUTO_INCREMENT PRIMARY KEY,
    ProyectoID INT NOT NULL,
    FechaGeneracion DATE NOT NULL,
    TipoInforme ENUM('Avance', 'Final', 'Otro') NOT NULL,
    Contenido TEXT,
    FOREIGN KEY (ProyectoID) REFERENCES Proyecto(ProyectoID)
);
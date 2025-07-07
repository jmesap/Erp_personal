USE BD_ERP_Josemesa;

-- Inserción de datos en la tabla Usuario
INSERT INTO Usuario (Nombre, Email, Contraseña, Rol) VALUES 
('Admin Jose', 'admin@empresa.com', 'admin123', 'Administrador'),
('Empleado Ana', 'ana@empresa.com', 'empleado123', 'Empleado');

-- Inserción de datos en la tabla Cliente
INSERT INTO Cliente (Nombre, Email, Teléfono, Dirección) VALUES 
('Cliente A', 'clienteA@correo.com', '123456789', 'Calle A, 123'),
('Cliente B', 'clienteB@correo.com', '987654321', 'Avenida B, 456'),
('Cliente C', 'clienteC@correo.com', '456123789', 'Calle C, 789');

-- Inserción de datos en la tabla Proyecto
INSERT INTO Proyecto (Nombre, Tipo, FechaInicio, FechaFin, ClienteID) VALUES 
('Desarrollo Web A', 'Desarrollo', '2024-01-10', '2024-03-15', 1),
('Traducción Manual B', 'Traducción', '2024-02-05', NULL, 2),
('App Móvil C', 'Desarrollo', '2024-03-01', '2024-06-01', 3);

-- Inserción de datos en la tabla Presupuesto
INSERT INTO Presupuesto (ProyectoID, ClienteID, TotalPresupuesto, FechaCreacion, Estado) VALUES 
(1, 1, 1200.50, '2024-01-15', 'Aprobado'),
(2, 2, 800.00, '2024-02-10', 'Pendiente'),
(3, 3, 2500.75, '2024-03-05', 'Rechazado');

-- Inserción de datos en la tabla Factura
INSERT INTO Factura (ProyectoID, ClienteID, TotalFactura, FechaEmision, Estado) VALUES 
(1, 1, 1200.50, '2024-03-20', 'Pagada'),
(2, 2, 800.00, '2024-02-20', 'Pendiente'),
(3, 3, 2500.75, '2024-06-05', 'Cancelada');

-- Inserción de datos en la tabla Informe
INSERT INTO Informe (ProyectoID, FechaGeneracion, TipoInforme, Contenido) VALUES 
(1, '2024-01-20', 'Avance', 'Primer avance del proyecto de desarrollo web.'),
(2, '2024-02-15', 'Final', 'Informe final del proyecto de traducción.'),
(3, '2024-04-10', 'Avance', 'Avance de abril para la aplicación móvil.');

-- Creación de usuario de base de datos y asignación de roles

-- Creación de usuario con permisos de administrador
CREATE USER 'admin_user'@'localhost' IDENTIFIED BY 'adminpassword';
GRANT ALL PRIVILEGES ON BD_ERP_Josemesa.* TO 'admin_user'@'localhost';

-- Creación de usuario con permisos de solo consulta
CREATE USER 'read_user'@'localhost' IDENTIFIED BY 'readpassword';
GRANT SELECT ON BD_ERP_Josemesa.* TO 'read_user'@'localhost';

-- Creación de usuario para el rol de empleado con permisos de inserción y actualización
CREATE USER 'employee_user'@'localhost' IDENTIFIED BY 'employeepassword';
GRANT SELECT, INSERT, UPDATE ON BD_ERP_Josemesa.* TO 'employee_user'@'localhost';

-- Aplicación de los cambios de privilegios
FLUSH PRIVILEGES;

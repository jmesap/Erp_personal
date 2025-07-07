# ERP_JoseMesaPadilla

Sistema ERP (Enterprise Resource Planning) desarrollado en Java, diseñado para facilitar la gestión integral de una empresa. Este proyecto implementa una arquitectura MVC (Modelo-Vista-Controlador) para organizar el código y mejorar la mantenibilidad.

## Características principales

- Gestión de clientes, facturas, proyectos, presupuestos e informes.
- Interfaz gráfica desarrollada con Swing para una experiencia de usuario amigable.
- Conexión a base de datos MySQL para almacenamiento y consulta de datos.
- Generación automática de documentos PDF para presupuestos y facturas.
- Control de usuarios y autenticación mediante sistema de login.
- Uso de librerías externas para mejorar funcionalidades (PDFBox, FlatLaf, MySQL Connector).

## Estructura del proyecto

- `controller/`: Controladores que gestionan la lógica de negocio y la interacción entre la vista y el modelo.
- `dao/`: Objetos de acceso a datos para interactuar con la base de datos.
- `model/`: Clases que representan las entidades del sistema (Cliente, Factura, Proyecto, etc.).
- `view/`: Interfaces gráficas y formularios Swing.
- `util/`: Utilidades como conexión a base de datos y generación de PDFs.
- `bbdd/`: Scripts SQL para creación y carga inicial de la base de datos.
- `librerias/`: Librerías externas necesarias para el funcionamiento del proyecto.
- `imagenes/`: Recursos gráficos usados en la interfaz.

## Requisitos

- Java JDK 8 o superior.
- MySQL Server para la base de datos.
- IDE recomendado: NetBeans (configurado para proyectos Java con Ant).
- Librerías externas incluidas en la carpeta `librerias/`.

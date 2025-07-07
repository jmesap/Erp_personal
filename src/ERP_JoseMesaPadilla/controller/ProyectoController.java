package ERP_JoseMesaPadilla.controller;

import ERP_JoseMesaPadilla.dao.ClienteDAO;
import ERP_JoseMesaPadilla.model.Cliente;
import ERP_JoseMesaPadilla.model.Proyecto;
import ERP_JoseMesaPadilla.view.ProyectoView;
import ERP_JoseMesaPadilla.dao.ProyectoDAO;

import javax.swing.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProyectoController {

    private ProyectoView view;
    private Proyecto proyectoModelo;  // Ahora tenemos el modelo de tipo Proyecto
    private ProyectoDAO proyectoDAO;
    private ClienteDAO clienteDAO;  // Crear una instancia de ClienteDAO


    public ProyectoController(ProyectoView view, Proyecto proyectoModelo) {
        this.view = view;
        this.proyectoModelo = proyectoModelo;  // Inicializamos el modelo
        this.proyectoDAO = new ProyectoDAO(); // Instanciamos el DAO
                this.clienteDAO = new ClienteDAO(); // Instanciamos el DAO

        initController();
    }

    private void initController() {
        // Agregar listeners a los botones
        view.getBtnAgregar().addActionListener(e -> {
            try {
                agregarProyecto();
            } catch (SQLException ex) {
                Logger.getLogger(ProyectoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        view.getBtnEditar().addActionListener(e -> prepararEdicionProyecto());
        view.getBtnEliminar().addActionListener(e -> eliminarProyecto());

        cargarClientesEnComboBox(); // Llenar ComboBox con clientes al iniciar
        cargarProyectosEnTabla(); // Cargar proyectos en la tabla
    }

    private void cargarClientesEnComboBox() {
        try {
            List<Cliente> clientes = Cliente.obtenerClientes(); // Obtener clientes de la base de datos
            JComboBox<Cliente> cbClientes = view.getCbClientes();
            cbClientes.removeAllItems(); // Limpiar ComboBox

            for (Cliente cliente : clientes) {
                cbClientes.addItem(cliente);  // Agregar el objeto Cliente al JComboBox
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error al cargar clientes: " + e.getMessage());
        }
    }

public void cargarProyectosEnTabla() {
    try {
        List<Proyecto> proyectos = proyectoDAO.obtenerProyectos(); // Obtener proyectos de la base de datos
        var modeloTabla = view.getModeloTabla();
        modeloTabla.setRowCount(0); // Limpiar tabla

        for (Proyecto proyecto : proyectos) {
            // Obtener el nombre del cliente usando el clienteId
            String nombreCliente = clienteDAO.obtenerNombreClientePorId(proyecto.getClienteId());
            
            // Si no se encuentra el cliente, asignar un valor por defecto (opcional)
            if (nombreCliente == null) {
                nombreCliente = "Cliente no encontrado";
            }

            modeloTabla.addRow(new Object[]{
                proyecto.getProyectoId(),
                proyecto.getNombreProyecto(),
                proyecto.getTipo(),
                proyecto.getFechaInicio(),
                proyecto.getFechaFin(),
                proyecto.getClienteId() + " - " + nombreCliente // Mostrar clienteId y nombre
            });
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(view, "Error al cargar proyectos: " + e.getMessage());
    }
}

    private void agregarProyecto() throws SQLException {
        String nombre = view.getTxtNombre().getText();
        String tipo = (String) view.getCbTipo().getSelectedItem();
        String fechaInicioStr = view.getTxtFechaInicio().getText();
        String fechaFinStr = view.getTxtFechaFin().getText();

        // Obtener el cliente seleccionado desde el JComboBox
        Cliente clienteSeleccionado = (Cliente) view.getCbClientes().getSelectedItem();

        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar un cliente.");
            return;  // Salir si no se ha seleccionado un cliente
        }

        int clienteId = clienteSeleccionado.getClienteId();  // Obtener el ID del cliente

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // Parsear las fechas
            java.util.Date fechaInicioUtil = dateFormat.parse(fechaInicioStr);
            java.util.Date fechaFinUtil = dateFormat.parse(fechaFinStr);

            java.sql.Date fechaInicio = new java.sql.Date(fechaInicioUtil.getTime());
            java.sql.Date fechaFin = new java.sql.Date(fechaFinUtil.getTime());

            // Crear el nuevo proyecto con los datos
            Proyecto nuevoProyecto = new Proyecto(nombre, tipo, fechaInicio, fechaFin, clienteId);

            // Llamar al método de DAO para agregar el proyecto
            boolean agregado = proyectoDAO.agregarProyecto(nuevoProyecto);
            if (agregado) {
                cargarProyectosEnTabla();  // Actualizar la tabla con el nuevo proyecto
                JOptionPane.showMessageDialog(view, "Proyecto agregado correctamente");
            } else {
                JOptionPane.showMessageDialog(view, "Error al agregar proyecto.");
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(view, "Formato de fecha inválido. Use 'yyyy-MM-dd'");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error al agregar proyecto: " + e.getMessage());
        }
    }

    private void prepararEdicionProyecto() {
        int row = view.getTablaProyectos().getSelectedRow();
        if (row != -1) {
            // Obtener los datos del proyecto desde la fila seleccionada
            int id = (int) view.getModeloTabla().getValueAt(row, 0);
            String nombre = (String) view.getModeloTabla().getValueAt(row, 1);
            String tipo = (String) view.getModeloTabla().getValueAt(row, 2);
            Date fechaInicio = (Date) view.getModeloTabla().getValueAt(row, 3);
            Date fechaFin = (Date) view.getModeloTabla().getValueAt(row, 4);
            String clienteInfo = (String) view.getModeloTabla().getValueAt(row, 5);

            // Establecer los valores en los campos de texto y JComboBox
            view.getTxtNombre().setText(nombre);
            view.getCbTipo().setSelectedItem(tipo);

            // Convertir las fechas de Date a String para mostrarlas correctamente en los campos
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            view.getTxtFechaInicio().setText(dateFormat.format(fechaInicio));
            view.getTxtFechaFin().setText(dateFormat.format(fechaFin));

            // Aquí es necesario extraer el ID del cliente del string "clienteInfo", que tiene el formato "id - nombre"
            int clienteId = Integer.parseInt(clienteInfo.split(" - ")[0]);

            // Buscar el cliente correspondiente en el JComboBox y seleccionarlo
            JComboBox<Cliente> cbClientes = view.getCbClientes();
            for (int i = 0; i < cbClientes.getItemCount(); i++) {
                Cliente cliente = cbClientes.getItemAt(i);
                if (cliente.getClienteId() == clienteId) {
                    cbClientes.setSelectedItem(cliente);  // Seleccionar el cliente en el ComboBox
                    break;
                }
            }

            // Preparar el botón para guardar los cambios del proyecto editado
            view.getBtnGuardarCambios().addActionListener(e -> editarProyecto());
        } else {
            JOptionPane.showMessageDialog(view, "Por favor, selecciona un proyecto para editar.");
        }
    }

    private void editarProyecto() {
        int row = view.getTablaProyectos().getSelectedRow();
        if (row != -1) {
            try {
                // Obtener los datos del proyecto seleccionado
                int id = (int) view.getModeloTabla().getValueAt(row, 0);
                String nombre = view.getTxtNombre().getText();
                String tipo = (String) view.getCbTipo().getSelectedItem();  // Obtener el tipo del JComboBox

                // Obtener las fechas desde los campos de texto
                String fechaInicioStr = view.getTxtFechaInicio().getText();
                String fechaFinStr = view.getTxtFechaFin().getText();

                // Verificar si las fechas están vacías antes de intentar el parseo
                if (fechaInicioStr == null || fechaInicioStr.isEmpty() || fechaFinStr == null || fechaFinStr.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Las fechas no pueden estar vacías.");
                    return;  // Salir si alguna fecha está vacía
                }

                // Usar DateTimeFormatter para parsear la fecha
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                LocalDate fechaInicio = null;
                LocalDate fechaFin = null;

                try {
                    // Intentar parsear las fechas
                    fechaInicio = LocalDate.parse(fechaInicioStr, dateFormat);
                    fechaFin = LocalDate.parse(fechaFinStr, dateFormat);
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(view, "Las fechas deben tener el formato 'yyyy-MM-dd'.");
                    return;  // Salir si las fechas no son válidas
                }

                // Asegúrate de que las fechas no sean null antes de intentar convertirlas a java.sql.Date
                if (fechaInicio == null || fechaFin == null) {
                    JOptionPane.showMessageDialog(view, "Las fechas no son válidas.");
                    return;
                }

                // Convertir las fechas de LocalDate a java.sql.Date
                java.sql.Date sqlFechaInicio = java.sql.Date.valueOf(fechaInicio);
                java.sql.Date sqlFechaFin = java.sql.Date.valueOf(fechaFin);

                // Obtener el cliente seleccionado desde el JComboBox
                Cliente clienteSeleccionado = (Cliente) view.getCbClientes().getSelectedItem();

                // Verificar si se ha seleccionado un cliente
                if (clienteSeleccionado == null) {
                    JOptionPane.showMessageDialog(view, "Debe seleccionar un cliente.");
                    return; // Salir del método si no se selecciona un cliente
                }

                int clienteId = clienteSeleccionado.getClienteId(); // Obtener el ID del cliente seleccionado

                // Crear el objeto Proyecto utilizando el constructor modificado
                Proyecto proyectoEditado = new Proyecto(id, nombre, tipo, sqlFechaInicio, sqlFechaFin, clienteId);

                // Intentar actualizar el proyecto en la base de datos usando el DAO
                boolean actualizado = proyectoDAO.editarProyecto(proyectoEditado);
                if (actualizado) {
                    JOptionPane.showMessageDialog(view, "Proyecto actualizado correctamente.");
                    cargarProyectosEnTabla(); // Actualizar la tabla con el nuevo proyecto
                } else {
                    JOptionPane.showMessageDialog(view, "Error al actualizar el proyecto.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Error al actualizar el proyecto: " + e.getMessage());
            }
        }
    }

    private void eliminarProyecto() {
        int row = view.getTablaProyectos().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Selecciona un proyecto para eliminar.");
            return;
        }

        int id = (int) view.getModeloTabla().getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(view, "¿Estás seguro de eliminar este proyecto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean eliminado = proyectoDAO.eliminarProyecto(id);
            if (eliminado) {
                cargarProyectosEnTabla();
                JOptionPane.showMessageDialog(view, "Proyecto eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(view, "Error al eliminar el proyecto.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error al eliminar el proyecto: " + e.getMessage());
        }
    }
}

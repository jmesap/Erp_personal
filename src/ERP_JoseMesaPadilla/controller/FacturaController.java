package ERP_JoseMesaPadilla.controller;

import ERP_JoseMesaPadilla.dao.FacturaDAO;
import ERP_JoseMesaPadilla.model.Cliente;
import ERP_JoseMesaPadilla.model.Factura;
import ERP_JoseMesaPadilla.model.Proyecto;
import ERP_JoseMesaPadilla.view.FacturaView;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.List;
import javax.swing.JOptionPane;

public final class FacturaController {

    private final FacturaView view;
    private final FacturaDAO dao;

    public FacturaController(FacturaView view, FacturaDAO dao) {
        this.view = view;
        this.dao = dao;

        // Asociar eventos de los botones
        view.getBtnCrear().addActionListener(this::crearFactura);
        view.getBtnFiltrar().addActionListener(this::filtrarFacturas);
        view.getBtnEditar().addActionListener(this::editarFactura);
        view.getBtnBorrar().addActionListener(this::borrarFactura);
        view.getBtnGuardarCambios().addActionListener(this::guardarCambiosFactura);
        //view.getBtnRetroceder().addActionListener(this::retroceder);

        // Escuchar cambios en el JComboBox de Cliente para actualizar los proyectos
        view.getCbCliente().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                actualizarProyectosPorCliente(); // Actualiza los proyectos cuando el cliente cambia
            }
        });

        // Cargar los datos iniciales
        cargarDatos();
        cargarFacturas();
    }

    /**
     * Cargar datos iniciales (Clientes y Proyectos).
     */
    public void cargarDatos() {
        try {
            // Cargar clientes en el JComboBox
            List<Cliente> clientes = dao.obtenerClientes();
            for (Cliente cliente : clientes) {
                view.getCbCliente().addItem(cliente);  // Aseguramos que estamos agregando el objeto Cliente
            }

            // Cargar proyectos en el JComboBox
            List<Proyecto> proyectos = dao.obtenerProyectos();
            for (Proyecto proyecto : proyectos) {
                view.getCbProyecto().addItem(proyecto);  // Aseguramos que estamos agregando el objeto Proyecto
            }
        } catch (Exception ex) {
            view.mostrarMensaje("Error al cargar los datos: " + ex.getMessage());
        }
    }

    /**
     * Crear una nueva factura.
     */
    private void crearFactura(ActionEvent e) {
        try {
            Proyecto proyectoSeleccionado = (Proyecto) view.getCbProyecto().getSelectedItem();
            Cliente clienteSeleccionado = (Cliente) view.getCbCliente().getSelectedItem();
            double totalFactura = Double.parseDouble(view.getTotalFactura().getText());
            String estado = (String) view.getCbEstado().getSelectedItem();

            if (proyectoSeleccionado == null || clienteSeleccionado == null) {
                view.mostrarMensaje("Por favor, selecciona un proyecto y un cliente.");
                return;
            }

            Factura nuevaFactura = new Factura(
                    0, // El ID será generado automáticamente
                    proyectoSeleccionado.getProyectoId(),
                    clienteSeleccionado.getClienteId(),
                    totalFactura,
                    new java.sql.Date(System.currentTimeMillis()), // Fecha actual
                    estado
            );

            // Insertar en la base de datos
            dao.insertarFactura(nuevaFactura);

            // Mostrar confirmación
            view.mostrarMensaje("Factura creada con éxito.");
            cargarFacturas();

        } catch (NumberFormatException ex) {
            view.mostrarMensaje("El total de la factura debe ser un número válido.");
        } catch (Exception ex) {
            view.mostrarMensaje("Error al crear la factura: " + ex.getMessage());
        }
    }

    /**
     * Filtrar las facturas según el filtro proporcionado.
     */
    private void filtrarFacturas(ActionEvent e) {
        try {
            String filtro = view.getFiltro(); // Obtener el valor de filtro desde la vista
            if (filtro.isEmpty()) {
                cargarFacturas(); // Si el filtro está vacío, cargar todas las facturas
                return;
            }

            view.mostrarFacturas(dao.filtrarFacturas("ClienteID", filtro)); // Filtrar las facturas por ClienteID
        } catch (Exception ex) {
            view.mostrarMensaje("Error al filtrar las facturas: " + ex.getMessage());
        }
    }

    /**
     * Cargar todas las facturas en la vista.
     */
    private void cargarFacturas() {
        try {
            List<Factura> facturas = dao.obtenerFacturas();
            view.mostrarFacturas(facturas); // Mostrar las facturas en la tabla
        } catch (Exception ex) {
            view.mostrarMensaje("Error al cargar las facturas: " + ex.getMessage());
        }
    }

    /**
     * Actualizar los proyectos disponibles según el cliente seleccionado.
     */
    public void actualizarProyectosPorCliente() {
        Cliente clienteSeleccionado = (Cliente) view.getCbCliente().getSelectedItem();
        if (clienteSeleccionado == null) {
            return;
        }

        int clienteId = clienteSeleccionado.getClienteId();
        List<Proyecto> proyectos = dao.obtenerProyectosPorCliente(clienteId);

        view.getCbProyecto().removeAllItems();

        if (proyectos.isEmpty()) {
            view.getCbProyecto().addItem(new Proyecto(-1, "No hay proyectos disponibles", null, null, null, 0));
        } else {
            for (Proyecto proyecto : proyectos) {
                view.getCbProyecto().addItem(proyecto);
            }
        }
    }

    /**
     * Editar una factura seleccionada en la tabla.
     */
    private void editarFactura(ActionEvent e) {
        int selectedRow = view.getTblFacturas().getSelectedRow();
        if (selectedRow >= 0) {
            int facturaId = (int) view.getTblFacturas().getValueAt(selectedRow, 0);
            Factura factura = dao.obtenerFacturaPorId(facturaId);

            if (factura != null) {
                view.getTotalFactura().setText(String.valueOf(factura.getTotalFactura()));
                view.getCbEstado().setSelectedItem(factura.getEstado());
                view.getCbProyecto().setSelectedItem(obtenerProyectoPorId(factura.getProyectoId()));
                view.getCbCliente().setSelectedItem(obtenerClientePorId(factura.getClienteId()));
            } else {
                view.mostrarMensaje("No se pudo cargar la factura seleccionada.");
            }
        } else {
            view.mostrarMensaje("Por favor, selecciona una factura para editar.");
        }
    }

    private Proyecto obtenerProyectoPorId(int proyectoId) {
        return dao.obtenerProyectos().stream()
                .filter(proyecto -> proyecto.getProyectoId() == proyectoId)
                .findFirst()
                .orElse(null);
    }

    private Cliente obtenerClientePorId(int clienteId) {
        return dao.obtenerClientes().stream()
                .filter(cliente -> cliente.getClienteId() == clienteId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Guardar los cambios en la factura editada.
     */
    private void guardarCambiosFactura(ActionEvent e) {
        int selectedRow = view.getTblFacturas().getSelectedRow();
        if (selectedRow >= 0) {
            int facturaId = (int) view.getTblFacturas().getValueAt(selectedRow, 0);
            Factura factura = dao.obtenerFacturaPorId(facturaId);

            if (factura != null) {
                try {
                    factura.setTotalFactura(Double.parseDouble(view.getTotalFactura().getText()));
                    factura.setEstado((String) view.getCbEstado().getSelectedItem());

                    Proyecto proyectoSeleccionado = (Proyecto) view.getCbProyecto().getSelectedItem();
                    factura.setProyectoId(proyectoSeleccionado.getProyectoId());

                    Cliente clienteSeleccionado = (Cliente) view.getCbCliente().getSelectedItem();
                    factura.setClienteId(clienteSeleccionado.getClienteId());

                    // Guardar los cambios en la base de datos
                    if (dao.actualizarFactura(factura)) {
                        view.mostrarMensaje("Factura actualizada con éxito.");
                        cargarFacturas();
                    } else {
                        view.mostrarMensaje("Error al actualizar la factura.");
                    }
                } catch (NumberFormatException ex) {
                    view.mostrarMensaje("Por favor, ingresa un valor numérico válido para el total.");
                }
            }
        }
    }

    /**
     * Borrar una factura seleccionada.
     */
    private void borrarFactura(ActionEvent e) {
        int selectedRow = view.getTblFacturas().getSelectedRow();
        if (selectedRow >= 0) {
            int facturaId = (int) view.getTblFacturas().getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(view, "¿Seguro que deseas borrar esta factura?");
            if (confirm == JOptionPane.YES_OPTION) {
                dao.eliminarFactura(facturaId);
                view.mostrarMensaje("Factura borrada correctamente.");
                cargarFacturas();
            }
        } else {
            view.mostrarMensaje("Por favor, selecciona una factura para borrar.");
        }
    }

    /**
     * Retroceder al menú anterior.
     */
    private void retroceder(ActionEvent e) {
        // Puedes implementar la lógica para ir al menú anterior
    }
}

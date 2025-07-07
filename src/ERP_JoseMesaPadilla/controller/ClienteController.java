package ERP_JoseMesaPadilla.controller;

import ERP_JoseMesaPadilla.model.Cliente;
import ERP_JoseMesaPadilla.view.ClienteView;
import javax.swing.JOptionPane;
import java.sql.SQLException;

public class ClienteController {
    private ClienteView view;
    private Cliente model;

    public ClienteController(ClienteView view, Cliente model) {
        this.view = view;
        this.model = model;

        initController();
    }

    private void initController() {
        view.getBtnAgregar().addActionListener(e -> agregarCliente());
        view.getBtnEditar().addActionListener(e -> prepararEdicionCliente());
        view.getBtnEliminar().addActionListener(e -> eliminarCliente());
        view.getBtnBuscar().addActionListener(e -> buscarClientePorNombre());
        cargarClientesEnTabla();
    }

    public void cargarClientesEnTabla() {
        try {
            var clientes = Cliente.obtenerClientes(); // Obtener clientes de la base de datos
            var modeloTabla = view.getModeloTabla();
            modeloTabla.setRowCount(0); // Limpiar tabla

            for (var cliente : clientes) {
                modeloTabla.addRow(new Object[]{
                        cliente.getClienteId(),
                        cliente.getNombre(),
                        cliente.getEmail(),
                        cliente.getTelefono(),
                        cliente.getDireccion()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error al cargar clientes: " + e.getMessage());
        }
    }

    private void agregarCliente() {
        String nombre = view.getTxtNombre().getText();
        String email = view.getTxtEmail().getText();
        String telefono = view.getTxtTelefono().getText();
        String direccion = view.getTxtDireccion().getText();

        if (nombre.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Por favor, llena los campos obligatorios (Nombre y Email).");
            return;
        }

        Cliente nuevoCliente = new Cliente(nombre, email, telefono, direccion);

        try {
            Cliente.agregarCliente(nuevoCliente); // Agregar el cliente a la base de datos
            cargarClientesEnTabla(); // Actualizar la tabla
            JOptionPane.showMessageDialog(view, "Cliente agregado correctamente");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error al agregar cliente: " + e.getMessage());
        }
    }

    private void prepararEdicionCliente() {
        int row = view.getTablaClientes().getSelectedRow();
        if (row != -1) {
            int id = (int) view.getModeloTabla().getValueAt(row, 0);
            String nombre = (String) view.getModeloTabla().getValueAt(row, 1);
            String email = (String) view.getModeloTabla().getValueAt(row, 2);
            String telefono = (String) view.getModeloTabla().getValueAt(row, 3);
            String direccion = (String) view.getModeloTabla().getValueAt(row, 4);

            view.getTxtNombre().setText(nombre);
            view.getTxtEmail().setText(email);
            view.getTxtTelefono().setText(telefono);
            view.getTxtDireccion().setText(direccion);

            view.getBtnGuardarCambios().addActionListener(e -> editarCliente(id));
        } else {
            JOptionPane.showMessageDialog(view, "Por favor, selecciona un cliente para editar.");
        }
    }

    private void editarCliente(int id) {
        String nombre = view.getTxtNombre().getText();
        String email = view.getTxtEmail().getText();
        String telefono = view.getTxtTelefono().getText();
        String direccion = view.getTxtDireccion().getText();

        if (nombre.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Por favor, llena los campos obligatorios (Nombre y Email).");
            return;
        }

        Cliente clienteAEditar = new Cliente(id, nombre, email, telefono, direccion);

        try {
            boolean actualizado = Cliente.editarCliente(clienteAEditar);
            if (actualizado) {
                JOptionPane.showMessageDialog(view, "Cliente actualizado correctamente");
                cargarClientesEnTabla(); // Actualizar tabla con los nuevos datos
            } else {
                JOptionPane.showMessageDialog(view, "Error al actualizar cliente");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error al actualizar cliente: " + e.getMessage());
        }
    }

    private void eliminarCliente() {
        int row = view.getTablaClientes().getSelectedRow();
        if (row != -1) {
            int id = (int) view.getModeloTabla().getValueAt(row, 0);

            int confirm = JOptionPane.showConfirmDialog(view, "¿Estás seguro de que deseas eliminar este cliente?");
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean eliminado = Cliente.eliminarCliente(id);
                    if (eliminado) {
                        JOptionPane.showMessageDialog(view, "Cliente eliminado correctamente");
                        cargarClientesEnTabla();
                    } else {
                        JOptionPane.showMessageDialog(view, "Error al eliminar cliente");
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(view, "Error al eliminar cliente: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(view, "Por favor, selecciona un cliente para eliminar.");
        }
    }

    private void buscarClientePorNombre() {
        String nombre = view.getTxtBuscar().getText();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Por favor, ingresa un nombre para buscar.");
            return;
        }

        try {
            var clientes = Cliente.buscarClientesPorNombre(nombre);
            var modeloTabla = view.getModeloTabla();
            modeloTabla.setRowCount(0); // Limpiar tabla

            for (var cliente : clientes) {
                modeloTabla.addRow(new Object[]{
                        cliente.getClienteId(),
                        cliente.getNombre(),
                        cliente.getEmail(),
                        cliente.getTelefono(),
                        cliente.getDireccion()
                });
            }

            if (clientes.isEmpty()) {
                JOptionPane.showMessageDialog(view, "No se encontraron clientes con el nombre proporcionado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error al buscar clientes: " + e.getMessage());
        }
    }
}

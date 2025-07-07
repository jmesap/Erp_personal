package ERP_JoseMesaPadilla.controller;

import ERP_JoseMesaPadilla.dao.PresupuestoDAO;
import ERP_JoseMesaPadilla.model.Cliente;
import ERP_JoseMesaPadilla.model.Presupuesto;
import ERP_JoseMesaPadilla.model.Proyecto;
import ERP_JoseMesaPadilla.util.GeneradorPDF;
import ERP_JoseMesaPadilla.view.PresupuestoView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class PresupuestoController {

    private final PresupuestoView view;
    private final PresupuestoDAO dao;

    public PresupuestoController(PresupuestoView view, PresupuestoDAO dao) {
        this.view = view;
        this.dao = dao;

        // Asociar eventos
        view.getBtnCrear().addActionListener(this::crearPresupuesto);
        view.getBtnFiltrar().addActionListener(this::filtrarPresupuestos);
        view.getBtnEditar().addActionListener(this::editarPresupuesto);
        view.getBtnBorrar().addActionListener(this::borrarPresupuesto);
        view.getBtnGuardarCambios().addActionListener(this::guardarCambiosPresupuesto);
        view.getBtnDescargarPDF().addActionListener(e -> {
            int selectedRow = view.getTblPresupuestos().getSelectedRow();
            if (selectedRow >= 0) {
                int presupuestoId = (int) view.getTblPresupuestos().getValueAt(selectedRow, 0);
                descargarPresupuestoPDF(presupuestoId);
            } else {
                view.mostrarMensaje("Por favor, selecciona un presupuesto para descargar.");
            }
        });

        // Escuchar cambios en el JComboBox de Cliente
        view.getCbCliente().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                actualizarProyectosPorCliente();
            }
        });

        cargarDatos();
        cargarPresupuestos();
    }

    /**
     * Método para cargar los datos iniciales (Clientes y Proyectos).
     */
    public void cargarDatos() {
        try {
            // Cargar clientes en el JComboBox
            List<Cliente> clientes = dao.obtenerClientes();
            for (Cliente cliente : clientes) {
                view.getCbCliente().addItem(cliente);
            }

            // Cargar proyectos en el JComboBox
            List<Proyecto> proyectos = dao.obtenerProyectos();
            for (Proyecto proyecto : proyectos) {
                view.getCbProyecto().addItem(proyecto);
            }

        } catch (Exception ex) {
            view.mostrarMensaje("Error al cargar los datos: " + ex.getMessage());
        }
    }

    private void guardarCambiosPresupuesto(ActionEvent e) {
        int selectedRow = view.tblPresupuestos.getSelectedRow();
        if (selectedRow >= 0) {
            // Obtener el ID del presupuesto desde la tabla
            int presupuestoId = (int) view.tblPresupuestos.getValueAt(selectedRow, 0);

            Presupuesto presupuesto = dao.obtenerPresupuestoPorId(presupuestoId);
            if (presupuesto != null) {
                try {
                    // Actualizar los datos del presupuesto
                    presupuesto.setTotalPresupuesto(Double.parseDouble(view.getTotalPresupuesto().getText()));
                    presupuesto.setEstado((String) view.getCbEstado().getSelectedItem());

                    Proyecto proyectoSeleccionado = (Proyecto) view.getCbProyecto().getSelectedItem();
                    presupuesto.setProyectoId(proyectoSeleccionado.getProyectoId());

                    Cliente clienteSeleccionado = (Cliente) view.getCbCliente().getSelectedItem();
                    presupuesto.setClienteId(clienteSeleccionado.getClienteId());

                    // Guardar los cambios en la base de datos
                    if (dao.actualizarPresupuesto(presupuesto)) {
                        view.mostrarMensaje("Presupuesto actualizado con éxito.");
                        cargarPresupuestos();

                    } else {
                        view.mostrarMensaje("Error al actualizar el presupuesto.");
                    }
                } catch (NumberFormatException ex) {
                    view.mostrarMensaje("Por favor, ingresa un valor numérico válido para el total.");
                }
            } else {
                view.mostrarMensaje("No se encontró el presupuesto para actualizar.");
            }
        }
    }

    /**
     * Método para crear un nuevo presupuesto.
     */
    private void crearPresupuesto(ActionEvent e) {
        try {
            Proyecto proyectoSeleccionado = (Proyecto) view.getCbProyecto().getSelectedItem();
            Cliente clienteSeleccionado = (Cliente) view.getCbCliente().getSelectedItem();
            double totalPresupuesto = Double.parseDouble(view.getTotalPresupuesto().getText());
            String estado = (String) view.getCbEstado().getSelectedItem();

            if (proyectoSeleccionado == null || clienteSeleccionado == null) {
                view.mostrarMensaje("Por favor, selecciona un proyecto y un cliente.");
                return;
            }

            Presupuesto nuevoPresupuesto = new Presupuesto(
                    0,
                    proyectoSeleccionado.getProyectoId(),
                    clienteSeleccionado.getClienteId(),
                    totalPresupuesto,
                    LocalDate.now(),
                    estado
            );

            dao.insertarPresupuesto(nuevoPresupuesto);
            view.mostrarMensaje("Presupuesto creado con éxito.");
            cargarPresupuestos();

        } catch (NumberFormatException ex) {
            view.mostrarMensaje("El total del presupuesto debe ser un número válido.");
        } catch (Exception ex) {
            view.mostrarMensaje("Error al crear el presupuesto: " + ex.getMessage());
        }
    }

    /**
     * Método para filtrar los presupuestos.
     */
    private void filtrarPresupuestos(ActionEvent e) {
        try {
            String filtro = view.getFiltro();
            if (filtro.isEmpty()) {
                cargarPresupuestos();
                return;
            }

            view.mostrarPresupuestos(dao.filtrarPresupuestos("ClienteID", filtro));

        } catch (Exception ex) {
            view.mostrarMensaje("Error al filtrar presupuestos: " + ex.getMessage());
        }
    }

    /**
     * Método para cargar los presupuestos en la tabla.
     */
    private void cargarPresupuestos() {
        try {
            List<Presupuesto> presupuestos = dao.obtenerPresupuestos();
            view.mostrarPresupuestos(presupuestos);
        } catch (Exception ex) {
            view.mostrarMensaje("Error al cargar presupuestos: " + ex.getMessage());
        }
    }

    /**
     * Método para actualizar los proyectos según el cliente seleccionado.
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
     * Método para preparar la edición de un presupuesto.
     */
    private void editarPresupuesto(ActionEvent e) {
        int selectedRow = view.getTblPresupuestos().getSelectedRow();
        if (selectedRow >= 0) {
            int presupuestoId = (int) view.getTblPresupuestos().getValueAt(selectedRow, 0);
            Presupuesto presupuesto = dao.obtenerPresupuestoPorId(presupuestoId);

            if (presupuesto != null) {
                view.getTotalPresupuesto().setText(String.valueOf(presupuesto.getTotalPresupuesto()));
                view.getCbEstado().setSelectedItem(presupuesto.getEstado());
                view.getCbProyecto().setSelectedItem(obtenerProyectoPorId(presupuesto.getProyectoId()));
                view.getCbCliente().setSelectedItem(obtenerClientePorId(presupuesto.getClienteId()));
            }
        } else {
            view.mostrarMensaje("Por favor, selecciona un presupuesto para editar.");
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

    private void borrarPresupuesto(ActionEvent e) {
        int selectedRow = view.getTblPresupuestos().getSelectedRow();
        if (selectedRow >= 0) {
            int presupuestoId = (int) view.getTblPresupuestos().getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(view, "¿Seguro que deseas borrar este presupuesto?");
            if (confirm == JOptionPane.YES_OPTION) {
                dao.eliminarPresupuesto(presupuestoId);
                view.mostrarMensaje("Presupuesto borrado correctamente.");
                cargarPresupuestos();
            }
        } else {
            view.mostrarMensaje("Por favor, selecciona un presupuesto para borrar.");
        }
    }

    public void descargarPresupuestoPDF(int presupuestoId) {
        try {
            // Obtener datos del presupuesto
            Presupuesto presupuesto = dao.obtenerPresupuestoPorId(presupuestoId);
            Cliente cliente = obtenerClientePorId(presupuesto.getClienteId());
            Proyecto proyecto = obtenerProyectoPorId(presupuesto.getProyectoId());

            // Crear el archivo de salida
            String filePath = "output/presupuestos/Presupuesto_" + presupuestoId + ".pdf";

            // Asegurarnos de que la carpeta de salida existe
            File outputDir = new File("output/presupuestos");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Generar el PDF con el diseño avanzado
            GeneradorPDF.generarPresupuestoPDF(
                    filePath,
                    cliente.getNombre(),
                    cliente.getEmail(),
                    cliente.getDireccion(),
                    cliente.getTelefono(),
                    presupuesto.getFechaCreacion().toString(),
                    " " + proyecto.getNombreProyecto(),
                    presupuesto.getTotalPresupuesto(),
                    "50% a la aceptación y 50% a la entrega",
                    "logoDocumentos.png"
            );

            // Mostrar mensaje de éxito
            view.mostrarMensaje("Presupuesto descargado en: " + filePath);

        } catch (Exception e) {
            view.mostrarMensaje("Error al generar el PDF: " + e.getMessage());
        }
    }

    private void retroceder(ActionEvent e) {
        System.out.println("Retroceder al menú anterior.");
    }
}

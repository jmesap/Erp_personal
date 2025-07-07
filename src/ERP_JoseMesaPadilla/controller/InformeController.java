/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ERP_JoseMesaPadilla.controller;

import ERP_JoseMesaPadilla.dao.InformeDAO;
import ERP_JoseMesaPadilla.dao.PresupuestoDAO;
import ERP_JoseMesaPadilla.dao.ProyectoDAO;
import ERP_JoseMesaPadilla.view.InformeView;
import ERP_JoseMesaPadilla.model.Informe;
import ERP_JoseMesaPadilla.model.Proyecto;
import java.awt.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InformeController {

    private final InformeView view;
    private final InformeDAO dao;

public InformeController(InformeView view, InformeDAO dao) {
    this.view = view;
    this.dao = dao;

    // Asociar eventos de los botones
    view.getBtnEditar().addActionListener(this::prepararEdicionInforme);
    view.getBtnGuardar().addActionListener(this::guardarInforme);
    view.getBtnActualizar().addActionListener(this::actualizarInforme);
    view.getBtnEliminar().addActionListener(this::eliminarInforme);
    view.getBtnNuevo().addActionListener(this::limpiarFormulario);
    cargarProyectosEnComboBox(); // Cargar proyectos al iniciar la vista

    // Cargar los datos iniciales
    cargarInformes();

    // Deshabilitar el botón de actualizar al inicio
    view.getBtnActualizar().setEnabled(false);
}


    private void cargarProyectosEnComboBox() {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        List<Proyecto> proyectos = null;
        try {
            proyectos = proyectoDAO.obtenerProyectos();
        } catch (SQLException ex) {
            Logger.getLogger(InformeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        JComboBox<Proyecto> cbProyectos = view.getCbProyectos();
        cbProyectos.removeAllItems(); // Limpiar ComboBox

        for (Proyecto proyecto : proyectos) {
            // Añadir directamente el objeto Proyecto al JComboBox
            cbProyectos.addItem(proyecto);
        }

        // Si deseas personalizar la forma en que se muestra el Proyecto (como "ID - Nombre")
        cbProyectos.setRenderer(new ListCellRenderer<Proyecto>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Proyecto> list, Proyecto value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel();
                if (value != null) {
                    // Muestra "ID - NombreProyecto"
                    label.setText(value.getProyectoId() + " - " + value.getNombreProyecto());
                }
                return label;
            }
        });
    }

    /**
     * Carga todos los informes desde la base de datos y los muestra en la
     * tabla.
     */
    public void cargarInformes() {
        try {
            List<Informe> informes = dao.obtenerTodosLosInformes();
            DefaultTableModel model = (DefaultTableModel) view.getTblInformes().getModel();
            model.setRowCount(0); // Limpiar tabla

            for (Informe informe : informes) {
                model.addRow(new Object[]{
                    informe.getInformeId(),
                    obtenerProyectoPorId(informe.getProyectoId()),
                    informe.getFechaGeneracion(),
                    informe.getTipoInforme(),
                    informe.getContenido()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al cargar los informes: " + ex.getMessage());
        }
    }

    /**
     * Guarda un nuevo informe en la base de datos.
     */
    public void guardarInforme(ActionEvent e) {
        try {
            // Obtén los valores de la vista
            String fechaTexto = view.getTxtFechaGeneracion().getText(); // Fecha en texto
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(fechaTexto); // Convierte a java.util.Date
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime()); // Convierte a java.sql.Date

            String tipo = view.getCbTipoInforme().getSelectedItem().toString(); // Tipo de informe
            String contenido = view.getTxtContenido().getText(); // Contenido del informe

            // Obtener el objeto Proyecto seleccionado
            Proyecto proyectoSeleccionado = (Proyecto) view.getCbProyectos().getSelectedItem();

            // Verifica si se seleccionó un proyecto
            if (proyectoSeleccionado == null) {
                JOptionPane.showMessageDialog(view, "Error: Debes seleccionar un proyecto.");
                return; // Salir si no hay proyecto seleccionado
            }

            // Obtener el nombre o el ID del proyecto según necesites
            String proyectoNombre = proyectoSeleccionado.getNombreProyecto(); // Si necesitas el nombre
            int proyectoID = proyectoSeleccionado.getProyectoId(); // O si necesitas el ID directamente

            // Si necesitas obtener el ID a partir del nombre o algún otro atributo, lo puedes hacer aquí
            System.out.println("Proyecto seleccionado: " + proyectoNombre); // Debug

            // Crea el objeto Informe con java.sql.Date
            Informe informe = new Informe(0, proyectoID, sqlDate, tipo, contenido);

            // Inserta el informe usando el DAO
            dao.insertarInforme(informe);

            // Actualiza la vista
            cargarInformes(); // Recarga la tabla con los datos actualizados
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(view, "Formato de fecha incorrecto. Use el formato yyyy-MM-dd.");
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al guardar el informe: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Actualiza el informe seleccionado.
     */
private void actualizarInforme(ActionEvent e) {
    int selectedRow = view.getTblInformes().getSelectedRow(); // Obtener la fila seleccionada
    if (selectedRow >= 0) {
        // Obtener el ID del informe desde la tabla
        int informeId = (int) view.getTblInformes().getValueAt(selectedRow, 0);

        // Obtener el informe correspondiente desde la base de datos
        Informe informe = dao.obtenerInformePorId(informeId);
        if (informe != null) {
            try {
                // Actualizar los datos del informe con los valores de la vista
                String fechaTexto = view.getTxtFechaGeneracion().getText(); // Fecha en texto
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDate = sdf.parse(fechaTexto); // Convierte a java.util.Date
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime()); // Convierte a java.sql.Date

                String tipo = (String) view.getCbTipoInforme().getSelectedItem(); // Tipo de informe
                String contenido = view.getTxtContenido().getText(); // Contenido del informe

                // Obtener el proyecto seleccionado
                Proyecto proyectoSeleccionado = (Proyecto) view.getCbProyectos().getSelectedItem();
                int proyectoID = proyectoSeleccionado.getProyectoId(); // Obtener el ID del proyecto

                // Verificar si el proyecto seleccionado es válido
                if (proyectoID == -1) {
                    JOptionPane.showMessageDialog(view, "Error: Proyecto seleccionado no válido.");
                    return; // Salir si el proyecto no es válido
                }

                // Establecer los valores en el objeto Informe
                informe.setFechaGeneracion(sqlDate);
                informe.setTipoInforme(tipo);
                informe.setContenido(contenido);
                informe.setProyectoId(proyectoID);

                // Guardar los cambios en la base de datos
                if (dao.actualizarInforme(informe)) {
                    JOptionPane.showMessageDialog(view, "Informe actualizado con éxito.");
                    cargarInformes(); // Recargar los informes en la tabla
                } else {
                    JOptionPane.showMessageDialog(view, "Error al actualizar el informe.");
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(view, "Formato de fecha incorrecto. Use el formato yyyy-MM-dd.");
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(view, "Informe no encontrado para actualizar.");
        }
    } else {
        JOptionPane.showMessageDialog(view, "Por favor, selecciona un informe para actualizar.");
    }
}


    /**
     * Elimina el informe seleccionado.
     */
    private void eliminarInforme(ActionEvent e) {
        int selectedRow = view.getTblInformes().getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(view, "¿Seguro que deseas eliminar este informe?");
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    int informeId = (int) view.getTblInformes().getValueAt(selectedRow, 0);
                    dao.eliminarInforme(informeId);
                    JOptionPane.showMessageDialog(view, "Informe eliminado exitosamente.");
                    cargarInformes();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Error al eliminar el informe: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(view, "Por favor, selecciona un informe para eliminar.");
        }
    }

    /**
     * Limpia el formulario para permitir crear un nuevo informe.
     */
    private void limpiarFormulario(ActionEvent e) {
        view.getTxtFechaGeneracion().setText("");
        view.getCbTipoInforme().setSelectedIndex(0);
        view.getTxtContenido().setText("");
        view.getTblInformes().clearSelection();
    }

    /**
     * Obtiene los datos ingresados en el formulario.
     */
    private Informe obtenerDatosFormulario() throws Exception {
        try {
            // Validar y obtener la fecha
            String fechaTexto = view.getTxtFechaGeneracion().getText().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Formato esperado
            Date fecha = sdf.parse(fechaTexto); // Convierte a tipo Date

            // Validar el contenido
            String tipoInforme = (String) view.getCbTipoInforme().getSelectedItem();
            String contenido = view.getTxtContenido().getText().trim();

            if (contenido.isEmpty()) {
                throw new Exception("El contenido no puede estar vacío.");
            }

            // Crear y devolver el objeto Informe
            return new Informe(0, 0, fecha, tipoInforme, contenido);
        } catch (Exception ex) {
            throw new Exception("Datos del formulario inválidos: " + ex.getMessage());
        }
    }

    /**
     * Obtiene el nombre del proyecto por su ID.
     */
    private Proyecto obtenerProyectoPorId(int proyectoId) {
        InformeDAO informeDAO = new InformeDAO(); // Instancia de InformeDAO
        return informeDAO.obtenerProyectos().stream()
                .filter(proyecto -> proyecto.getProyectoId() == proyectoId)
                .findFirst()
                .orElse(null);
    }

    private int obtenerIdDesdeSeleccion(String seleccion) {
        if (seleccion != null && !seleccion.isEmpty()) {
            try {
                // Divide la cadena usando el primer espacio, y toma la primera parte (ID)
                String[] partes = seleccion.split(" - ");
                if (partes.length > 1) {
                    return Integer.parseInt(partes[0].trim()); // Devuelve el ID (parte antes del "-")
                } else {
                    return -1; // Si no hay un ID válido, retornar -1
                }
            } catch (NumberFormatException ex) {
                return -1; // Si ocurre un error al convertir el ID, lo maneja aquí
            }
        } else {
            return -1; // Si la selección está vacía o nula, retornar -1
        }
    }

    private void prepararEdicionInforme(ActionEvent e) {
        int selectedRow = view.getTblInformes().getSelectedRow(); // Fila seleccionada
        if (selectedRow >= 0) {
            // Obtener los datos de la tabla
            int informeId = (int) view.getTblInformes().getValueAt(selectedRow, 0);
            String tipoInforme = (String) view.getTblInformes().getValueAt(selectedRow, 3);
            String contenido = (String) view.getTblInformes().getValueAt(selectedRow, 4);

            // Obtener el objeto Proyecto completo (asumiendo que la columna 1 contiene un objeto Proyecto)
            Proyecto proyecto = (Proyecto) view.getTblInformes().getValueAt(selectedRow, 1);
            String proyectoNombre = proyecto.getNombreProyecto(); // Obtener el nombre del proyecto

            // Obtener el valor de la fecha como java.sql.Date
            java.sql.Date fechaSQL = (java.sql.Date) view.getTblInformes().getValueAt(selectedRow, 2);

            // Convertir la fecha a String usando un SimpleDateFormat
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Ajusta el formato de fecha según lo necesites
            String fecha = sdf.format(fechaSQL); // Convertir la fecha SQL a String

            // Mostrar los valores en los campos de texto y combos
            view.getTxtFechaGeneracion().setText(fecha);
            view.getCbTipoInforme().setSelectedItem(tipoInforme);
            view.getTxtContenido().setText(contenido);

            // Seleccionar el proyecto en el JComboBox de tipo Proyecto
            JComboBox<Proyecto> cbProyectos = view.getCbProyectos();
            for (int i = 0; i < cbProyectos.getItemCount(); i++) {
                Proyecto proyectoItem = cbProyectos.getItemAt(i); // Obtener el Proyecto completo
                if (proyectoItem.getNombreProyecto().equals(proyectoNombre)) { // Comparar por nombre del proyecto
                    cbProyectos.setSelectedItem(proyectoItem); // Seleccionar el objeto Proyecto
                    break;
                }
            }

            // Deshabilitar el botón de "Guardar" y habilitar el de "Actualizar"
            view.getBtnGuardar().setEnabled(false);
            view.getBtnActualizar().setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(view, "Por favor, selecciona un informe para editar.");
        }
    }

}

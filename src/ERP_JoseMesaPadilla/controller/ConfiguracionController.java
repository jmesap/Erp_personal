
package ERP_JoseMesaPadilla.controller;

import ERP_JoseMesaPadilla.dao.UsuarioDAO;
import ERP_JoseMesaPadilla.view.ConfiguracionView;

import javax.swing.*;

public class ConfiguracionController {
    // Programar para que en configuración se cree un nuevo usuario (se abrirá un cuadro de diálogo para que se cree).
    // Que se pueda seleccionar la carpeta donde se descargan los presupuestos y las facturas
    private ConfiguracionView configuracionView;
    private UsuarioDAO usuarioDAO;

    // Constructor que acepta ConfiguracionView y una instancia de UsuarioDAO
    public ConfiguracionController(ConfiguracionView configuracionView, UsuarioDAO usuarioDAO) {
        this.configuracionView = configuracionView;
        this.usuarioDAO = usuarioDAO;

        // Inicializar los listeners para manejar eventos en la vista
        initListeners();
    }

    // Método para inicializar los oyentes de la vista (botones, etc.)
    private void initListeners() {
        configuracionView.getBtnCambiarRol().addActionListener(e -> cambiarRol());
    }

    // Método que maneja la lógica para cambiar el rol de un usuario
    private void cambiarRol() {
        String usuarioId = configuracionView.getUsuarioIdField().getText(); // Obtener el ID de usuario desde la vista
        String nuevoRol = configuracionView.getNuevoRolComboBox().getSelectedItem().toString(); // Obtener el nuevo rol desde el comboBox

        if (usuarioId.isEmpty()) {
            JOptionPane.showMessageDialog(configuracionView, "Por favor, ingrese un ID de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si el nuevo rol es válido
        if (!nuevoRol.equals("Administrador") && !nuevoRol.equals("Empleado")) {
            JOptionPane.showMessageDialog(configuracionView, "Rol inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Intentar cambiar el rol en la base de datos
        boolean exito = usuarioDAO.cambiarRolUsuario(usuarioId, nuevoRol);

        if (exito) {
            JOptionPane.showMessageDialog(configuracionView, "Rol de usuario actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(configuracionView, "Hubo un error al actualizar el rol del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
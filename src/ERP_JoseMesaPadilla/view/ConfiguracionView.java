package ERP_JoseMesaPadilla.view;

import javax.swing.*;
import java.awt.*;

public class ConfiguracionView extends JPanel {

    private JTextField usuarioIdField;
    private JComboBox<String> nuevoRolComboBox;
    private JButton btnCambiarRol;

    public ConfiguracionView() {
        setLayout(new BorderLayout());
        setBackground(new Color(101, 102, 103)); // Fondo oscuro

        // Panel superior: Título
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(101, 102, 103));
        JLabel titleLabel = new JLabel("Configuración de Roles");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        // Panel central: Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(101, 102, 103));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo: ID de Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabel("ID de Usuario:"), gbc);

        gbc.gridx = 1;
        usuarioIdField = new JTextField();
        formPanel.add(usuarioIdField, gbc);

        // Campo: Nuevo Rol
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createLabel("Nuevo Rol:"), gbc);

        gbc.gridx = 1;
        String[] roles = {"Administrador", "Empleado"};
        nuevoRolComboBox = new JComboBox<>(roles);
        formPanel.add(nuevoRolComboBox, gbc);

        // Panel inferior: Botón
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(101, 102, 103));

        btnCambiarRol = createButton("Cambiar Rol");
        buttonPanel.add(btnCambiarRol);

        // Añadir componentes al panel principal
        add(topPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Montserrat", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Montserrat", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(255, 182, 0));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Getters para los componentes
    public JTextField getUsuarioIdField() {
        return usuarioIdField;
    }

    public JComboBox<String> getNuevoRolComboBox() {
        return nuevoRolComboBox;
    }

    public JButton getBtnCambiarRol() {
        return btnCambiarRol;
    }
public Component getContentPane() {
    return this;
}

}

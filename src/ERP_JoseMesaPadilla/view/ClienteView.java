package ERP_JoseMesaPadilla.view;

import ERP_JoseMesaPadilla.controller.ClienteController;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ClienteView extends JFrame {

    private JTextField txtNombre, txtEmail, txtTelefono, txtDireccion, txtBuscar;
    private JButton btnAgregar, btnEditar, btnEliminar, btnBuscar, btnGuardarCambios;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    public ClienteView() {
        setTitle("Gestión de Clientes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 700);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(101, 102, 103));

        // Panel superior (logo y título)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(101, 102, 103));
        JLabel logoLabel = new JLabel(loadIcon("fotoUsuario.png", 100, 100));
        topPanel.add(logoLabel);

        JLabel titleLabel = new JLabel("Gestión de Clientes");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        // Panel central (formulario y tabla)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(101, 102, 103));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel para formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(101, 102, 103));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Añadir campos de texto y etiquetas al formulario
        txtNombre = addFormRow("Nombre:", formPanel, gbc, 0);
        txtEmail = addFormRow("Email:", formPanel, gbc, 1);
        txtTelefono = addFormRow("Teléfono:", formPanel, gbc, 2);
        txtDireccion = addFormRow("Dirección:", formPanel, gbc, 3);
        txtBuscar = addFormRow("Buscar por nombre:", formPanel, gbc, 4);

        // Botones del formulario
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(101, 102, 103));
        btnAgregar = createButton("Agregar Cliente");
        btnEditar = createButton("Editar Cliente");
        btnEliminar = createButton("Eliminar Cliente");
        btnGuardarCambios = createButton("Guardar Cambios");
        btnBuscar = createButton("Buscar");
        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnGuardarCambios);
        buttonPanel.add(btnBuscar);

        // Añadir formulario y botones al panel central
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        // Tabla de clientes
        modeloTabla = new DefaultTableModel();
        modeloTabla.setColumnIdentifiers(new Object[]{"ID", "Nombre", "Email", "Teléfono", "Dirección"});
        tablaClientes = new JTable(modeloTabla);
        estiloTabla(tablaClientes);
        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        scrollPane.setPreferredSize(new Dimension(750, 300));
        centerPanel.add(scrollPane, BorderLayout.SOUTH);

        // Añadir todo al mainPanel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Configurar el JFrame
        setContentPane(mainPanel);
    }

    private void estiloTabla(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Montserrat", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(255, 182, 0));
        table.setSelectionForeground(Color.WHITE);

        // Estilo del encabezado
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Montserrat", Font.BOLD, 16));
        header.setBackground(new Color(255, 219, 118)); // Fondo amarillo corporativo
        header.setForeground(new Color(101, 102, 103)); // Letra #656667
        header.setReorderingAllowed(false);

        // Alineación de texto en celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    private JTextField addFormRow(String labelText, JPanel parentPanel, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Montserrat", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(300, 30));
        textField.setFont(new Font("Montserrat", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = row;
        parentPanel.add(label, gbc);

        gbc.gridx = 1;
        parentPanel.add(textField, gbc);

        return textField;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Montserrat", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(255, 182, 0));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private ImageIcon loadIcon(String iconName, int width, int height) {
        String iconPath = "/ERP_JoseMesaPadilla/imagenes/" + iconName;
        java.net.URL imgURL = getClass().getResource(iconPath);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            System.err.println("No se pudo cargar la imagen: " + iconPath);
            return null;
        }
    }

    // Getters para los botones y otros componentes
    public JButton getBtnAgregar() {
        return btnAgregar;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnGuardarCambios() {
        return btnGuardarCambios;
    }

    public JTable getTablaClientes() {
        return tablaClientes;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JTextField getTxtTelefono() {
        return txtTelefono;
    }

    public JTextField getTxtDireccion() {
        return txtDireccion;
    }

    public JTextField getTxtBuscar() {
        return txtBuscar;
    }
}

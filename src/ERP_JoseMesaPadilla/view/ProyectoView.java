package ERP_JoseMesaPadilla.view;

import ERP_JoseMesaPadilla.model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ProyectoView extends JFrame {

    private JTextField txtNombre, txtFechaInicio, txtFechaFin;
    private JComboBox<String> cbTipo;
    private JComboBox<Cliente> cbClientes;
    private JButton btnAgregar, btnEditar, btnEliminar, btnGuardarCambios;
    private JTable tablaProyectos;
    private DefaultTableModel modeloTabla;

    public ProyectoView() {
        setTitle("Gestión de Proyectos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(101, 102, 103));

        // Panel superior (logo y título)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(101, 102, 103));
        JLabel logoLabel = new JLabel(loadIcon("fotoUsuario.png", 100, 100));
        topPanel.add(logoLabel);

        JLabel titleLabel = new JLabel("Gestión de Proyectos");
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
        cbTipo = addFormRowComboBox("Tipo:", new String[]{"Desarrollo", "Traducción"}, formPanel, gbc, 1);
        txtFechaInicio = addFormRow("Fecha Inicio:", formPanel, gbc, 2);
        txtFechaFin = addFormRow("Fecha Fin:", formPanel, gbc, 3);
        cbClientes = addFormRowComboBox("Cliente:", new JComboBox<>(), formPanel, gbc, 4);

        // Botones del formulario
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(101, 102, 103));
        btnAgregar = createButton("Agregar Proyecto");
        btnEditar = createButton("Editar Proyecto");
        btnEliminar = createButton("Eliminar Proyecto");
        btnGuardarCambios = createButton("Guardar Cambios");
        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnGuardarCambios);

        // Añadir formulario y botones al panel central
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        // Tabla de proyectos estilizada
        modeloTabla = new DefaultTableModel();
        modeloTabla.setColumnIdentifiers(new Object[]{"ID", "Nombre", "Tipo", "Fecha Inicio", "Fecha Fin", "Cliente"});
        tablaProyectos = new JTable(modeloTabla);
        estiloTabla(tablaProyectos);
        JScrollPane scrollPane = new JScrollPane(tablaProyectos);
        scrollPane.setPreferredSize(new Dimension(850, 300));
        centerPanel.add(scrollPane, BorderLayout.SOUTH);

        // Añadir todo al mainPanel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Configurar el JFrame
        setContentPane(mainPanel);
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

    private <T> JComboBox<T> addFormRowComboBox(String labelText, T[] items, JPanel parentPanel, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Montserrat", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);

        JComboBox<T> comboBox = new JComboBox<>(items);
        comboBox.setPreferredSize(new Dimension(300, 30));
        comboBox.setFont(new Font("Montserrat", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = row;
        parentPanel.add(label, gbc);

        gbc.gridx = 1;
        parentPanel.add(comboBox, gbc);

        return comboBox;
    }

    private <T> JComboBox<T> addFormRowComboBox(String labelText, JComboBox<T> comboBox, JPanel parentPanel, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Montserrat", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);

        comboBox.setPreferredSize(new Dimension(300, 30));
        comboBox.setFont(new Font("Montserrat", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = row;
        parentPanel.add(label, gbc);

        gbc.gridx = 1;
        parentPanel.add(comboBox, gbc);

        return comboBox;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Montserrat", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(255, 182, 0));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
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

    // Getters para componentes
    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JComboBox<String> getCbTipo() {
        return cbTipo;
    }

    public JTextField getTxtFechaInicio() {
        return txtFechaInicio;
    }

    public JTextField getTxtFechaFin() {
        return txtFechaFin;
    }

    public JComboBox<Cliente> getCbClientes() {
        return cbClientes;
    }

    public JButton getBtnAgregar() {
        return btnAgregar;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnGuardarCambios() {
        return btnGuardarCambios;
    }

    public JTable getTablaProyectos() {
        return tablaProyectos;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
}

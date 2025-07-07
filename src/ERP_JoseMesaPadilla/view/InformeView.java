package ERP_JoseMesaPadilla.view;

import ERP_JoseMesaPadilla.model.Proyecto;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class InformeView extends JPanel {
    private JTable tblInformes;
    private JTextField txtFechaGeneracion;
    private JComboBox<String> cbTipoInforme;
    private JComboBox<Proyecto> cbProyectos;
    private JTextArea txtContenido;
    private JButton btnGuardar, btnActualizar, btnEliminar, btnNuevo, btnEditar;

    public InformeView() {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 62, 80)); // Fondo oscuro

        // Panel superior: Título
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(101, 102, 103));
        JLabel titleLabel = new JLabel("Gestión de Informes");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        // Panel central: Tabla de informes
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(101, 102, 103));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tblInformes = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Proyecto", "Fecha", "Tipo", "Contenido"}, 0
        ));
        JScrollPane tableScrollPane = new JScrollPane(tblInformes);
        tableScrollPane.setPreferredSize(new Dimension(750, 300));
        estiloTabla(tblInformes);

        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Panel derecho: Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(101, 102, 103));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo: Proyecto (ComboBox)
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabel("Proyecto:"), gbc);

        gbc.gridx = 1;
        cbProyectos = new JComboBox<>();
        formPanel.add(cbProyectos, gbc);

        // Campo: Fecha de generación
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createLabel("Fecha de Generación:"), gbc);

        gbc.gridx = 1;
        txtFechaGeneracion = new JTextField();
        formPanel.add(txtFechaGeneracion, gbc);

        // Campo: Tipo de informe
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(createLabel("Tipo de Informe:"), gbc);

        gbc.gridx = 1;
        cbTipoInforme = new JComboBox<>(new String[]{"Avance", "Final", "Otro"});
        formPanel.add(cbTipoInforme, gbc);

        // Campo: Contenido
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(createLabel("Contenido:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        txtContenido = new JTextArea(5, 20);
        JScrollPane scrollContenido = new JScrollPane(txtContenido);
        formPanel.add(scrollContenido, gbc);

        // Panel inferior: Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(101, 102, 103));

        btnGuardar = createButton("Guardar");
        btnActualizar = createButton("Actualizar");
        btnEliminar = createButton("Eliminar");
        btnNuevo = createButton("Nuevo");
        btnEditar = createButton("Editar");

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnNuevo);
        buttonPanel.add(btnEditar);

        // Añadir componentes al panel principal
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(formPanel, BorderLayout.EAST);
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

    private void estiloTabla(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Montserrat", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(255, 182, 0));
        table.setSelectionForeground(Color.WHITE);

        // Estilo del encabezado
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Montserrat", Font.BOLD, 16));
        header.setBackground(new Color(255, 219, 118));
        header.setForeground(new Color(45, 62, 80)); // Texto oscuro
        header.setReorderingAllowed(false);

        // Alineación de texto en celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    // Getters para los componentes
    public JTable getTblInformes() {
        return tblInformes;
    }

    public JTextField getTxtFechaGeneracion() {
        return txtFechaGeneracion;
    }

    public JComboBox<String> getCbTipoInforme() {
        return cbTipoInforme;
    }

    public JComboBox<Proyecto> getCbProyectos() {
        return cbProyectos;
    }

    public JTextArea getTxtContenido() {
        return txtContenido;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnNuevo() {
        return btnNuevo;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }
}

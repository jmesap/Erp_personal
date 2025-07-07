package ERP_JoseMesaPadilla.view;

import ERP_JoseMesaPadilla.dao.ClienteDAO;
import ERP_JoseMesaPadilla.dao.ProyectoDAO;
import ERP_JoseMesaPadilla.model.Cliente;
import ERP_JoseMesaPadilla.model.Factura;
import ERP_JoseMesaPadilla.model.Proyecto;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class FacturaView extends JPanel {

    public JTable tblFacturas;
    private DefaultTableModel modeloTabla;
    private JComboBox<Proyecto> cbProyecto;
    private JComboBox<Cliente> cbCliente;
    private JTextField txtTotalFactura;
    private JComboBox<String> cbEstado;
    private JButton btnCrear, btnFiltrar, btnEditar, btnBorrar, btnRetroceder, btnGuardarCambios;
    private JTextField txtFiltro;

    public FacturaView() {
        setLayout(new BorderLayout());
        setBackground(new Color(101, 102, 103));

        // Panel superior con título
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(101, 102, 103));
        JLabel titleLabel = new JLabel("Gestión de Facturas");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        // Panel central con formulario y tabla
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(101, 102, 103));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(101, 102, 103));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbProyecto = addComboBoxRow("Proyecto:", formPanel, gbc, 0);
        cbCliente = addComboBoxRow("Cliente:", formPanel, gbc, 1);
        txtTotalFactura = addTextFieldRow("Total Factura:", formPanel, gbc, 2);
        cbEstado = addComboBoxRow("Estado:", formPanel, gbc, 3, new String[]{"Pendiente", "Pagada", "Cancelada"});

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(101, 102, 103));
        btnCrear = createButton("Crear Factura");
        btnFiltrar = createButton("Filtrar");
        btnEditar = createButton("Editar");
        btnBorrar = createButton("Borrar");
        btnGuardarCambios = createButton("Guardar Cambios");

        buttonPanel.add(btnCrear);
        buttonPanel.add(btnFiltrar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnBorrar);
        buttonPanel.add(btnGuardarCambios);

        // Tabla de facturas
        modeloTabla = new DefaultTableModel();
        modeloTabla.setColumnIdentifiers(new Object[]{"ID", "Número", "Proyecto", "Cliente", "Total", "Fecha", "Estado"});
        tblFacturas = new JTable(modeloTabla);
        estiloTabla(tblFacturas);

        JScrollPane scrollPane = new JScrollPane(tblFacturas);
        scrollPane.setPreferredSize(new Dimension(750, 300));

        // Añadir componentes al panel central
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Añadir todo al JPanel principal
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JTextField addTextFieldRow(String labelText, JPanel parentPanel, GridBagConstraints gbc, int row) {
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

    private <T> JComboBox<T> addComboBoxRow(String labelText, JPanel parentPanel, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Montserrat", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);

        JComboBox<T> comboBox = new JComboBox<>();
        comboBox.setPreferredSize(new Dimension(300, 30));
        comboBox.setFont(new Font("Montserrat", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = row;
        parentPanel.add(label, gbc);

        gbc.gridx = 1;
        parentPanel.add(comboBox, gbc);

        return comboBox;
    }

    private JComboBox<String> addComboBoxRow(String labelText, JPanel parentPanel, GridBagConstraints gbc, int row, String[] options) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Montserrat", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);

        JComboBox<String> comboBox = new JComboBox<>(options);
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
        header.setBackground(new Color(255, 219, 118));
        header.setForeground(new Color(101, 102, 103)); // Texto gris oscuro
        header.setReorderingAllowed(false);

        // Alineación de texto en celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    // Getters
    public JButton getBtnCrear() {
        return btnCrear;
    }

    public JButton getBtnFiltrar() {
        return btnFiltrar;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JButton getBtnBorrar() {
        return btnBorrar;
    }

    public JButton getBtnGuardarCambios() {
        return btnGuardarCambios;  // Método añadido para obtener el botón "Guardar Cambios"
    }

    public JTable getTblFacturas() {
        return tblFacturas;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JComboBox<Proyecto> getCbProyecto() {
        return cbProyecto;
    }

    public JComboBox<Cliente> getCbCliente() {
        return cbCliente;
    }

    public JTextField getTotalFactura() {
        return txtTotalFactura;
    }

    public JComboBox<String> getCbEstado() {
        return cbEstado;
    }

    // Método para obtener el filtro
    public String getFiltro() {
        return txtFiltro.getText().trim();
    }

    // Método para mostrar la lista de facturas en la tabla
    public void mostrarFacturas(List<Factura> facturas) {
        modeloTabla.setRowCount(0); // Limpiar tabla

        for (Factura factura : facturas) {
            // Obtener el nombre del cliente
            String clienteNombre = ClienteDAO.obtenerNombreClientePorId(factura.getClienteId());

            // Obtener el nombre del proyecto
            String proyectoNombre = ProyectoDAO.obtenerNombreProyectoPorId(factura.getProyectoId());

            // Agregar la fila a la tabla con los nombres de proyecto y cliente
            modeloTabla.addRow(new Object[]{
                factura.getFacturaId(),
                factura.generarNumeroFactura(),
                proyectoNombre, // Mostrar el nombre del proyecto
                clienteNombre, // Mostrar el nombre del cliente
                factura.getTotalFactura(),
                factura.getFechaEmision(),
                factura.getEstado()
            });
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}

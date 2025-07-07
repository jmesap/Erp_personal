package ERP_JoseMesaPadilla.view;

import ERP_JoseMesaPadilla.controller.*;
import ERP_JoseMesaPadilla.dao.*;
import ERP_JoseMesaPadilla.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MainView extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private UsuarioDAO usuarioDAO;

    public MainView(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;

        // Establecer FlatLaf Look and Feel
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        setTitle("ERP Personal");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Barra superior
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);

        // Barra lateral
        JPanel sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        // Panel de contenido
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Agregar vistas
        contentPanel.add(createClienteViewPanel(), "Clientes");
        contentPanel.add(createProyectosViewPanel(), "Proyectos");
        contentPanel.add(createPresupuestosViewPanel(), "Presupuestos");
        contentPanel.add(createFacturasViewPanel(), "Facturas");
        contentPanel.add(createInformesViewPanel(), "Informes");
        contentPanel.add(createConfiguracionViewPanel(), "Configuracion");


        
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setBackground(new Color(217, 217, 217)); // Gris claro

        // Panel para el logotipo
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(217, 217, 217));
        logoPanel.setPreferredSize(new Dimension(200, 150));
        JLabel logoLabel = new JLabel(loadIcon("fotoUsuario.png", 100, 100));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel nombreUsuario = new JLabel("José Mesa Padilla");
        nombreUsuario.setHorizontalAlignment(SwingConstants.CENTER);
        nombreUsuario.setFont(new Font("Roboto", Font.BOLD, 14));
        nombreUsuario.setForeground(Color.BLACK);
        logoPanel.add(logoLabel);
        logoPanel.add(nombreUsuario);

        // Botones de la barra lateral
        JPanel buttonsPanel = new JPanel(new GridLayout(6, 1));
        buttonsPanel.setBackground(new Color(50, 50, 50));
        buttonsPanel.add(createSidebarButton("Proyectos", "proyectos.png", "Proyectos"));
        buttonsPanel.add(createSidebarButton("Presupuestos", "presupuestos.png", "Presupuestos"));
        buttonsPanel.add(createSidebarButton("Facturas", "factura.png", "Facturas"));
        buttonsPanel.add(createSidebarButton("Clientes", "clientes.png", "Clientes"));
        buttonsPanel.add(createSidebarButton("Informes", "estadisticas.png", "Informes"));
        buttonsPanel.add(createSidebarButton("Configuración", "configuracion.png", "Configuracion"));

        sidebar.add(logoPanel, BorderLayout.NORTH);
        sidebar.add(buttonsPanel, BorderLayout.CENTER);

        return sidebar;
    }

    private JButton createSidebarButton(String text, String iconFileName, String viewName) {
        JButton button = new JButton(text, loadIcon(iconFileName, 30, 30));
        button.setFont(new Font("Roboto", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 50));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover y clic
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 182, 0)); // Color primario
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 50, 50)); // Color original
            }
        });

        button.addActionListener(e -> showView(viewName));
        return button;
    }

    private void showView(String viewName) {
        cardLayout.show(contentPanel, viewName);
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

    private JPanel createTopBar() {
        JPanel topBar = new JPanel();
        topBar.setLayout(new BorderLayout());
        topBar.setBackground(Color.BLACK);
        topBar.setPreferredSize(new Dimension(1366, 50));

        // Íconos en la barra superior
        JPanel iconsPanel = new JPanel();
        iconsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        iconsPanel.setBackground(Color.BLACK);

        iconsPanel.add(new JLabel(loadIcon("proyectosBarra.png", 30, 30)));
        iconsPanel.add(new JLabel(loadIcon("presupuestosBarra.png", 30, 30)));
        iconsPanel.add(new JLabel(loadIcon("facturaBarra.png", 30, 30)));
        iconsPanel.add(new JLabel(loadIcon("clientesBarra.png", 30, 30)));
        iconsPanel.add(new JLabel(loadIcon("estadisticasBarra.png", 30, 30)));

        // Triángulo amarillo en la esquina superior derecha
        JPanel trianglePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(255, 182, 0));
                int[] xPoints = {getWidth() - 50, getWidth(), getWidth()};
                int[] yPoints = {0, 0, 50};
                g.fillPolygon(xPoints, yPoints, 3);
            }
        };
        trianglePanel.setBackground(Color.BLACK);

        topBar.add(iconsPanel, BorderLayout.CENTER);
        topBar.add(trianglePanel, BorderLayout.EAST);

        return topBar;
    }

      private JPanel createClienteViewPanel() {
        ClienteView clienteView = new ClienteView();
        Cliente clienteModel = null;
        ClienteController clienteController = new ClienteController(clienteView, clienteModel);
        clienteController.cargarClientesEnTabla();
        JPanel clientePanel = new JPanel(new BorderLayout());
        clientePanel.add(clienteView.getContentPane(), BorderLayout.CENTER);
        return clientePanel;
    }

    private JPanel createProyectosViewPanel() {
        ProyectoView proyectoView = new ProyectoView();
        Proyecto proyectoModel = null;
        ProyectoController proyectoController = new ProyectoController(proyectoView, proyectoModel);
        proyectoController.cargarProyectosEnTabla();
        JPanel proyectoPanel = new JPanel(new BorderLayout());
        proyectoPanel.add(proyectoView.getContentPane(), BorderLayout.CENTER);
        return proyectoPanel;
    }

    private JPanel createPresupuestosViewPanel() {
        PresupuestoView presupuestoView = new PresupuestoView();
        PresupuestoDAO presupuestoDao = new PresupuestoDAO();
        PresupuestoController presupuestoController = new PresupuestoController(presupuestoView, presupuestoDao);
        presupuestoController.cargarDatos();
        JPanel presupuestoPanel = new JPanel(new BorderLayout());
        presupuestoPanel.add(presupuestoView, BorderLayout.CENTER);
        return presupuestoPanel;
    }

    private JPanel createFacturasViewPanel() {
        FacturaView facturaView = new FacturaView();
        FacturaDAO facturaDao = new FacturaDAO();
        FacturaController facturaController = new FacturaController(facturaView, facturaDao);
        facturaController.cargarDatos();
        JPanel facturasPanel = new JPanel(new BorderLayout());
        facturasPanel.add(facturaView, BorderLayout.CENTER);
        return facturasPanel;
    }

    private JPanel createInformesViewPanel() {
        InformeView informeView = new InformeView();
        InformeDAO informeDao = new InformeDAO();
        InformeController informeController = new InformeController(informeView, informeDao);
        informeController.cargarInformes();
        JPanel informesPanel = new JPanel(new BorderLayout());
        informesPanel.add(informeView, BorderLayout.CENTER);
        return informesPanel;
    }

    private JPanel createConfiguracionViewPanel() {
        ConfiguracionView configuracionView = new ConfiguracionView();
        ConfiguracionController configuracionController = new ConfiguracionController(configuracionView, usuarioDAO);
        JPanel configuracionPanel = new JPanel(new BorderLayout());
        configuracionPanel.add(configuracionView.getContentPane(), BorderLayout.CENTER);
        return configuracionPanel;
    }
}

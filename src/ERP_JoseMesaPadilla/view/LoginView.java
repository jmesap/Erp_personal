package ERP_JoseMesaPadilla.view;

import ERP_JoseMesaPadilla.controller.LoginController;
import ERP_JoseMesaPadilla.dao.UsuarioDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginView extends JFrame {

    private JTextField emailField;
    private JPasswordField dbPasswordField;
    private JButton loginButton;

    public LoginView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Iniciar Sesión - Traductor & Desarrollador");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con color de fondo #656667
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(101, 102, 103));  // #656667
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior con el logo
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(101, 102, 103));  // #656667
        JLabel logoLabel = new JLabel(loadIcon("fotoUsuario.png", 100, 100));
        topPanel.add(logoLabel);

        JLabel titleLabel = new JLabel("José Mesa Padilla", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        // Panel central con campos de texto
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(101, 102, 103));  // #656667
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        emailField = createInputField("Usuario o Email:", centerPanel);
        dbPasswordField = createPasswordField("Contraseña:", centerPanel);

        // Botón de inicio de sesión
        loginButton = new JButton("Iniciar Sesión");
        loginButton.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 16));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(255, 182, 0));
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(this::onLoginButtonClicked);

        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(loginButton);

        // Agregar los paneles al mainPanel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Configurar el JFrame
        setContentPane(mainPanel);
    }

    private JTextField createInputField(String labelText, JPanel parentPanel) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(300, 30));
        textField.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
        textField.setMaximumSize(new Dimension(300, 30));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(new Color(101, 102, 103));  // #656667
        inputPanel.add(label, BorderLayout.NORTH);
        inputPanel.add(textField, BorderLayout.CENTER);

        parentPanel.add(inputPanel);
        parentPanel.add(Box.createVerticalStrut(10));

        return textField;
    }

    private JPasswordField createPasswordField(String labelText, JPanel parentPanel) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 30));
        passwordField.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(300, 30));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(new Color(101, 102, 103));  // #656667
        inputPanel.add(label, BorderLayout.NORTH);
        inputPanel.add(passwordField, BorderLayout.CENTER);

        parentPanel.add(inputPanel);
        parentPanel.add(Box.createVerticalStrut(10));

        return passwordField;
    }

    private void onLoginButtonClicked(ActionEvent e) {
        String emailOrUser = emailField.getText();
        String dbPassword = new String(dbPasswordField.getPassword());

        String dbUrl = "jdbc:mysql://localhost:3306/BD_ERP_Josemesa";  // Ajusta según sea necesario
        String dbUser = emailOrUser;
        String dbPasswordFromField = dbPassword;

        LoginController controller = new LoginController();
        boolean isAuthenticated = controller.autenticarUsuario(dbUrl, dbUser, dbPasswordFromField, emailOrUser);

        if (isAuthenticated) {
            JOptionPane.showMessageDialog(this, "Login exitoso");

            UsuarioDAO usuarioDAO;
            try {
                usuarioDAO = new UsuarioDAO(dbUrl, dbUser, dbPasswordFromField);
            } catch (SQLException ex) {
                Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            MainView mainView = new MainView(usuarioDAO);
            mainView.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Login fallido. Verifique sus credenciales.");
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}

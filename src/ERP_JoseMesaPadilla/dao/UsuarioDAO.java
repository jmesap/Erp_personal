package ERP_JoseMesaPadilla.dao;

import java.sql.*;

public class UsuarioDAO {

    private Connection connection;
    private String dbUrl;   // Atributo para la URL de la base de datos
    private String dbUser;  // Atributo para el usuario
    private String dbPassword; // Atributo para la contraseña

    // Constructor de la clase con los parámetros para establecer la conexión
    public UsuarioDAO(String dbUrl, String dbUser, String dbPassword) throws SQLException {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw e;
        }
    }

    // Getters y Setters para dbUrl, dbUser y dbPassword (opcional, si los necesitas)
    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    // Método para verificar si el usuario existe en la base de datos
    public boolean verificarUsuario(String emailOrUser) {
        String query = "SELECT * FROM Usuario WHERE Email = ? OR Nombre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, emailOrUser);
            stmt.setString(2, emailOrUser);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next(); // Si existe una fila, el usuario es válido
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para cambiar el rol de un usuario
    public boolean cambiarRolUsuario(String usuarioId, String nuevoRol) {
        String query = "UPDATE Usuario SET Rol = ? WHERE UsuarioID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nuevoRol); // Establecer el nuevo rol
            statement.setInt(2, Integer.parseInt(usuarioId)); // Establecer el ID del usuario

            int filasAfectadas = statement.executeUpdate(); // Ejecutar la consulta

            return filasAfectadas > 0; // Si se afectaron filas, la actualización fue exitosa
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cerrar la conexión a la base de datos
    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}

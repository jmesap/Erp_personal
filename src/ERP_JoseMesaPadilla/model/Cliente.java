package ERP_JoseMesaPadilla.model;

import ERP_JoseMesaPadilla.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private int clienteId;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;

    // Constructor
    public Cliente(String nombre, String email, String telefono, String direccion) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Constructor con id
    public Cliente(int clienteId, String nombre, String email, String telefono, String direccion) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Getters y setters
    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int id) {
        this.clienteId = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Método para agregar cliente a la base de datos
    public static void agregarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Cliente (Nombre, Email, Telefono, Direccion) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getEmail());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getDireccion());

            pstmt.executeUpdate();
        }
    }

    // Método para obtener todos los clientes de la base de datos
    public static List<Cliente> obtenerClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("Nombre"),
                        rs.getString("Email"),
                        rs.getString("Telefono"),
                        rs.getString("Direccion")
                );
                cliente.setClienteId(rs.getInt("ClienteID"));
                clientes.add(cliente);
            }
        }

        return clientes;
    }
// Método para actualizar un cliente en la base de datos

    public static boolean editarCliente(Cliente cliente) throws SQLException {
        String query = "UPDATE Cliente SET Nombre = ?, Email = ?, Telefono = ?, Direccion = ? WHERE ClienteID = ?";

        // Usar try-with-resources para gestionar la conexión y el PreparedStatement
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            // Establecer los parámetros en el PreparedStatement
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getDireccion());
            stmt.setInt(5, cliente.getClienteId()); // El id del cliente a editar

            // Ejecutar la actualización
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0; // Retornar true si la actualización fue exitosa
        }
    }

// Continuación de la clase Cliente
    // Método para eliminar un cliente de la base de datos
    public static boolean eliminarCliente(int id) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE ClienteID = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0; // Retorna true si se eliminó correctamente
        }
    }

    // Método para buscar un cliente por ID
    public static Cliente obtenerClientePorId(int id) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE ClienteID = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("ClienteID"),
                            rs.getString("Nombre"),
                            rs.getString("Email"),
                            rs.getString("Telefono"),
                            rs.getString("Direccion")
                    );
                }
            }
        }
        return null; // Retorna null si no se encuentra el cliente
    }

    // Método para buscar clientes por nombre (o parte del nombre)
    public static List<Cliente> buscarClientesPorNombre(String nombre) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente WHERE Nombre LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombre + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente(
                            rs.getInt("ClienteID"),
                            rs.getString("Nombre"),
                            rs.getString("Email"),
                            rs.getString("Telefono"),
                            rs.getString("Direccion")
                    );
                    clientes.add(cliente);
                }
            }
        }
        return clientes;
    }

    // Método toString para imprimir clientes (útil para depuración)
    @Override
    public String toString() {
        return nombre;
    }
}

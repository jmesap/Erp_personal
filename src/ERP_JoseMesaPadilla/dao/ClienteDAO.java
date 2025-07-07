/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ERP_JoseMesaPadilla.dao;

import ERP_JoseMesaPadilla.model.Cliente;
import static ERP_JoseMesaPadilla.util.DatabaseConnection.getConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public List<Cliente> obtenerClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String query = "SELECT * FROM Cliente";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Aquí cambiamos "String" por "int" para el ID
                int id = rs.getInt("ClienteID");  // Asignamos el ID a una variable int
                String nombre = rs.getString("Nombre");
                String email = rs.getString("Email");
                String telefono = rs.getString("Telefono");
                String direccion = rs.getString("Direccion");

                Cliente cliente = new Cliente(id, nombre, email, telefono, direccion);
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los clientes: " + e.getMessage());
            throw new SQLException("Error al obtener los clientes", e);
        }
        return clientes;
    }

    // Método para obtener el nombre de un cliente por su ID
// Método estático para obtener el nombre de un cliente por su ID
    public static String obtenerNombreClientePorId(int clienteId) {
        String nombreCliente = null;
        String query = "SELECT Nombre FROM Cliente WHERE ClienteID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nombreCliente = rs.getString("Nombre");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreCliente;
    }

}

package ERP_JoseMesaPadilla.dao;

import ERP_JoseMesaPadilla.model.Proyecto;
import static ERP_JoseMesaPadilla.util.DatabaseConnection.getConnection; // Asumo que tienes esta clase de conexión
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProyectoDAO {

    // Método para obtener todos los proyectos
    public List<Proyecto> obtenerProyectos() throws SQLException {
        List<Proyecto> proyectos = new ArrayList<>();
        String query = "SELECT ProyectoID, Nombre, Tipo, FechaInicio, FechaFin, ClienteID FROM proyecto";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Obtener datos del ResultSet
                int id = rs.getInt("ProyectoID");
                String nombre = rs.getString("Nombre");
                String tipo = rs.getString("Tipo");
                Date fechaInicio = rs.getDate("FechaInicio");
                Date fechaFin = rs.getDate("FechaFin");
                int clienteId = rs.getInt("ClienteID");

                // Crear objeto Proyecto y agregarlo a la lista
                Proyecto proyecto = new Proyecto(id, nombre, tipo, fechaInicio, fechaFin, clienteId);
                proyectos.add(proyecto);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los proyectos: " + e.getMessage());
            throw new SQLException("Error al obtener los proyectos", e);
        }

        return proyectos;
    }

    // Método para agregar un nuevo proyecto
    public boolean agregarProyecto(Proyecto proyecto) throws SQLException {
        String query = "INSERT INTO proyecto (Nombre, Tipo, FechaInicio, FechaFin, ClienteID) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, proyecto.getNombreProyecto());
            stmt.setString(2, proyecto.getTipo());
            stmt.setDate(3, proyecto.getFechaInicio());
            stmt.setDate(4, proyecto.getFechaFin());
            stmt.setInt(5, proyecto.getClienteId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        // Obtener el ID generado automáticamente y asignarlo al objeto Proyecto
                        proyecto.setProyectoId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    // Método para editar un proyecto existente
    public boolean editarProyecto(Proyecto proyecto) throws SQLException {
        String query = "UPDATE proyecto SET Nombre = ?, Tipo = ?, FechaInicio = ?, FechaFin = ?, ClienteID = ? WHERE ProyectoID = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, proyecto.getNombreProyecto());
            stmt.setString(2, proyecto.getTipo());
            stmt.setDate(3, proyecto.getFechaInicio());
            stmt.setDate(4, proyecto.getFechaFin());
            stmt.setInt(5, proyecto.getClienteId());
            stmt.setInt(6, proyecto.getProyectoId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Método para eliminar un proyecto
    public boolean eliminarProyecto(int id) throws SQLException {
        String query = "DELETE FROM proyecto WHERE ProyectoID = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Método en el DAO para obtener un Proyecto por su ID
// Método estático en ProyectoDAO para obtener el nombre del proyecto por su ID
    public static String obtenerNombreProyectoPorId(int proyectoId) {
        String nombreProyecto = null;
        String query = "SELECT Nombre FROM Proyecto WHERE ProyectoID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, proyectoId); // Establecer el ID del proyecto
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nombreProyecto = rs.getString("Nombre"); // Obtener el nombre del proyecto
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreProyecto;
    }

}

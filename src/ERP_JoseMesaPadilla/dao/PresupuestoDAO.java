package ERP_JoseMesaPadilla.dao;

import ERP_JoseMesaPadilla.model.Cliente;
import ERP_JoseMesaPadilla.model.Presupuesto;
import ERP_JoseMesaPadilla.model.Proyecto;
import ERP_JoseMesaPadilla.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PresupuestoDAO {

    private Connection connection;

    public PresupuestoDAO() {
        try {
            // Inicialización de la conexión
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(PresupuestoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertarPresupuesto(Presupuesto presupuesto) throws SQLException {
        String query = "INSERT INTO Presupuesto (ProyectoID, ClienteID, TotalPresupuesto, FechaCreacion, Estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, presupuesto.getProyectoId());
            stmt.setInt(2, presupuesto.getClienteId());
            stmt.setDouble(3, presupuesto.getTotalPresupuesto());
            stmt.setDate(4, Date.valueOf(presupuesto.getFechaCreacion()));
            stmt.setString(5, presupuesto.getEstado());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                presupuesto.setPresupuestoId(rs.getInt(1));
            }
        }
    }

    public List<Presupuesto> obtenerPresupuestos() throws SQLException {
        String query = "SELECT * FROM Presupuesto";
        List<Presupuesto> presupuestos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Presupuesto presupuesto = new Presupuesto(
                        rs.getInt("PresupuestoID"),
                        rs.getInt("ProyectoID"),
                        rs.getInt("ClienteID"),
                        rs.getDouble("TotalPresupuesto"),
                        rs.getDate("FechaCreacion").toLocalDate(),
                        rs.getString("Estado")
                );
                presupuestos.add(presupuesto);
            }
        }
        return presupuestos;
    }

    public List<Presupuesto> filtrarPresupuestos(String campo, String valor) throws SQLException {
        String query = "SELECT * FROM Presupuesto WHERE " + campo + " = ?";
        List<Presupuesto> presupuestos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, valor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Presupuesto presupuesto = new Presupuesto(
                            rs.getInt("PresupuestoID"),
                            rs.getInt("ProyectoID"),
                            rs.getInt("ClienteID"),
                            rs.getDouble("TotalPresupuesto"),
                            rs.getDate("FechaCreacion").toLocalDate(),
                            rs.getString("Estado")
                    );
                    presupuestos.add(presupuesto);
                }
            }
        }
        return presupuestos;
    }

// Método para obtener todos los proyectos
    public List<Proyecto> obtenerProyectos() {
        List<Proyecto> proyectos = new ArrayList<>();
        String query = "SELECT ProyectoID, Nombre FROM Proyecto";
        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Recuperar los valores de la base de datos
                int proyectoId = rs.getInt("ProyectoID");
                String nombre = rs.getString("Nombre");

                // Usamos valores predeterminados para los demás campos
                // Como no tenemos los demás campos en la consulta, los pasamos como null o valores por defecto
                Proyecto proyecto = new Proyecto(proyectoId, nombre, null, null, null, 0);

                proyectos.add(proyecto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proyectos;
    }

    // Método para obtener todos los clientes
    public List<Cliente> obtenerClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String query = "SELECT ClienteID, Nombre, Direccion, Telefono, Email FROM Cliente";
        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Aquí pasamos todos los parámetros al constructor de Cliente
                Cliente cliente = new Cliente(
                        rs.getInt("ClienteID"),
                        rs.getString("Nombre"),
                        rs.getString("Direccion"),
                        rs.getString("Telefono"),
                        rs.getString("Email")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    // Método para obtener los proyectos de un cliente específico
    public List<Proyecto> obtenerProyectosPorCliente(int clienteId) {
        List<Proyecto> proyectos = new ArrayList<>();
        // Consulta para obtener los proyectos de un cliente específico
        String query = "SELECT ProyectoID, Nombre FROM Proyecto WHERE ClienteID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, clienteId);  // Establecemos el cliente en la consulta

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Proyecto proyecto = new Proyecto(
                            rs.getInt("ProyectoID"),
                            rs.getString("Nombre"),
                            null, null, null, 0 // Usamos valores predeterminados para otros campos
                    );
                    proyectos.add(proyecto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return proyectos;
    }

    public void eliminarPresupuesto(int presupuestoId) {
        String query = "DELETE FROM Presupuesto WHERE PresupuestoID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, presupuestoId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Presupuesto eliminado correctamente.");
            } else {
                System.out.println("No se encontró el presupuesto con ID: " + presupuestoId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean actualizarPresupuesto(Presupuesto presupuestoEditado) {
        String query = "UPDATE Presupuesto SET ProyectoID = ?, ClienteID = ?, TotalPresupuesto = ?, Estado = ? WHERE PresupuestoID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, presupuestoEditado.getProyectoId());
            stmt.setInt(2, presupuestoEditado.getClienteId());
            stmt.setDouble(3, presupuestoEditado.getTotalPresupuesto());
            stmt.setString(4, presupuestoEditado.getEstado());
            stmt.setInt(5, presupuestoEditado.getPresupuestoId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true si se actualizó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Presupuesto obtenerPresupuestoPorId(int presupuestoId) {
        String query = "SELECT * FROM Presupuesto WHERE PresupuestoID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, presupuestoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Presupuesto(
                            rs.getInt("PresupuestoID"),
                            rs.getInt("ProyectoID"),
                            rs.getInt("ClienteID"),
                            rs.getDouble("TotalPresupuesto"),
                            rs.getDate("FechaCreacion").toLocalDate(),
                            rs.getString("Estado")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

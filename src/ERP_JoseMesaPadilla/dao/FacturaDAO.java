package ERP_JoseMesaPadilla.dao;

import ERP_JoseMesaPadilla.model.Cliente;
import ERP_JoseMesaPadilla.model.Factura;
import ERP_JoseMesaPadilla.model.Proyecto;
import ERP_JoseMesaPadilla.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacturaDAO {

    private Connection connection;

    public FacturaDAO() {
        try {
            // Inicialización de la conexión
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FacturaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertarFactura(Factura factura) throws SQLException {
        String query = "INSERT INTO Factura (ProyectoID, ClienteID, TotalFactura, FechaEmision, Estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, factura.getProyectoId());
            stmt.setInt(2, factura.getClienteId());
            stmt.setDouble(3, factura.getTotalFactura());

            // Convertir de java.util.Date a java.sql.Date
            java.util.Date fechaEmisionUtil = factura.getFechaEmision();
            java.sql.Date fechaEmisionSql = new java.sql.Date(fechaEmisionUtil.getTime());

            // Establecer la fecha en el PreparedStatement
            stmt.setDate(4, fechaEmisionSql);

            stmt.setString(5, factura.getEstado());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                factura.setFacturaId(rs.getInt(1));
            }
        }
    }

    public List<Factura> obtenerFacturas() throws SQLException {
        String query = "SELECT * FROM Factura";
        List<Factura> facturas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Factura factura = new Factura(
                        rs.getInt("FacturaID"),
                        rs.getInt("ProyectoID"),
                        rs.getInt("ClienteID"),
                        rs.getDouble("TotalFactura"),
                        rs.getDate("FechaEmision"),
                        rs.getString("Estado")
                );
                facturas.add(factura);
            }
        }
        return facturas;
    }

    public List<Factura> filtrarFacturas(String campo, String valor) throws SQLException {
        String query = "SELECT * FROM Factura WHERE " + campo + " = ?";
        List<Factura> facturas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, valor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Factura factura = new Factura(
                            rs.getInt("FacturaID"),
                            rs.getInt("ProyectoID"),
                            rs.getInt("ClienteID"),
                            rs.getDouble("TotalFactura"),
                            rs.getDate("FechaEmision"),
                            rs.getString("Estado")
                    );
                    facturas.add(factura);
                }
            }
        }
        return facturas;
    }

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

    public void eliminarFactura(int facturaId) {
        String query = "DELETE FROM Factura WHERE FacturaID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, facturaId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Factura eliminada correctamente.");
            } else {
                System.out.println("No se encontró la factura con ID: " + facturaId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean actualizarFactura(Factura facturaEditada) {
        String query = "UPDATE Factura SET ProyectoID = ?, ClienteID = ?, TotalFactura = ?, Estado = ? WHERE FacturaID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, facturaEditada.getProyectoId());
            stmt.setInt(2, facturaEditada.getClienteId());
            stmt.setDouble(3, facturaEditada.getTotalFactura());
            stmt.setString(4, facturaEditada.getEstado());
            stmt.setInt(5, facturaEditada.getFacturaId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true si se actualizó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Factura obtenerFacturaPorId(int facturaId) {
        String query = "SELECT * FROM Factura WHERE FacturaID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, facturaId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Factura(
                            rs.getInt("FacturaID"),
                            rs.getInt("ProyectoID"),
                            rs.getInt("ClienteID"),
                            rs.getDouble("TotalFactura"),
                            rs.getDate("FechaEmision"),
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

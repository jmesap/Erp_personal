/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ERP_JoseMesaPadilla.dao;

import ERP_JoseMesaPadilla.model.Informe;
import ERP_JoseMesaPadilla.model.Proyecto;
import ERP_JoseMesaPadilla.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InformeDAO {

    private Connection connection;

    public InformeDAO() {
        try {
            // Inicialización de la conexión
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(PresupuestoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean insertarInforme(Informe informe) {
        String sql = "INSERT INTO Informe (ProyectoID, FechaGeneracion, TipoInforme, Contenido) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, informe.getProyectoId());
            stmt.setDate(2, new java.sql.Date(informe.getFechaGeneracion().getTime()));
            stmt.setString(3, informe.getTipoInforme());
            stmt.setString(4, informe.getContenido());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarInforme(Informe informe) {
        String sql = "UPDATE Informe SET ProyectoID = ?, FechaGeneracion = ?, TipoInforme = ?, Contenido = ? WHERE InformeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, informe.getProyectoId());
            stmt.setDate(2, new java.sql.Date(informe.getFechaGeneracion().getTime()));
            stmt.setString(3, informe.getTipoInforme());
            stmt.setString(4, informe.getContenido());
            stmt.setInt(5, informe.getInformeId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true si se actualizó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarInforme(int informeId) {
        String sql = "DELETE FROM Informe WHERE InformeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, informeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Informe obtenerInformePorId(int informeId) {
        String sql = "SELECT * FROM Informe WHERE InformeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, informeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Informe(
                        rs.getInt("InformeID"),
                        rs.getInt("ProyectoID"),
                        rs.getDate("FechaGeneracion"),
                        rs.getString("TipoInforme"),
                        rs.getString("Contenido")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra el informe
    }

    public List<Informe> obtenerTodosLosInformes() {
        String sql = "SELECT * FROM Informe";
        List<Informe> informes = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                informes.add(new Informe(
                        rs.getInt("InformeID"),
                        rs.getInt("ProyectoID"),
                        rs.getDate("FechaGeneracion"),
                        rs.getString("TipoInforme"),
                        rs.getString("Contenido")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return informes;
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
}

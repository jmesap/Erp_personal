package ERP_JoseMesaPadilla.model;

import ERP_JoseMesaPadilla.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Proyecto {

    private int proyectoId;
    private String nombreProyecto;
    private String tipo;
    private Date fechaInicio;
    private Date fechaFin;
    private int clienteId;

    // Constructor
    public Proyecto(String nombreProyecto, String tipo, Date fechaInicio, Date fechaFin, int clienteId) {
        this.nombreProyecto = nombreProyecto;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.clienteId = clienteId;
    }

    // Constructor con ID
    public Proyecto(int proyectoId, String nombreProyecto, String tipo, Date fechaInicio, Date fechaFin, int clienteId) {
        this.proyectoId = proyectoId;
        this.nombreProyecto = nombreProyecto;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.clienteId = clienteId;
    }

    // Getters y setters...
    public int getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(int proyectoId) {
        this.proyectoId = proyectoId;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    // Métodos CRUD
    public static void agregarProyecto(Proyecto proyecto) throws SQLException {
        String sql = "INSERT INTO Proyecto (Nombre, Tipo, FechaInicio, FechaFin, ClienteID) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, proyecto.getNombreProyecto());
            pstmt.setString(2, proyecto.getTipo());
            pstmt.setDate(3, proyecto.getFechaInicio());
            pstmt.setDate(4, proyecto.getFechaFin());
            pstmt.setInt(5, proyecto.getClienteId());
            pstmt.executeUpdate();
        }
    }

    public static List<Proyecto> obtenerProyectos() throws SQLException {
        List<Proyecto> proyectos = new ArrayList<>();
        String sql = "SELECT * FROM Proyecto";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Proyecto proyecto = new Proyecto(
                        rs.getInt("ProyectoID"),
                        rs.getString("Nombre"),
                        rs.getString("Tipo"),
                        rs.getDate("FechaInicio"),
                        rs.getDate("FechaFin"),
                        rs.getInt("ClienteID")
                );
                proyectos.add(proyecto);
            }
        }

        return proyectos;
    }

    public static boolean editarProyecto(Proyecto proyecto) throws SQLException {
        String sql = "UPDATE Proyecto SET Nombre = ?, Tipo = ?, FechaInicio = ?, FechaFin = ?, ClienteID = ? WHERE ProyectoID = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, proyecto.getNombreProyecto());
            pstmt.setString(2, proyecto.getTipo());
            pstmt.setDate(3, proyecto.getFechaInicio());
            pstmt.setDate(4, proyecto.getFechaFin());
            pstmt.setInt(5, proyecto.getClienteId());
            pstmt.setInt(6, proyecto.getProyectoId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public static boolean eliminarProyecto(int id) throws SQLException {
        String sql = "DELETE FROM Proyecto WHERE ProyectoID = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public String toString() {
        return this.nombreProyecto;  // Mostrar solo el nombre del proyecto
    }

}

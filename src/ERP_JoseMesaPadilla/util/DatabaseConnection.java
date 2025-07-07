package ERP_JoseMesaPadilla.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/bd_erp_josemesa?characterEncoding=UTF8";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    Connection conn = null;

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al establecer la conexión: " + e.getMessage());
            throw new SQLException("Error al establecer la conexión", e);
        }
    }

    public void cierraConexion() {
        try {
            conn.close();
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Error al cerrar conexion", "Error: " + sqle, JOptionPane.ERROR_MESSAGE);
        }
    }

    public ResultSet consultarRegistros(String strSentenciaSQL) {

        try {

            PreparedStatement pstm = conn.prepareStatement(strSentenciaSQL);
            ResultSet respuesta = pstm.executeQuery();
            return respuesta;

        } catch (SQLException e) {

            System.out.println("EXCEPCIÓN: " + e);
            return null;
        }

    }

    public int ejecutarSentencia(String strSentenciaSQL) {

        try {

            PreparedStatement pstm = conn.prepareStatement(strSentenciaSQL);
            pstm.execute();
            return 1;

        } catch (SQLException e) {

            System.out.println("EXCEPCIÓN: " + e);
            return 0;

        }

    }

}

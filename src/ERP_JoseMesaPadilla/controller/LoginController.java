package ERP_JoseMesaPadilla.controller;

import ERP_JoseMesaPadilla.dao.UsuarioDAO;

public class LoginController {

    private UsuarioDAO usuarioDAO;

    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    public boolean autenticarUsuario(String dbUrl, String dbUser, String dbPassword, String emailOrUser) {
        try {
            // Crear instancia de UsuarioDAO
            UsuarioDAO usuarioDAO = new UsuarioDAO(dbUrl, dbUser, dbPassword);

            // Verificar si el usuario existe en la base de datos
            boolean isAuthenticated = usuarioDAO.verificarUsuario(emailOrUser);

            // Si es necesario, puedes acceder a los datos de la conexión:
            System.out.println("Conectado a la base de datos en: " + usuarioDAO.getDbUrl());

            return isAuthenticated;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}

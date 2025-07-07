package ERP_JoseMesaPadilla;

import ERP_JoseMesaPadilla.controller.ClienteController;
import ERP_JoseMesaPadilla.model.Cliente;
import ERP_JoseMesaPadilla.view.ClienteView;

public class Main {
    public static void main(String[] args) {
        // Crear instancia de la vista
        ClienteView clienteView = new ClienteView();

        // Crear instancia del modelo (puedes crear un cliente vacío como modelo base)
        Cliente clienteModelo = new Cliente("", "", "", "");

        // Crear instancia del controlador
        ClienteController clienteController = new ClienteController(clienteView, clienteModelo);

        // Mostrar la ventana principal
        clienteView.setVisible(true);
    }
}

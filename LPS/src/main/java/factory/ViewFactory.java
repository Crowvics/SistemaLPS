package factory;

import model.entities.Usuario;
import view.AdministradorView;
import view.TecnicoView;
import view.ClienteView;

import javax.swing.*;

public class ViewFactory {

    public static JFrame criarViewParaUsuario(Usuario usuario) {
        switch (usuario.getTipoUsuario()) {
            case "Administrador":
                return new AdministradorView();

            case "Técnico":
                return new TecnicoView();

            case "Cliente":
                return new ClienteView(usuario.getEmail());

            default:
                throw new IllegalArgumentException("Tipo de usuário desconhecido: " + usuario.getTipoUsuario());
        }
    }
}

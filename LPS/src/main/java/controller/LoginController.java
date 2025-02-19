package controller;

import factory.ViewFactory;
import model.entities.Usuario;
import model.exception.LoginException;
import model.valid.ValidateLogin;
import view.LoginView;

import javax.swing.*;

public class LoginController {

    private final LoginView view;
    private final UsuarioController usuarioController;
    private final ValidateLogin validateLogin;

    public LoginController(LoginView view) {
        this.view = view;
        this.usuarioController = new UsuarioController();
        this.validateLogin = new ValidateLogin();
    }

    public void autenticarUsuario() {
        String email = view.getEmail();
        String senha = view.getSenha();

        try {
            validateLogin.validarCredenciais(email, senha);

            Usuario usuario = usuarioController.buscarUsuarioPorEmail(email);
            validateLogin.validarUsuarioExiste(usuario);

            validateLogin.validarSenha(usuario, senha);

            view.mostrarMensagem("Login bem-sucedido! Bem-vindo, " + usuario.getNome());
            JFrame novaTela = ViewFactory.criarViewParaUsuario(usuario);

            if (novaTela != null) {
                novaTela.setVisible(true);
                view.dispose();
            } else {
                view.mostrarMensagem("Erro ao abrir a tela do usuário.");
            }

        } catch (LoginException e) {
            view.mostrarMensagem(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            view.mostrarMensagem("Ocorreu um erro inesperado ao tentar fazer login.");
        }
    }

    public void redirecionarParaCriarConta() {
        new view.CriarContaView().setVisible(true);
    }
}

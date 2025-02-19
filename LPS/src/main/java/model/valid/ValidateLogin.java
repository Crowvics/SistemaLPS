package model.valid;

import model.entities.Usuario;
import model.exception.LoginException;

public class ValidateLogin {

    public void validarCredenciais(String email, String senha) {
        if (email == null || email.trim().isEmpty()) {
            throw new LoginException("Erro - O campo 'Email' está vazio.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new LoginException("Erro - O campo 'Senha' está vazio.");
        }
    }

    public void validarUsuarioExiste(Usuario usuario) {
        if (usuario == null) {
            throw new LoginException("Email ou senha incorretos!");
        }
    }

    public void validarSenha(Usuario usuario, String senha) {
        if (!usuario.getSenha().equals(senha)) {
            throw new LoginException("Email ou senha incorretos!");
        }
    }
}

package model.valid;

import java.util.List;
import model.entities.Usuario;
import model.exception.UsuarioException;

public class ValidateUsuario {

    public Usuario validaCamposEntrada(String nome, String cpf, String email, String senha, String tipoUsuario) {
        validarNome(nome);
        validarCPF(cpf);
        validarEmail(email);
        validarSenha(senha);
        validarTipoUsuario(tipoUsuario);

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setTipoUsuario(tipoUsuario);

        return usuario;
    }

    public void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new UsuarioException("Erro - Campo vazio: 'Nome'.");
        }
        if (nome.length() < 3 || nome.length() > 100) {
            throw new UsuarioException("Erro - O nome deve ter entre 3 e 100 caracteres.");
        }
    }

    public void validarCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new UsuarioException("Erro - Campo vazio: 'CPF'.");
        }
        if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new UsuarioException("Erro - Formato inválido no campo 'CPF'. Use o formato ###.###.###-##.");
        }
    }

    public void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new UsuarioException("Erro - Campo vazio: 'Email'.");
        }
        if (!email.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,4}$")) {
            throw new UsuarioException("Erro - Formato inválido no campo 'Email'.");
        }
    }

    public void validarSenha(String senha) {
        if (senha == null || senha.trim().isEmpty()) {
            throw new UsuarioException("Erro - Campo vazio: 'Senha'.");
        }
        if (senha.length() < 6) {
            throw new UsuarioException("Erro - A senha deve ter no mínimo 6 caracteres.");
        }
    }

    public void validarTipoUsuario(String tipoUsuario) {
        if (tipoUsuario == null || tipoUsuario.trim().isEmpty()) {
            throw new UsuarioException("Erro - Campo vazio: 'Tipo de Usuário'.");
        }
        if (!tipoUsuario.equalsIgnoreCase("Administrador")
                && !tipoUsuario.equalsIgnoreCase("Técnico")
                && !tipoUsuario.equalsIgnoreCase("Cliente")) {
            throw new UsuarioException("Erro - Tipo de usuário inválido. Deve ser 'Administrador', 'Técnico' ou 'Cliente'.");
        }
    }

    public void validarUsuarioExiste(Usuario usuario) {
        if (usuario == null) {
            throw new UsuarioException("Erro - Usuário não encontrado.");
        }
    }

    public void validarUnicidadeCPF(String cpf, Long idUsuario, List<Usuario> usuariosExistentes) {
        for (Usuario usuario : usuariosExistentes) {
            if (usuario.getCpf().equalsIgnoreCase(cpf) && (idUsuario == null || !usuario.getId().equals(idUsuario))) {
                throw new UsuarioException("Erro - CPF já cadastrado.");
            }
        }
    }

    public void validarUnicidadeEmail(String email, Long idUsuario, List<Usuario> usuariosExistentes) {
        for (Usuario usuario : usuariosExistentes) {
            if (usuario.getEmail().equalsIgnoreCase(email) && (idUsuario == null || !usuario.getId().equals(idUsuario))) {
                throw new UsuarioException("Erro - Email já cadastrado.");
            }
        }
    }

}

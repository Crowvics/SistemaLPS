package controller;

import model.dao.UsuarioDAO;
import model.entities.Usuario;
import model.valid.ValidateUsuario;
import model.exception.UsuarioException;
import view.AdministradorView;

import java.util.List;

public class UsuarioController {

    private final UsuarioDAO usuarioDAO;
    private final ValidateUsuario validateUsuario;

    public UsuarioController() {
        this.usuarioDAO = UsuarioDAO.getInstance();
        this.validateUsuario = new ValidateUsuario();
    }

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.findAll();
    }

    public void listarUsuariosParaView(AdministradorView view) {
        if (view != null) {
            view.atualizarTabelaUsuarios(listarUsuarios());
        }
    }

    public boolean clienteExiste(String email) {
        return usuarioDAO.findByEmail(email) != null;
    }

    public Usuario buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioDAO.find(id);
        validateUsuario.validarUsuarioExiste(usuario);
        return usuario;
    }

    public String adicionarUsuario(String nome, String cpf, String email, String senha, String tipoUsuario) {
        try {
            List<Usuario> usuariosExistentes = listarUsuarios();

            validateUsuario.validarUnicidadeCPF(cpf, null, usuariosExistentes);
            validateUsuario.validarUnicidadeEmail(email, null, usuariosExistentes);

            Usuario usuario = validateUsuario.validaCamposEntrada(nome, cpf, email, senha, tipoUsuario);

            usuarioDAO.save(usuario);
            return "Usuário cadastrado com sucesso!";
        } catch (UsuarioException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Erro ao cadastrar usuário: " + e.getMessage();
        }
    }

    public String editarUsuario(Long id, String nome, String cpf, String email, String senha, String tipoUsuario) {
        try {
            Usuario usuarioExistente = buscarUsuarioPorId(id);
            List<Usuario> usuariosExistentes = listarUsuarios();

            validateUsuario.validarUnicidadeCPF(cpf, id, usuariosExistentes);
            validateUsuario.validarUnicidadeEmail(email, id, usuariosExistentes);

            Usuario usuarioAtualizado = validateUsuario.validaCamposEntrada(nome, cpf, email, senha, tipoUsuario);

            usuarioExistente.setNome(usuarioAtualizado.getNome());
            usuarioExistente.setCpf(usuarioAtualizado.getCpf());
            usuarioExistente.setEmail(usuarioAtualizado.getEmail());
            usuarioExistente.setSenha(usuarioAtualizado.getSenha());
            usuarioExistente.setTipoUsuario(usuarioAtualizado.getTipoUsuario());

            usuarioDAO.update(usuarioExistente);
            return "Usuário atualizado com sucesso!";
        } catch (UsuarioException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Erro ao editar usuário: " + e.getMessage();
        }
    }

    public String excluirUsuario(Long id) {
        try {
            buscarUsuarioPorId(id);
            usuarioDAO.delete(id);
            return "Usuário excluído com sucesso!";
        } catch (UsuarioException e) {
            return e.getMessage();
        }
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioDAO.findByEmail(email);
    }

    public Usuario getUsuarioSelecionado(int selectedRow) {
        List<Usuario> usuarios = listarUsuarios();
        if (selectedRow < 0 || selectedRow >= usuarios.size()) {
            return null;
        }
        return usuarios.get(selectedRow);
    }
}

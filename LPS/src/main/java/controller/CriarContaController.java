package controller;

import model.entities.Usuario;
import view.CriarContaView;

public class CriarContaController {

    private final CriarContaView view;
    private final UsuarioController usuarioController;

    public CriarContaController(CriarContaView view) {
        this.view = view;
        this.usuarioController = new UsuarioController();
    }

    public void criarConta() {
        String nome = view.getNome();
        String cpf = view.getCpf();
        String email = view.getEmail();
        String senha = view.getSenha();

        try {
            String mensagem = usuarioController.adicionarUsuario(nome, cpf, email, senha, "Cliente");
            view.mostrarMensagem(mensagem);

            if (mensagem.equals("Usuário cadastrado com sucesso!")) {
                view.dispose();
            }
        } catch (Exception e) {
            view.mostrarMensagem(e.getMessage());
        }
    }

}

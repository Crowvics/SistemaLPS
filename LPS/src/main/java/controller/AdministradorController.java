package controller;

import view.AdministradorView;

import java.util.List;

public class AdministradorController {

    private final AdministradorView view;
    private final UsuarioController usuarioController;
    private final PecaController pecaController;
    private final DispositivoController dispositivoController;
    private final AtendimentoController atendimentoController;
    private final RelatorioController relatorioController;

    public AdministradorController(AdministradorView view) {
        this.view = view;
        this.usuarioController = new UsuarioController();
        this.atendimentoController = new AtendimentoController();
        this.dispositivoController = new DispositivoController(atendimentoController);
        this.pecaController = new PecaController();
        this.relatorioController = new RelatorioController(this.atendimentoController, this.pecaController);
    }

    public void listarRelatoriosParaView() {
        view.atualizarTabelaHistoricoRelatorios(relatorioController.listarRelatorios());
    }

    public String excluirRelatorioComoAdministrador(Long id) {
        return relatorioController.excluirRelatorioComoAdministrador(id);
    }

    public void listarUsuariosParaView() {
        view.atualizarTabelaUsuarios(usuarioController.listarUsuarios());
    }

    public String adicionarUsuario(String nome, String cpf, String email, String senha, String tipoUsuario) {
        return usuarioController.adicionarUsuario(nome, cpf, email, senha, tipoUsuario);
    }

    public String editarUsuario(Long id, String nome, String cpf, String email, String senha, String tipoUsuario) {
        return usuarioController.editarUsuario(id, nome, cpf, email, senha, tipoUsuario);
    }

    public String excluirUsuario(Long id) {
        return usuarioController.excluirUsuario(id);
    }

    public void listarPecasParaView() {
        view.atualizarTabelaPecas(pecaController.listarPecas());
    }

    public String adicionarPeca(String codigo, String nome, String categoria, String descricao, String quantidade) {
        return pecaController.adicionarPeca(codigo, nome, categoria, descricao, quantidade);
    }

    public String editarPeca(Long id, String codigo, String nome, String categoria, String descricao, String quantidade) {
        return pecaController.editarPeca(id, codigo, nome, categoria, descricao, quantidade);
    }

    public String excluirPeca(Long id) {
        return pecaController.excluirPeca(id);
    }

    public void listarDispositivosParaView() {
        view.atualizarTabelaDispositivos(dispositivoController.listarDispositivos());
    }

    public String editarDispositivo(Long id, String categoria, String marca, String modelo, String descricao) {
        return dispositivoController.editarDispositivoAdmin(id, categoria, marca, modelo, descricao);
    }

    public String excluirDispositivo(Long id) {
        return dispositivoController.excluirDispositivoAdmin(id);
    }

    public void listarAtendimentosParaView() {
        view.atualizarTabelaAtendimentos(atendimentoController.listarTodosAtendimentos());
    }

    public String editarAtendimentoAdmin(Long id, String novoStatus) {
        return atendimentoController.editarAtendimentoAdmin(id, novoStatus);
    }

    public String excluirAtendimentoAdmin(Long id) {
        return atendimentoController.excluirAtendimentoAdmin(id);
    }

    public List<String> listarCategorias() {
        return dispositivoController.listarCategorias();
    }
}

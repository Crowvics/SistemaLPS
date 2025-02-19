package controller;

import model.entities.Atendimento;
import model.entities.Dispositivo;

import java.util.List;

public class ClienteController {

    private final String clienteEmail;
    private final AtendimentoController atendimentoController;
    private final DispositivoController dispositivoController;

    public ClienteController(String clienteEmail) {
        this.clienteEmail = clienteEmail;

        this.atendimentoController = new AtendimentoController();

        this.dispositivoController = new DispositivoController(atendimentoController);

        this.atendimentoController.setDispositivoController(dispositivoController);
    }

    public List<Dispositivo> listarDispositivos() {
        return dispositivoController.listarDispositivosPorCliente(clienteEmail);
    }

    public String adicionarDispositivo(String nome, String categoria, String marca, String modelo, String descricao) {
        return dispositivoController.adicionarDispositivo(clienteEmail, nome, categoria, marca, modelo, descricao);
    }

    public String editarDispositivo(Long id, String nome, String categoria, String marca, String modelo, String descricao) {
        return dispositivoController.editarDispositivoCliente(id, nome, categoria, marca, modelo, descricao);
    }

    public String excluirDispositivo(Long id) {
        return dispositivoController.excluirDispositivo(id, clienteEmail);
    }

    public List<String> listarCategorias() {
        return dispositivoController.listarCategorias();
    }

    public List<String> getDispositivosParaComboBox() {
        return dispositivoController.listarDispositivosPorCliente(clienteEmail)
                .stream()
                .map(Dispositivo::getNomeDispositivo)
                .toList();
    }

    public String solicitarAtendimento(String dispositivoNome, String problema) {
        return atendimentoController.adicionarAtendimento(dispositivoNome, clienteEmail, problema);
    }

    public String editarAtendimento(Long atendimentoId, String novoDispositivoNome, String novoProblema) {
        return atendimentoController.editarAtendimentoCliente(atendimentoId, novoDispositivoNome, novoProblema, clienteEmail);
    }

    public String excluirAtendimento(Long atendimentoId) {
        return atendimentoController.excluirAtendimentoCliente(atendimentoId);
    }

    public List<Atendimento> listarAtendimentos() {
        return atendimentoController.listarAtendimentosCliente(clienteEmail);
    }

    public Long getIdDispositivoPorNome(String nomeDispositivo) {
        Dispositivo dispositivo = dispositivoController.buscarDispositivoPorNome(clienteEmail, nomeDispositivo);
        return (dispositivo != null) ? dispositivo.getId() : null;
    }

    public String enviarFeedback(Long atendimentoId, String nota) {
        return atendimentoController.registrarFeedback(atendimentoId, nota);
    }
}

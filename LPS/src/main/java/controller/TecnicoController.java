package controller;

import model.entities.Atendimento;
import view.TecnicoView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TecnicoController {

    private final TecnicoView view;
    private final PecaController pecaController;
    private final DispositivoController dispositivoController;
    private final AtendimentoController atendimentoController;
    private final RelatorioController relatorioController;
    private final Map<String, Integer> pecasSelecionadas;

    public TecnicoController(TecnicoView view) {
        this.view = view;
        this.pecaController = new PecaController();

        this.atendimentoController = new AtendimentoController();
        this.dispositivoController = new DispositivoController(atendimentoController);
        this.atendimentoController.setDispositivoController(dispositivoController);

        this.relatorioController = new RelatorioController(this.atendimentoController, this.pecaController);

        this.pecasSelecionadas = new HashMap<>();
    }

    public void listarPecasParaView() {
        view.atualizarTabelaPecas(pecaController.listarPecas());
    }

    public void listarDispositivosParaView() {
        view.atualizarTabelaDispositivos(dispositivoController.listarDispositivos());
    }

    public void listarAtendimentosPendentesParaView() {
        List<Atendimento> atendimentosEmAndamento = atendimentoController.listarAtendimentosTecnico();
        view.atualizarTabelaAtendimentos(atendimentosEmAndamento);
        view.atualizarListaAtendimentosPendentes(atendimentosEmAndamento);
    }

    public void listarPecasDisponiveisParaView() {
        view.atualizarListaPecasDisponiveis(pecaController.listarPecas());
    }

    public String adicionarPecaLista(String nomePeca, String quantidadeStr) {
        try {
            String mensagem = relatorioController.adicionarPecaRelatorio(nomePeca, quantidadeStr, pecasSelecionadas);
            atualizarListaPecasSelecionadasNaView();
            return mensagem;
        } catch (Exception e) {
            return "Erro ao adicionar peça: " + e.getMessage();
        }
    }

    public String removerPecaDoRelatorio(String nomePeca) {
        if (pecasSelecionadas.containsKey(nomePeca)) {
            pecasSelecionadas.remove(nomePeca);
            atualizarListaPecasSelecionadasNaView();
            return "Peça '" + nomePeca + "' removida do relatório.";
        }
        return "Erro - Peça não encontrada na lista.";
    }

    private void atualizarListaPecasSelecionadasNaView() {
        view.atualizarListaPecasSelecionadas();
    }

    public String enviarRelatorioManutencao(String atendimentoSelecionado, String descricaoServico) {
        String mensagem = relatorioController.registrarRelatorio(atendimentoSelecionado, descricaoServico, pecasSelecionadas);

        if (mensagem.startsWith("Relatório registrado")) {
            pecasSelecionadas.clear();
            atualizarListaPecasSelecionadasNaView();
        }

        return mensagem;
    }

    public void listarHistoricoRelatoriosParaView() {
        view.atualizarTabelaHistoricoRelatorios(relatorioController.listarRelatorios());
    }

    public List<String> getPecasSelecionadasFormatada() {
        return pecasSelecionadas.entrySet()
                .stream()
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.toList());
    }

    public String excluirRelatorio(Long idRelatorio) {
        return relatorioController.excluirRelatorioComoTecnico(idRelatorio);
    }

    public Map<String, Integer> getPecasSelecionadas() {
        return new HashMap<>(pecasSelecionadas);
    }

}

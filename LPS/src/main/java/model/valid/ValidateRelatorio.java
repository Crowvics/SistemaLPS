package model.valid;

import model.entities.Atendimento;
import model.entities.Relatorio;
import model.exception.RelatorioException;

import java.util.Map;

public class ValidateRelatorio {

    public void validarEntradaRelatorio(Atendimento atendimento, String descricaoManutencao, Map<String, Integer> pecasUtilizadas, Relatorio relatorioExistente) {
        validarAtendimentoParaRelatorio(atendimento);
        validarDescricaoManutencao(descricaoManutencao);
        validarPecasUtilizadas(pecasUtilizadas);
        validarRelatorioNaoExiste(relatorioExistente);
    }

    private void validarAtendimentoParaRelatorio(Atendimento atendimento) {
        if (atendimento == null) {
            throw new RelatorioException("Erro - Atendimento não encontrado.");
        }

        if (!"Em andamento".equalsIgnoreCase(atendimento.getStatus().trim())) {
            throw new RelatorioException("Erro - Apenas atendimentos 'Em andamento' podem ter relatórios.");
        }
    }

    private void validarPecasUtilizadas(Map<String, Integer> pecasUtilizadas) {
        if (pecasUtilizadas == null || pecasUtilizadas.isEmpty()) {
            throw new RelatorioException("Erro - Nenhuma peça foi adicionada ao relatório.");
        }

        for (Map.Entry<String, Integer> peca : pecasUtilizadas.entrySet()) {
            validarPecaQuantidade(peca.getKey(), peca.getValue());
        }
    }

    public void validarPecaQuantidade(String nomePeca, int quantidade) {
        if (nomePeca == null || nomePeca.trim().isEmpty()) {
            throw new RelatorioException("Erro - Nome da peça inválido.");
        }

        if (quantidade <= 0) {
            throw new RelatorioException("Erro - Quantidade inválida para a peça: " + nomePeca);
        }
    }

    public void validarPecaParaAdicionar(Map<String, Integer> pecasSelecionadas, String nomePeca, int quantidade) {
        validarPecaQuantidade(nomePeca, quantidade);

        if (pecasSelecionadas.containsKey(nomePeca)) {
            throw new RelatorioException("Erro - A peça '" + nomePeca + "' já foi adicionada ao relatório.");
        }
    }

    public void validarPecaParaRemover(Map<String, Integer> pecasSelecionadas, String nomePeca) {
        if (!pecasSelecionadas.containsKey(nomePeca)) {
            throw new RelatorioException("Erro - A peça '" + nomePeca + "' não está no relatório.");
        }
    }

    private void validarDescricaoManutencao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new RelatorioException("Erro - A descrição da manutenção não pode estar vazia.");
        }

        if (descricao.length() < 10 || descricao.length() > 500) {
            throw new RelatorioException("Erro - A descrição da manutenção deve ter entre 10 e 500 caracteres.");
        }
    }

    private void validarRelatorioNaoExiste(Relatorio relatorioExistente) {
        if (relatorioExistente != null) {
            throw new RelatorioException("Erro - Já existe um relatório para este atendimento.");
        }
    }

    public void validarRelatorioExiste(Relatorio relatorio) {
        if (relatorio == null) {
            throw new RelatorioException("Erro - Relatório não encontrado.");
        }
    }

    public void validarExclusaoRelatorioPorTecnico(Relatorio relatorio) {
        if (relatorio == null) {
            throw new RelatorioException("Erro - Relatório não encontrado.");
        }

        if (!"Em andamento".equalsIgnoreCase(relatorio.getAtendimento().getStatus().trim())) {
            throw new RelatorioException("Erro - Técnicos só podem excluir relatórios com atendimento 'Em andamento'.");
        }
    }

    public void validarExclusaoRelatorioPorAdministrador(Relatorio relatorio) {
        if (relatorio == null) {
            throw new RelatorioException("Erro - Relatório não encontrado.");
        }
    }

    public void validarEdicaoRelatorio(Relatorio relatorio) {
        if (relatorio == null) {
            throw new RelatorioException("Erro - Relatório não encontrado.");
        }

        if (!"Em andamento".equalsIgnoreCase(relatorio.getAtendimento().getStatus().trim())) {
            throw new RelatorioException("Erro - Apenas relatórios de atendimentos 'Em andamento' podem ser editados.");
        }
    }

    public void validarAlteracaoStatus(Atendimento atendimento, String novoStatus) {
        if (atendimento == null) {
            throw new RelatorioException("Erro - Atendimento não encontrado.");
        }

        if (!"Finalizado".equalsIgnoreCase(novoStatus.trim()) && !"Em andamento".equalsIgnoreCase(novoStatus.trim())) {
            throw new RelatorioException("Erro - O status do atendimento só pode ser alterado para 'Finalizado' ou 'Em andamento'.");
        }
    }

    public void validarAlteracaoPecas(Map<String, Integer> pecasAntigas, Map<String, Integer> pecasNovas) {
        if (pecasAntigas.equals(pecasNovas)) {
            throw new RelatorioException("Erro - Nenhuma alteração foi feita nas peças.");
        }
    }

    public void validarDependenciasController(Object controller, String nomeController) {
        if (controller == null) {
            throw new RelatorioException("Erro - " + nomeController + " não pode ser nulo.");
        }
    }

}

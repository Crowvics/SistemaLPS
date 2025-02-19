package controller;

import model.dao.RelatorioDAO;
import model.entities.Relatorio;
import model.entities.Atendimento;
import model.exception.RelatorioException;

import java.util.List;
import java.util.Map;
import model.valid.ValidateRelatorio;

public class RelatorioController {

    private final RelatorioDAO relatorioDAO;
    private final AtendimentoController atendimentoController;
    private final PecaController pecaController;
    private final ValidateRelatorio validateRelatorio;

    public RelatorioController(AtendimentoController atendimentoController, PecaController pecaController) {
        this.relatorioDAO = new RelatorioDAO();
        this.atendimentoController = atendimentoController;
        this.pecaController = pecaController;
        this.validateRelatorio = new ValidateRelatorio();
    }

    public List<Relatorio> listarRelatorios() {
        List<Relatorio> relatorios = relatorioDAO.listarRelatorios();

        for (Relatorio relatorio : relatorios) {
            relatorio.getAtendimento().getStatus();
        }

        return relatorios;
    }

    public String adicionarPecaRelatorio(String nomePeca, String quantidadeStr, Map<String, Integer> pecasSelecionadas) {
        return pecaController.adicionarPecaRelatorio(nomePeca, quantidadeStr, pecasSelecionadas);
    }

    public String registrarRelatorio(String atendimentoSelecionado, String descricao, Map<String, Integer> pecasUtilizadas) {
        try {
            Long atendimentoId = Long.parseLong(atendimentoSelecionado.split(" - ")[0].trim());
            Atendimento atendimento = atendimentoController.buscarAtendimento(atendimentoId);

            if (atendimento == null) {
                return "Erro - Atendimento não encontrado.";
            }

            Relatorio relatorioExistente = buscarRelatorioPorAtendimento(atendimentoId);
            validateRelatorio.validarEntradaRelatorio(atendimento, descricao, pecasUtilizadas, relatorioExistente);

            for (Map.Entry<String, Integer> peca : pecasUtilizadas.entrySet()) {
                boolean estoqueAtualizado = pecaController.reduzirEstoque(peca.getKey(), peca.getValue());
                if (!estoqueAtualizado) {
                    return "Erro - Estoque insuficiente para a peça: " + peca.getKey();
                }
            }

            Relatorio novoRelatorio = new Relatorio(atendimento, descricao, pecasUtilizadas);
            relatorioDAO.save(novoRelatorio);

            return "Relatório registrado com sucesso!";
        } catch (RelatorioException e) {
            return "Erro ao registrar relatório: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro inesperado ao registrar relatório.";
        }
    }

    public Relatorio buscarRelatorioPorAtendimento(Long atendimentoId) {
        return relatorioDAO.findByAtendimentoId(atendimentoId);
    }

    public Relatorio buscarRelatorioPorId(Long relatorioId) {
        return relatorioDAO.find(relatorioId);
    }

    public String excluirRelatorioComoTecnico(Long id) {
        try {
            Relatorio relatorio = relatorioDAO.find(id);
            validateRelatorio.validarRelatorioExiste(relatorio);
            validateRelatorio.validarExclusaoRelatorioPorTecnico(relatorio);

            pecaController.restaurarEstoque(relatorio.getPecasUtilizadas());

            relatorioDAO.delete(id);

            return "Relatório excluído com sucesso!";
        } catch (RelatorioException e) {
            return "Erro ao excluir relatório: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro inesperado ao excluir relatório.";
        }
    }

    public String excluirRelatorioComoAdministrador(Long id) {
        try {
            Relatorio relatorio = relatorioDAO.find(id);
            validateRelatorio.validarRelatorioExiste(relatorio);
            validateRelatorio.validarExclusaoRelatorioPorAdministrador(relatorio);

            if (!"Finalizado".equalsIgnoreCase(relatorio.getAtendimento().getStatus().trim())) {
                pecaController.restaurarEstoque(relatorio.getPecasUtilizadas());
            }

            relatorioDAO.delete(id);
            return "Relatório excluído com sucesso!";
        } catch (RelatorioException e) {
            return "Erro ao excluir relatório: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro inesperado ao excluir relatório.";
        }
    }

    public void restaurarEstoqueSeNecessario(Relatorio relatorio) {
        if (!"Finalizado".equalsIgnoreCase(relatorio.getAtendimento().getStatus().trim())) {
            pecaController.restaurarEstoque(relatorio.getPecasUtilizadas());
        }
    }

    public boolean atendimentoPossuiRelatorio(Long atendimentoId) {
        return relatorioDAO.findByAtendimentoId(atendimentoId) != null;
    }

    public void excluirRelatorioPorAtendimento(Long atendimentoId) {
        Relatorio relatorio = relatorioDAO.findByAtendimentoId(atendimentoId);
        if (relatorio != null) {
            relatorioDAO.delete(relatorio.getId());
        }
    }

}

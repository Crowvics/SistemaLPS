package controller;

import model.dao.PecaDAO;
import model.entities.Peca;
import model.valid.ValidatePeca;
import view.AdministradorView;

import java.util.List;
import java.util.Map;

public class PecaController {

    private final PecaDAO pecaDAO;
    private final ValidatePeca validatePeca;

    public PecaController() {
        this.pecaDAO = PecaDAO.getInstance();
        this.validatePeca = new ValidatePeca();
    }

    public void listarPecasParaView(AdministradorView view) {
        if (view != null) {
            view.atualizarTabelaPecas(listarPecas());
        }
    }

    public List<Peca> listarPecas() {
        return pecaDAO.findAll();
    }

    public String adicionarPeca(String codigo, String nome, String categoria, String descricao, String quantidade) {
        try {
            List<Peca> pecasExistentes = listarPecas();

            validatePeca.validarUnicidadeCodigo(codigo, null, pecasExistentes);
            validatePeca.validarUnicidadeNome(nome, null, pecasExistentes);

            Peca novaPeca = validatePeca.validaCamposEntrada(codigo, nome, categoria, descricao, quantidade);

            pecaDAO.save(novaPeca);

            return "Peça cadastrada com sucesso!";
        } catch (Exception e) {
            return "Erro ao adicionar peça: " + e.getMessage();
        }
    }

    public Peca getPecaSelecionada(int selectedRow) {
        List<Peca> pecas = listarPecas();
        return validatePeca.validarPecaSelecionada(pecas, selectedRow);
    }

    public String editarPeca(Long id, String codigo, String nome, String categoria, String descricao, String quantidade) {
        try {
            Peca pecaExistente = pecaDAO.find(id);
            validatePeca.validarPecaExiste(pecaExistente);

            List<Peca> pecasExistentes = listarPecas();
            pecasExistentes.remove(pecaExistente);

            validatePeca.validarUnicidadeCodigo(codigo, id, pecasExistentes);
            validatePeca.validarUnicidadeNome(nome, id, pecasExistentes);

            Peca pecaAtualizada = validatePeca.validaCamposEntrada(codigo, nome, categoria, descricao, quantidade);

            pecaExistente.setCodigoPeca(pecaAtualizada.getCodigoPeca());
            pecaExistente.setNome(pecaAtualizada.getNome());
            pecaExistente.setCategoria(pecaAtualizada.getCategoria());
            pecaExistente.setDescricao(pecaAtualizada.getDescricao());
            pecaExistente.setQuantidade(pecaAtualizada.getQuantidade());

            pecaDAO.update(pecaExistente);
            return "Peça atualizada com sucesso!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String excluirPeca(Long id) {
        try {
            Peca pecaExistente = pecaDAO.find(id);
            validatePeca.validarPecaExiste(pecaExistente);
            pecaDAO.delete(id);
            return "Peça excluída com sucesso!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String atualizarQuantidadePeca(String nomePeca, int quantidadeAlteracao) {
        try {
            Peca peca = pecaDAO.findByName(nomePeca);
            validatePeca.validarPecaExiste(peca);
            validatePeca.validarQuantidadeDisponivel(peca, peca.getQuantidade() + quantidadeAlteracao);

            peca.setQuantidade(peca.getQuantidade() + quantidadeAlteracao);
            pecaDAO.update(peca);

            return "Peça '" + nomePeca + "' atualizada com sucesso!";
        } catch (Exception e) {
            return "Erro ao atualizar peça: " + e.getMessage();
        }
    }

    public String validarQuantidadeDisponivel(Map<String, Integer> pecasUtilizadas) {
        try {
            for (Map.Entry<String, Integer> pecaUsada : pecasUtilizadas.entrySet()) {
                Peca peca = pecaDAO.findByName(pecaUsada.getKey());
                validatePeca.validarPecaExiste(peca);
                validatePeca.validarQuantidadeDisponivel(peca, peca.getQuantidade() - pecaUsada.getValue());
            }
            return "Todas as peças possuem quantidade suficiente.";
        } catch (Exception e) {
            return "Erro ao validar quantidade de peças: " + e.getMessage();
        }
    }

    public boolean reduzirEstoque(String nomePeca, int quantidade) {
        try {
            Peca peca = pecaDAO.findByName(nomePeca);

            validatePeca.validarReducaoEstoque(peca, quantidade);

            peca.setQuantidade(peca.getQuantidade() - quantidade);
            pecaDAO.update(peca);

            return true;
        } catch (Exception e) {
            System.out.println("Erro ao reduzir estoque: " + e.getMessage());
            return false;
        }
    }

    public void reduzirEstoqueDefinitivamente(Map<String, Integer> pecasUtilizadas) {
        for (Map.Entry<String, Integer> peca : pecasUtilizadas.entrySet()) {
            Peca pecaBanco = pecaDAO.findByName(peca.getKey());
            validatePeca.validarPecaExiste(pecaBanco);
            validatePeca.validarReducaoEstoque(pecaBanco, peca.getValue());

            pecaBanco.setQuantidade(pecaBanco.getQuantidade() - peca.getValue());
            pecaDAO.update(pecaBanco);
        }
    }

    public void restaurarEstoque(Map<String, Integer> pecasUtilizadas) {
        for (Map.Entry<String, Integer> peca : pecasUtilizadas.entrySet()) {
            Peca pecaBanco = pecaDAO.findByName(peca.getKey());
            if (pecaBanco != null) {
                pecaBanco.setQuantidade(pecaBanco.getQuantidade() + peca.getValue());
                pecaDAO.update(pecaBanco);
            }
        }
    }

    public String adicionarPecaRelatorio(String nomePeca, String quantidadeStr, Map<String, Integer> pecasSelecionadas) {
        try {
            int quantidade = Integer.parseInt(quantidadeStr);

            Peca peca = pecaDAO.findByName(nomePeca);

            validatePeca.validarPecaExiste(peca);
            validatePeca.validarReducaoEstoque(peca, quantidade);

            pecasSelecionadas.put(nomePeca, quantidade);

            return "Peça '" + nomePeca + "' adicionada ao relatório com sucesso!";
        } catch (NumberFormatException e) {
            return "Erro: Quantidade inválida.";
        } catch (Exception e) {
            return "Erro ao adicionar peça ao relatório: " + e.getMessage();
        }
    }

}

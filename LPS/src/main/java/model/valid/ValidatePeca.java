package model.valid;

import model.entities.Peca;
import model.exception.PecaException;

import java.util.List;

public class ValidatePeca {

    public void validarPecaExiste(Peca peca) {
        if (peca == null) {
            throw new PecaException("Erro - Peça não encontrada.");
        }
    }

    public void validarQuantidadeDisponivel(Peca peca, int novaQuantidade) {
        if (novaQuantidade < 0) {
            throw new PecaException("Erro - Estoque insuficiente para a peça '" + peca.getNome() + "'. Disponível: "
                    + peca.getQuantidade() + ", Requerido: " + novaQuantidade);
        }
    }

    public Peca validaCamposEntrada(String codigo, String nome, String categoria, String descricao, String quantidade) {
        validarCodigo(codigo);
        validarNome(nome);
        validarCategoria(categoria);

        int qtd;
        try {
            qtd = Integer.parseInt(quantidade);
        } catch (NumberFormatException e) {
            throw new PecaException("Erro - Quantidade inválida. Deve ser um número inteiro.");
        }

        validarQuantidade(qtd);

        Peca peca = new Peca();
        peca.setCodigoPeca(codigo);
        peca.setNome(nome);
        peca.setCategoria(categoria);
        peca.setDescricao(descricao);
        peca.setQuantidade(qtd);

        return peca;
    }

    public Peca validarPecaSelecionada(List<Peca> pecas, int selectedRow) {
        if (selectedRow < 0 || selectedRow >= pecas.size()) {
            throw new PecaException("Erro - Nenhuma peça selecionada.");
        }
        return pecas.get(selectedRow);
    }

    private void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new PecaException("Erro - Campo vazio: 'Código'.");
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new PecaException("Erro - Campo vazio: 'Nome'.");
        }
    }

    private void validarCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new PecaException("Erro - Campo vazio: 'Categoria'.");
        }
    }

    public void validarQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new PecaException("Erro - A quantidade deve ser maior que zero.");
        }
    }

    public void validarReducaoEstoque(Peca peca, int quantidade) {
        validarPecaExiste(peca);
        validarQuantidade(quantidade);

        if (peca.getQuantidade() < quantidade) {
            throw new PecaException("Erro - Estoque insuficiente para a peça '" + peca.getNome() + "'. Disponível: "
                    + peca.getQuantidade() + ", Requerido: " + quantidade);
        }
    }

    public void validarUnicidadeCodigo(String codigo, Long idAtual, List<Peca> pecasExistentes) {
        for (Peca peca : pecasExistentes) {
            if (peca.getCodigoPeca().equalsIgnoreCase(codigo) && (idAtual == null || !peca.getId().equals(idAtual))) {
                throw new PecaException("Erro - Já existe uma peça cadastrada com o código '" + codigo + "'.");
            }
        }
    }

    public void validarUnicidadeNome(String nome, Long idAtual, List<Peca> pecasExistentes) {
        for (Peca peca : pecasExistentes) {
            if (peca.getNome().equalsIgnoreCase(nome) && (idAtual == null || !peca.getId().equals(idAtual))) {
                throw new PecaException("Erro - Já existe uma peça cadastrada com o nome '" + nome + "'.");
            }
        }
    }

}

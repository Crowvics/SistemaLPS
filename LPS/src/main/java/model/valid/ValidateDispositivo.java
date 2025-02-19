package model.valid;

import model.entities.Dispositivo;
import model.exception.DispositivoException;

import java.util.Arrays;
import java.util.List;

public class ValidateDispositivo {

    public void validarDadosDispositivo(String nomeDispositivo, String categoria, String marca, String modelo, String descricao) {
        validarNomeDispositivo(nomeDispositivo);
        validarCategoria(categoria);
        validarMarca(marca);
        validarModelo(modelo);
        validarDescricao(descricao);
    }

    public List<String> getCategoriasPermitidas() {
        return Arrays.asList("Celular", "Notebook", "Impressora", "Outro");
    }

    public void validarNomeDispositivo(String nomeDispositivo) {
        if (nomeDispositivo == null || nomeDispositivo.trim().isEmpty()) {
            throw new DispositivoException("Erro - Campo vazio: 'Nome do Dispositivo'.");
        }
        if (nomeDispositivo.length() < 3 || nomeDispositivo.length() > 100) {
            throw new DispositivoException("Erro - O nome do dispositivo deve ter entre 3 e 100 caracteres.");
        }
    }

    public void validarCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty() || !getCategoriasPermitidas().contains(categoria)) {
            throw new DispositivoException("Erro - Categoria inválida. Escolha entre: Celular, Notebook, Impressora ou Outro.");
        }
    }

    public void validarMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            throw new DispositivoException("Erro - Campo vazio: 'Marca'.");
        }
    }

    public void validarModelo(String modelo) {
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new DispositivoException("Erro - Campo vazio: 'Modelo'.");
        }
    }

    public void validarDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new DispositivoException("Erro - Campo vazio: 'Descrição'.");
        }
    }

    public void validarPertinenciaDispositivoCliente(Dispositivo dispositivo, String clienteEmail) {
        if (!dispositivo.getClienteEmail().equals(clienteEmail)) {
            throw new DispositivoException("Erro - O dispositivo não pertence ao cliente informado.");
        }
    }

    public void validarNomeDuplicado(Dispositivo dispositivoExistente, Long id) {
        if (dispositivoExistente != null && !dispositivoExistente.getId().equals(id)) {
            throw new DispositivoException("Erro: Já existe um dispositivo com o mesmo nome para este cliente.");
        }
    }

    public void validarSemAtendimentoAtivo(boolean possuiAtendimento) {
        if (possuiAtendimento) {
            throw new DispositivoException("Erro - O dispositivo possui um atendimento ativo e não pode ser alterado ou excluído.");
        }
    }

    public Dispositivo validarDispositivoExiste(Dispositivo dispositivo) {
        if (dispositivo == null) {
            throw new DispositivoException("Erro - Dispositivo não encontrado.");
        }
        return dispositivo;
    }

    public void validarCamposEdicao(String nome, String categoria, String marca, String modelo, String descricao) {
        validarDadosDispositivo(nome, categoria, marca, modelo, descricao);
    }

    public void validarClienteExiste(boolean clienteExiste) {
        if (!clienteExiste) {
            throw new DispositivoException("Erro: O e-mail do cliente não está cadastrado no sistema.");
        }
    }
}

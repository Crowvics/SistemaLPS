package model.valid;

import java.util.Arrays;
import java.util.List;
import model.entities.Atendimento;
import model.entities.Dispositivo;
import model.exception.AtendimentoException;

public class ValidateAtendimento {

    public void validarCamposAtendimento(String clienteEmail, Long dispositivoId, String problema) {
        if (clienteEmail == null || clienteEmail.trim().isEmpty()) {
            throw new AtendimentoException("Erro - Cliente não identificado.");
        }
        if (dispositivoId == null) {
            throw new AtendimentoException("Erro - Nenhum dispositivo selecionado.");
        }
        validarDescricaoProblema(problema);
    }

    public void validarDispositivoEncontrado(Dispositivo dispositivo) {
        if (dispositivo == null) {
            throw new AtendimentoException("Erro - Dispositivo não encontrado.");
        }
    }

    public Atendimento validarAtendimentoExiste(Atendimento atendimento) {
        if (atendimento == null) {
            throw new AtendimentoException("Erro - Atendimento não encontrado.");
        }
        return atendimento;
    }

    public void validarDescricaoProblema(String problema) {
        if (problema == null || problema.trim().isEmpty()) {
            throw new AtendimentoException("Erro - O campo 'Descrição do Problema' não pode estar vazio.");
        }
    }

    public void validarAcessoCliente(Atendimento atendimento, boolean edicao) {
        validarAtendimentoExiste(atendimento);
        if (!"Aguardando análise".equalsIgnoreCase(atendimento.getStatus().trim())) {
            throw new AtendimentoException(
                    "Erro - Apenas atendimentos 'Aguardando análise' podem ser " + (edicao ? "editados" : "excluídos") + " pelo cliente."
            );
        }
    }

    public void validarAcessoAdmin(Atendimento atendimento) {
        validarAtendimentoExiste(atendimento);
    }

    public void validarDispositivoSemAtendimento(Atendimento atendimento) {
        if (atendimento != null) {
            throw new AtendimentoException("Erro - Este dispositivo já possui um atendimento registrado.");
        }
    }

    public void validarFinalizacaoAtendimento(Atendimento atendimento) {
        validarAtendimentoExiste(atendimento);
        if (!"Em andamento".equalsIgnoreCase(atendimento.getStatus().trim())) {
            throw new AtendimentoException("Erro - Apenas atendimentos 'Em andamento' podem ser finalizados.");
        }
    }

    public void validarStatusAtendimento(String status) {
        List<String> statusPermitidos = Arrays.asList("Aguardando análise", "Em andamento", "Finalizado");
        if (status == null || status.trim().isEmpty() || !statusPermitidos.contains(status)) {
            throw new AtendimentoException("Erro - Status inválido. Deve ser: 'Aguardando análise', 'Em andamento' ou 'Finalizado'.");
        }
    }

    public void validarAtendimentoParaFeedback(Atendimento atendimento) {
        validarAtendimentoExiste(atendimento);

        if (!"Finalizado".equalsIgnoreCase(atendimento.getStatus().trim())) {
            throw new AtendimentoException("Erro - Apenas atendimentos 'Finalizado' podem receber feedback.");
        }

        if (atendimento.getFeedbackNota() != null && !atendimento.getFeedbackNota().equals(" - ")) {
            throw new AtendimentoException("Erro - Feedback já foi enviado para este atendimento.");
        }
    }

    public void validarNotaFeedback(String nota) {
        int notaNumerica = Integer.parseInt(nota);
        if (notaNumerica < 1 || notaNumerica > 10) {
            throw new AtendimentoException("Erro - A nota do feedback deve estar entre 1 e 10.");
        }
    }

    public void validarAtendimentoSemRelatorio(boolean possuiRelatorio) {
        if (possuiRelatorio) {
            throw new AtendimentoException("Erro - O atendimento possui um relatório associado. Exclua o relatório antes de excluí-lo.");
        }
    }

}

package controller;

import model.dao.AtendimentoDAO;
import model.entities.Atendimento;
import model.entities.Dispositivo;
import model.exception.AtendimentoException;
import model.valid.ValidateAtendimento;

import java.util.List;
import java.util.stream.Collectors;
import model.entities.Relatorio;

public class AtendimentoController {

    private final AtendimentoDAO atendimentoDAO;
    private final ValidateAtendimento validateAtendimento;
    private DispositivoController dispositivoController;
    private RelatorioController relatorioController;

    public AtendimentoController() {
        this.atendimentoDAO = AtendimentoDAO.getInstance();
        this.validateAtendimento = new ValidateAtendimento();
        this.relatorioController = null;
    }

    public void setRelatorioController(RelatorioController relatorioController) {
        this.relatorioController = relatorioController;
    }

    public void setDispositivoController(DispositivoController dispositivoController) {
        this.dispositivoController = dispositivoController;
    }

    public String adicionarAtendimento(String dispositivoNome, String clienteEmail, String problema) {
        try {
            if (dispositivoController == null) {
                throw new IllegalStateException("Erro: DispositivoController não foi configurado.");
            }

            Dispositivo dispositivo = dispositivoController.buscarDispositivoPorNome(clienteEmail, dispositivoNome);
            validateAtendimento.validarDispositivoEncontrado(dispositivo);

            Atendimento atendimentoExistente = atendimentoDAO.findByDispositivoId(dispositivo.getId());
            validateAtendimento.validarDispositivoSemAtendimento(atendimentoExistente);
            validateAtendimento.validarCamposAtendimento(clienteEmail, dispositivo.getId(), problema);

            Atendimento atendimento = new Atendimento();
            atendimento.setDispositivo(dispositivo);
            atendimento.setProblema(problema);
            atendimento.setStatus("Aguardando análise");

            atendimentoDAO.save(atendimento);
            return "Atendimento cadastrado com sucesso!";
        } catch (IllegalStateException e) {
            return e.getMessage();
        } catch (AtendimentoException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Erro inesperado ao cadastrar atendimento: " + e.getMessage();
        }
    }

    public List<Atendimento> listarAtendimentosCliente(String clienteEmail) {
        return atendimentoDAO.findByClienteEmail(clienteEmail);
    }

    public List<Atendimento> listarTodosAtendimentos() {
        return atendimentoDAO.findTodosAtendimentos();
    }

    public List<Atendimento> listarAtendimentosEmAndamento() {
        return atendimentoDAO.findAtendimentosEmAndamento();
    }

    public String editarAtendimentoCliente(Long atendimentoId, String novoDispositivoNome, String novoProblema, String clienteEmail) {
        try {
            Atendimento atendimento = buscarAtendimento(atendimentoId);
            validateAtendimento.validarAcessoCliente(atendimento, true);
            validateAtendimento.validarDescricaoProblema(novoProblema);

            Dispositivo dispositivoOriginal = atendimento.getDispositivo();

            Dispositivo novoDispositivo = dispositivoController.buscarDispositivoPorNome(clienteEmail, novoDispositivoNome);
            validateAtendimento.validarDispositivoEncontrado(novoDispositivo);

            if (!dispositivoOriginal.getId().equals(novoDispositivo.getId())) {
                Atendimento atendimentoExistente = atendimentoDAO.findByDispositivoId(novoDispositivo.getId());
                validateAtendimento.validarDispositivoSemAtendimento(atendimentoExistente);
            }

            atendimento.setDispositivo(novoDispositivo);
            atendimento.setProblema(novoProblema);
            atendimentoDAO.update(atendimento);
            return "Atendimento atualizado com sucesso!";
        } catch (AtendimentoException e) {
            return "Erro ao editar atendimento: " + e.getMessage();
        }
    }

    public String editarAtendimentoAdmin(Long atendimentoId, String novoStatus) {
        try {
            Atendimento atendimento = buscarAtendimento(atendimentoId);
            validateAtendimento.validarAcessoAdmin(atendimento);
            validateAtendimento.validarStatusAtendimento(novoStatus);

            atendimento.setStatus(novoStatus);
            atendimentoDAO.update(atendimento);

            return "Atendimento atualizado com sucesso!";
        } catch (AtendimentoException e) {
            return "Erro ao editar atendimento: " + e.getMessage();
        }
    }


    public String excluirAtendimentoCliente(Long id) {
        try {
            Atendimento atendimento = buscarAtendimento(id);
            validateAtendimento.validarAcessoCliente(atendimento, false);
            atendimentoDAO.delete(id);
            return "Atendimento excluído com sucesso!";
        } catch (AtendimentoException e) {
            return e.getMessage();
        }
    }

    public String excluirAtendimentoAdmin(Long atendimentoId) {
        try {
            Atendimento atendimento = buscarAtendimento(atendimentoId);
            validateAtendimento.validarAcessoAdmin(atendimento);

            boolean possuiRelatorio = atendimentoDAO.atendimentoPossuiRelatorio(atendimentoId);
            validateAtendimento.validarAtendimentoSemRelatorio(possuiRelatorio);

            atendimentoDAO.delete(atendimentoId);
            return "Atendimento excluído com sucesso!";
        } catch (AtendimentoException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Erro ao excluir atendimento: " + e.getMessage();
        }
    }

    public boolean possuiAtendimentoAtivo(Long dispositivoId) {
        return atendimentoDAO.findByDispositivoId(dispositivoId) != null;
    }

    public List<Atendimento> listarAtendimentosPorDispositivo(Long dispositivoId) {
        return atendimentoDAO.findByDispositivoIdList(dispositivoId);
    }

    public Atendimento buscarAtendimento(Long id) {
        Atendimento atendimento = atendimentoDAO.find(id);
        return validateAtendimento.validarAtendimentoExiste(atendimento);
    }

    public List<Atendimento> listarAtendimentosTecnico() {
        return atendimentoDAO.findAtendimentosEmAndamento()
                .stream()
                .filter(atendimento -> "Em andamento".equalsIgnoreCase(atendimento.getStatus()))
                .collect(Collectors.toList());
    }

    public String registrarFeedback(Long atendimentoId, String nota) {
        try {
            Atendimento atendimento = buscarAtendimento(atendimentoId);
            validateAtendimento.validarAtendimentoParaFeedback(atendimento);

            atendimento.setFeedbackNota(nota);
            atendimentoDAO.update(atendimento);

            return "Feedback registrado com sucesso!";
        } catch (AtendimentoException e) {
            return "Erro ao registrar feedback: " + e.getMessage();
        }
    }

    public boolean atendimentoPossuiFeedback(Long atendimentoId) {
        Atendimento atendimento = buscarAtendimento(atendimentoId);
        return atendimento.getFeedbackNota() != null && !atendimento.getFeedbackNota().equals(" - ");
    }

}

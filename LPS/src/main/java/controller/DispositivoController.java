package controller;

import model.dao.DispositivoDAO;
import model.entities.Dispositivo;
import model.exception.DispositivoException;
import model.valid.ValidateDispositivo;

import java.util.List;

public class DispositivoController {

    private final DispositivoDAO dispositivoDAO;
    private final UsuarioController usuarioController;
    private AtendimentoController atendimentoController;

    private final ValidateDispositivo validateDispositivo;

    public DispositivoController(AtendimentoController atendimentoController) {
        this.dispositivoDAO = DispositivoDAO.getInstance();
        this.usuarioController = new UsuarioController();
        this.atendimentoController = atendimentoController;
        this.validateDispositivo = new ValidateDispositivo();
    }

    public List<Dispositivo> listarDispositivos() {
        return dispositivoDAO.findAll();
    }

    public List<Dispositivo> listarDispositivosPorCliente(String clienteEmail) {
        return dispositivoDAO.findByClienteEmail(clienteEmail);
    }

    public String adicionarDispositivo(String clienteEmail, String nomeDispositivo, String categoria, String marca, String modelo, String descricao) {
        try {
            validateDispositivo.validarClienteExiste(usuarioController.clienteExiste(clienteEmail));
            validateDispositivo.validarDadosDispositivo(nomeDispositivo, categoria, marca, modelo, descricao);

            Dispositivo existente = dispositivoDAO.findByNomeEmailDispositivo(clienteEmail, nomeDispositivo);
            validateDispositivo.validarNomeDuplicado(existente, null);

            Dispositivo novoDispositivo = new Dispositivo(nomeDispositivo, categoria, marca, modelo, descricao, clienteEmail);
            dispositivoDAO.save(novoDispositivo);

            return "Dispositivo cadastrado com sucesso!";
        } catch (DispositivoException e) {
            return "Erro ao cadastrar dispositivo: " + e.getMessage();
        } catch (Exception e) {
            return "Erro inesperado ao cadastrar dispositivo.";
        }
    }

    public String editarDispositivoCliente(Long id, String novoNome, String novaCategoria, String novaMarca, String novoModelo, String novaDescricao) {
        try {
            Dispositivo dispositivo = validateDispositivo.validarDispositivoExiste(dispositivoDAO.find(id));
            validateDispositivo.validarPertinenciaDispositivoCliente(dispositivo, dispositivo.getClienteEmail());

            boolean possuiAtendimento = atendimentoController.possuiAtendimentoAtivo(id);
            validateDispositivo.validarSemAtendimentoAtivo(possuiAtendimento);

            validateDispositivo.validarCamposEdicao(novoNome, novaCategoria, novaMarca, novoModelo, novaDescricao);

            Dispositivo existente = dispositivoDAO.findByNomeEmailDispositivo(dispositivo.getClienteEmail(), novoNome);
            validateDispositivo.validarNomeDuplicado(existente, id);

            dispositivo.setNomeDispositivo(novoNome);
            dispositivo.setCategoria(novaCategoria);
            dispositivo.setMarca(novaMarca);
            dispositivo.setModelo(novoModelo);
            dispositivo.setDescricao(novaDescricao);

            dispositivoDAO.update(dispositivo);
            return "Dispositivo atualizado com sucesso!";
        } catch (DispositivoException e) {
            return "Erro ao editar dispositivo: " + e.getMessage();
        } catch (Exception e) {
            return "Erro inesperado ao editar dispositivo.";
        }
    }

    public String editarDispositivoAdmin(Long id, String novaCategoria, String novaMarca, String novoModelo, String novaDescricao) {
        try {
            Dispositivo dispositivo = validateDispositivo.validarDispositivoExiste(dispositivoDAO.find(id));

            boolean possuiAtendimento = atendimentoController.possuiAtendimentoAtivo(id);
            validateDispositivo.validarSemAtendimentoAtivo(possuiAtendimento);

            validateDispositivo.validarCamposEdicao(dispositivo.getNomeDispositivo(), novaCategoria, novaMarca, novoModelo, novaDescricao);

            dispositivo.setCategoria(novaCategoria);
            dispositivo.setMarca(novaMarca);
            dispositivo.setModelo(novoModelo);
            dispositivo.setDescricao(novaDescricao);

            dispositivoDAO.update(dispositivo);
            return "Dispositivo atualizado com sucesso!";
        } catch (DispositivoException e) {
            return "Erro ao editar dispositivo: " + e.getMessage();
        } catch (Exception e) {
            return "Erro inesperado ao editar dispositivo.";
        }
    }

    public String excluirDispositivo(Long id, String clienteEmail) {
        try {
            Dispositivo dispositivo = validateDispositivo.validarDispositivoExiste(dispositivoDAO.find(id));
            validateDispositivo.validarPertinenciaDispositivoCliente(dispositivo, clienteEmail);

            boolean possuiAtendimento = atendimentoController.possuiAtendimentoAtivo(id);
            validateDispositivo.validarSemAtendimentoAtivo(possuiAtendimento);

            dispositivoDAO.delete(id);
            return "Dispositivo excluído com sucesso!";
        } catch (DispositivoException e) {
            return "Erro ao excluir dispositivo: " + e.getMessage();
        } catch (Exception e) {
            return "Erro inesperado ao excluir dispositivo.";
        }
    }

    public String excluirDispositivoAdmin(Long id) {
        try {
            Dispositivo dispositivo = validateDispositivo.validarDispositivoExiste(dispositivoDAO.find(id));

            boolean possuiAtendimento = atendimentoController.possuiAtendimentoAtivo(id);
            validateDispositivo.validarSemAtendimentoAtivo(possuiAtendimento);

            dispositivoDAO.delete(id);
            return "Dispositivo excluído com sucesso!";
        } catch (DispositivoException e) {
            return "Erro ao excluir dispositivo: " + e.getMessage();
        } catch (Exception e) {
            return "Erro inesperado ao excluir dispositivo.";
        }
    }

    public void validarDispositivoExiste(Dispositivo dispositivo) {
        validateDispositivo.validarDispositivoExiste(dispositivo);
    }

    public Dispositivo buscarDispositivoPorNome(String clienteEmail, String nomeDispositivo) {
        return dispositivoDAO.findByNomeEmailDispositivo(clienteEmail, nomeDispositivo);
    }

    public List<String> listarCategorias() {
        return validateDispositivo.getCategoriasPermitidas();
    }

    public void setAtendimentoController(AtendimentoController atendimentoController) {
        this.atendimentoController = atendimentoController;
    }

}

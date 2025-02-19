package model.dao;

import factory.DatabaseJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.entities.Atendimento;

import java.util.List;

public class AtendimentoDAO {

    private static AtendimentoDAO instancia;

    private AtendimentoDAO() {
    }

    public static AtendimentoDAO getInstance() {
        if (instancia == null) {
            instancia = new AtendimentoDAO();
        }
        return instancia;
    }

    public void save(Atendimento atendimento) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();

            if (atendimento.getStatus() == null || atendimento.getStatus().trim().isEmpty()) {
                atendimento.setStatus("Aguardando análise");
            }

            em.persist(atendimento);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Atendimento atendimento) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(atendimento);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            Atendimento atendimento = em.find(Atendimento.class, id);
            if (atendimento != null) {
                em.remove(atendimento);
            } else {
                throw new RuntimeException("Erro: Atendimento inexistente.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Atendimento find(Long id) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            return em.find(Atendimento.class, id);
        } finally {
            em.close();
        }
    }

    public List<Atendimento> findByClienteEmail(String clienteEmail) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Atendimento> query = em.createQuery(
                    "SELECT a FROM Atendimento a JOIN FETCH a.dispositivo d WHERE d.clienteEmail = :clienteEmail",
                    Atendimento.class
            );
            query.setParameter("clienteEmail", clienteEmail);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Atendimento> findByClienteEmailAndStatus(String clienteEmail, String status) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Atendimento> query = em.createQuery(
                    "SELECT a FROM Atendimento a WHERE a.dispositivo.clienteEmail = :clienteEmail AND a.status = :status",
                    Atendimento.class
            );
            query.setParameter("clienteEmail", clienteEmail);
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public boolean atendimentoPossuiRelatorio(Long atendimentoId) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(r) FROM Relatorio r WHERE r.atendimento.id = :atendimentoId",
                    Long.class
            );
            query.setParameter("atendimentoId", atendimentoId);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    public Atendimento findByDispositivoNome(String clienteEmail, String dispositivoNome) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Atendimento> query = em.createQuery(
                    "SELECT a FROM Atendimento a JOIN a.dispositivo d WHERE d.clienteEmail = :clienteEmail AND d.nomeDispositivo = :dispositivoNome",
                    Atendimento.class
            );
            query.setParameter("clienteEmail", clienteEmail);
            query.setParameter("dispositivoNome", dispositivoNome);

            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    public boolean existeAtendimentoPendente(Long dispositivoId) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(a) FROM Atendimento a WHERE a.dispositivo.id = :dispositivoId AND a.status = 'Pendente'",
                    Long.class
            );
            query.setParameter("dispositivoId", dispositivoId);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    public Atendimento findByDispositivoId(Long dispositivoId) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Atendimento> query = em.createQuery(
                    "SELECT a FROM Atendimento a WHERE a.dispositivo.id = :dispositivoId",
                    Atendimento.class
            );
            query.setParameter("dispositivoId", dispositivoId);

            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    public List<Atendimento> findByDispositivoIdList(Long dispositivoId) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Atendimento> query = em.createQuery(
                    "SELECT a FROM Atendimento a WHERE a.dispositivo.id = :dispositivoId",
                    Atendimento.class
            );
            query.setParameter("dispositivoId", dispositivoId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void finalizarAtendimento(Long atendimentoId) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            Atendimento atendimento = em.find(Atendimento.class, atendimentoId);
            if (atendimento == null) {
                throw new RuntimeException("Erro: Atendimento não encontrado.");
            }
            atendimento.setStatus("Finalizado");
            em.merge(atendimento);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Atendimento> findTodosAtendimentos() {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Atendimento> query = em.createQuery(
                    "SELECT a FROM Atendimento a JOIN FETCH a.dispositivo d ORDER BY a.id DESC",
                    Atendimento.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Atendimento> findAtendimentosEmAndamento() {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Atendimento> query = em.createQuery(
                    "SELECT a FROM Atendimento a WHERE a.status = 'Em andamento'",
                    Atendimento.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void atualizarFeedbackAtendimento(Long atendimentoId, String feedback) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            Atendimento atendimento = em.find(Atendimento.class, atendimentoId);
            if (atendimento != null) {
                atendimento.setFeedbackNota(feedback);
                em.merge(atendimento);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

}

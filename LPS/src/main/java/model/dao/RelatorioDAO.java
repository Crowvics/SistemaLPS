package model.dao;

import factory.DatabaseJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.entities.Relatorio;
import java.util.List;

public class RelatorioDAO {

    private static RelatorioDAO instancia;

    public RelatorioDAO() {
    }

    public static RelatorioDAO getInstance() {
        if (instancia == null) {
            instancia = new RelatorioDAO();
        }
        return instancia;
    }

    public void save(Relatorio relatorio) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(relatorio);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Relatorio relatorio) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(relatorio);
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
            Relatorio relatorio = em.find(Relatorio.class, id);
            if (relatorio != null) {
                em.remove(relatorio);
            } else {
                throw new RuntimeException("Erro: Relatório inexistente.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Relatorio find(Long id) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Relatorio> query = em.createQuery(
                    "SELECT r FROM Relatorio r LEFT JOIN FETCH r.pecasUtilizadas WHERE r.id = :id",
                    Relatorio.class
            );
            query.setParameter("id", id);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    public List<Relatorio> findAll() {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Relatorio> query = em.createQuery(
                    "SELECT r FROM Relatorio r",
                    Relatorio.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Relatorio findByAtendimentoId(Long atendimentoId) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Relatorio> query = em.createQuery(
                    "SELECT r FROM Relatorio r LEFT JOIN FETCH r.pecasUtilizadas WHERE r.atendimento.id = :atendimentoId",
                    Relatorio.class
            );
            query.setParameter("atendimentoId", atendimentoId);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    public List<Relatorio> listarRelatorios() {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT r FROM Relatorio r LEFT JOIN FETCH r.pecasUtilizadas",
                    Relatorio.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

}

package model.dao;

import factory.DatabaseJPA;
import model.entities.Peca;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class PecaDAO {

    private static PecaDAO instancia;

    private PecaDAO() {
    }

    public static PecaDAO getInstance() {
        if (instancia == null) {
            instancia = new PecaDAO();
        }
        return instancia;
    }

    public void save(Peca peca) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            if (findByCodigoPeca(peca.getCodigoPeca()) != null) {
                throw new RuntimeException("Erro: Peça com código '" + peca.getCodigoPeca() + "' já cadastrada.");
            }

            em.getTransaction().begin();
            em.persist(peca);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Peca peca) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(peca);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Peca find(Long id) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            return em.find(Peca.class, id);
        } finally {
            em.close();
        }
    }

    public Peca findByCodigoPeca(String codigoPeca) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Peca> query = em.createQuery(
                    "SELECT p FROM Peca p WHERE p.codigoPeca = :codigoPeca", Peca.class);
            query.setParameter("codigoPeca", codigoPeca);

            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    public List<Peca> findAll() {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            String jpql = "SELECT p FROM Peca p";
            TypedQuery<Peca> query = em.createQuery(jpql, Peca.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            Peca peca = em.find(Peca.class, id);
            if (peca != null) {
                em.remove(peca);
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
                throw new IllegalArgumentException("Peça inexistente.");
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Peca findByName(String nome) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            String jpql = "SELECT p FROM Peca p WHERE p.nome = :nome";
            TypedQuery<Peca> query = em.createQuery(jpql, Peca.class);
            query.setParameter("nome", nome);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    public boolean existePecaComMesmoCodigoOuNome(String codigo, String nome, Long idAtual) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(p) FROM Peca p WHERE (p.codigoPeca = :codigo OR p.nome = :nome) AND p.id <> :idAtual",
                    Long.class
            );
            query.setParameter("codigo", codigo);
            query.setParameter("nome", nome);
            query.setParameter("idAtual", idAtual);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    public List<Peca> filterByCategory(String categoria) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            String jpql = "SELECT p FROM Peca p WHERE p.categoria = :categoria";
            TypedQuery<Peca> query = em.createQuery(jpql, Peca.class);
            query.setParameter("categoria", categoria);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

}

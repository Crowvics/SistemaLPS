package model.dao;

import factory.DatabaseJPA;
import model.entities.Dispositivo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class DispositivoDAO {

    private static DispositivoDAO instancia;

    private DispositivoDAO() {
    }

    public static DispositivoDAO getInstance() {
        if (instancia == null) {
            instancia = new DispositivoDAO();
        }
        return instancia;
    }

    public void save(Dispositivo dispositivo) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(dispositivo);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Dispositivo dispositivo) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();

            Dispositivo existente = findByNomeEmailDispositivo(dispositivo.getClienteEmail(), dispositivo.getNomeDispositivo());
            if (existente != null && !existente.getId().equals(dispositivo.getId())) {
                throw new RuntimeException("Erro: Já existe um dispositivo com esse nome para este cliente.");
            }

            em.merge(dispositivo);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Dispositivo find(Long id) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            return em.find(Dispositivo.class, id);
        } finally {
            em.close();
        }
    }

    public List<Dispositivo> findByClienteEmail(String clienteEmail) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Dispositivo> query = em.createQuery(
                    "SELECT d FROM Dispositivo d WHERE d.clienteEmail = :clienteEmail", Dispositivo.class);
            query.setParameter("clienteEmail", clienteEmail);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Dispositivo findByNomeEmailDispositivo(String clienteEmail, String nomeDispositivo) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Dispositivo> query = em.createQuery(
                    "SELECT d FROM Dispositivo d WHERE d.clienteEmail = :clienteEmail AND d.nomeDispositivo = :nomeDispositivo",
                    Dispositivo.class
            );
            query.setParameter("clienteEmail", clienteEmail);
            query.setParameter("nomeDispositivo", nomeDispositivo);

            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    public Dispositivo findByNomeDispositivo(String nomeDispositivo) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Dispositivo> query = em.createQuery(
                    "SELECT d FROM Dispositivo d WHERE d.nomeDispositivo = :nomeDispositivo",
                    Dispositivo.class
            );
            query.setParameter("nomeDispositivo", nomeDispositivo);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    public List<Dispositivo> findAll() {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            return em.createQuery("SELECT d FROM Dispositivo d", Dispositivo.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            Dispositivo dispositivo = em.find(Dispositivo.class, id);
            if (dispositivo != null) {
                em.remove(dispositivo);
            } else {
                throw new RuntimeException("Erro: Dispositivo inexistente.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Long findIdByNomeEmail(String clienteEmail, String nomeDispositivo) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT d.id FROM Dispositivo d WHERE d.clienteEmail = :clienteEmail AND d.nomeDispositivo = :nomeDispositivo",
                    Long.class
            );
            query.setParameter("clienteEmail", clienteEmail);
            query.setParameter("nomeDispositivo", nomeDispositivo);

            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

}

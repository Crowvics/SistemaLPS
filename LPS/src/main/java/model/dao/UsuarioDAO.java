package model.dao;

import factory.DatabaseJPA;
import model.entities.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class UsuarioDAO {

    private static UsuarioDAO instancia;

    private UsuarioDAO() {
    }

    public static UsuarioDAO getInstance() {
        if (instancia == null) {
            instancia = new UsuarioDAO();
        }
        return instancia;
    }

    public void save(Usuario usuario) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            if (existeEmailOuCPF(usuario.getEmail(), usuario.getCpf())) {
                throw new RuntimeException("Erro: Email ou CPF já cadastrados.");
            }

            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
            System.out.println("✅ Usuário salvo com sucesso: " + usuario.getNome());
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("❌ Erro ao salvar usuário: " + e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Usuario usuario) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            if (existeEmailOuCPFParaOutroUsuario(usuario.getEmail(), usuario.getCpf(), usuario.getId())) {
                throw new RuntimeException("Erro: Email ou CPF já cadastrados para outro usuário.");
            }

            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Usuario find(Long id) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public Usuario findByEmail(String email) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
            query.setParameter("email", email);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    public List<Usuario> findAll() {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
            System.out.println("✅ Usuários encontrados no banco: " + usuarios.size());
            return usuarios;
        } finally {
            em.close();
        }
    }

    public Usuario findByCPF(String cpf) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.cpf = :cpf", Usuario.class);
            query.setParameter("cpf", cpf);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    public List<Usuario> findByTipo(String tipo) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.tipoUsuario = :tipo", Usuario.class);
            query.setParameter("tipo", tipo);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class, id);
            if (usuario != null) {
                em.remove(usuario);
            } else {
                throw new RuntimeException("Erro: Usuário inexistente.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private boolean existeEmailOuCPF(String email, String cpf) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(u) FROM Usuario u WHERE u.email = :email OR u.cpf = :cpf", Long.class);
            query.setParameter("email", email);
            query.setParameter("cpf", cpf);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    private boolean existeEmailOuCPFParaOutroUsuario(String email, String cpf, Long id) {
        EntityManager em = DatabaseJPA.getInstance().getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(u) FROM Usuario u WHERE (u.email = :email OR u.cpf = :cpf) AND u.id != :id", Long.class);
            query.setParameter("email", email);
            query.setParameter("cpf", cpf);
            query.setParameter("id", id);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}

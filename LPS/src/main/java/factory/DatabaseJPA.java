package factory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseJPA {

    private static DatabaseJPA instance;
    private final EntityManagerFactory entityManagerFactory;

    private DatabaseJPA() {
        entityManagerFactory = Persistence.createEntityManagerFactory("projetolps");
    }

    public static DatabaseJPA getInstance() {
        if (instance == null) {
            instance = new DatabaseJPA();
        }
        return instance;
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void close() {
        entityManagerFactory.close();
    }
}

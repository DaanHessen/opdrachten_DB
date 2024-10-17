package nl.hu.DP.application;

import nl.hu.DP.domain.Reiziger;
import nl.hu.DP.repository.ReizigerDAO;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class ReizigerDAOHibernate implements ReizigerDAO {
    private EntityManager entityManager;

    public ReizigerDAOHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public long getNextReizigerId() {
        Long maxId = (Long) entityManager.createQuery("SELECT MAX(r.id) FROM Reiziger r").getSingleResult();
        return (maxId != null ? maxId + 1 : 1);
    }

    @Override
    public boolean save(Reiziger reiziger) {
        try {
            entityManager.persist(reiziger);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        try {
            entityManager.merge(reiziger);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        try {
            Reiziger managedReiziger = entityManager.find(Reiziger.class, reiziger.getId());
            if (managedReiziger != null) {
                entityManager.remove(managedReiziger);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Reiziger findById(long id) {
        try {
            return entityManager.find(Reiziger.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGeboortedatum(Date geboortedatum) {
        try {
            TypedQuery<Reiziger> query = entityManager.createQuery(
                    "SELECT r FROM Reiziger r WHERE r.geboortedatum = :datum", Reiziger.class);
            query.setParameter("datum", geboortedatum);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Reiziger> findAll() {
        try {
            TypedQuery<Reiziger> query = entityManager.createQuery(
                    "SELECT r FROM Reiziger r", Reiziger.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
package nl.hu.DP.application;

import nl.hu.DP.domain.Adres;
import nl.hu.DP.repository.AdresDAO;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class AdresDAOHibernate implements AdresDAO {
    private EntityManager entityManager;

    public AdresDAOHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public long getNextAdresId() {
        Long maxId = (Long) entityManager.createQuery("SELECT MAX(a.adresId) FROM Adres a").getSingleResult();
        return (maxId != null ? maxId + 1 : 1);
    }

    @Override
    public boolean save(Adres adres) {
        try {
            entityManager.persist(adres);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        try {
            entityManager.merge(adres);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) {
        try {
            Adres managedAdres = entityManager.find(Adres.class, adres.getAdresId());
            if (managedAdres != null) {
                entityManager.remove(managedAdres);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Adres findById(long adresId) {
        try {
            return entityManager.find(Adres.class, adresId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Adres> findAll() {
        try {
            TypedQuery<Adres> query = entityManager.createQuery(
                    "SELECT a FROM Adres a", Adres.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
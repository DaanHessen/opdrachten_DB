package nl.hu.DP.application;

import nl.hu.DP.domain.OVChipkaart;
import nl.hu.DP.domain.Reiziger;
import nl.hu.DP.repository.OVChipkaartDAO;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class OVChipkaartDAOHibernate implements OVChipkaartDAO {
    private EntityManager entityManager;

    public OVChipkaartDAOHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public long getNextOVChipkaartId() {
        Long maxId = (Long) entityManager.createQuery("SELECT MAX(o.kaartNummer) FROM OVChipkaart o").getSingleResult();
        return (maxId != null ? maxId + 1 : 1);
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        try {
            entityManager.persist(ovChipkaart);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        try {
            entityManager.merge(ovChipkaart);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        try {
            OVChipkaart managedOV = entityManager.find(OVChipkaart.class, ovChipkaart.getKaartNummer());
            if (managedOV != null) {
                entityManager.remove(managedOV);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public OVChipkaart findById(long kaartNummer) {
        try {
            return entityManager.find(OVChipkaart.class, kaartNummer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        try {
            TypedQuery<OVChipkaart> query = entityManager.createQuery(
                    "SELECT o FROM OVChipkaart o WHERE o.reiziger = :reiziger", OVChipkaart.class);
            query.setParameter("reiziger", reiziger);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findAll() {
        try {
            TypedQuery<OVChipkaart> query = entityManager.createQuery(
                    "SELECT o FROM OVChipkaart o", OVChipkaart.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
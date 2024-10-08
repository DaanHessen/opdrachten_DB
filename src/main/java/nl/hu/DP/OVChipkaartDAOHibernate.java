package nl.hu.DP;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.*;
import java.util.List;

public class OVChipkaartDAOHibernate implements OVChipkaartDAO {
    private SessionFactory sessionFactory;

    public OVChipkaartDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Long getNextOVChipkaartId(Session session) {
        Number maxId = (Number) session.createQuery("SELECT MAX(kaart_nummer) FROM OVChipkaart").uniqueResult();
        return (maxId == null) ? 1L : maxId.longValue() + 1L;
    }

    public boolean save(OVChipkaart ovChipkaart) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.doWork(connection -> connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE));

            if (ovChipkaart.getKaartnummer() == null) {
                ovChipkaart.setKaartnummer(getNextOVChipkaartId(session));
            }

            session.save(ovChipkaart);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(OVChipkaart ovChipkaart) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            if (ovChipkaart.getKaartnummer() == null) {
                ovChipkaart.setKaartnummer(getNextOVChipkaartId(session));
            }

            session.update(ovChipkaart);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(OVChipkaart ovChipkaart) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(ovChipkaart);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public OVChipkaart findById(long ovChipkaart_id) {
        try (Session session = sessionFactory.openSession()){
            return session.get(OVChipkaart.class, ovChipkaart_id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<OVChipkaart> findByGbdatum(String gbdatum) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM OVChipkaart o WHERE o.geldig_tot = :geldig_tot", OVChipkaart.class)
                    .setParameter("geldig_tot", gbdatum)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM OVChipkaart o WHERE o.reiziger.reiziger_id = :reiziger_id", OVChipkaart.class)
                    .setParameter("reiziger_id", reiziger.getId())
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<OVChipkaart> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM OVChipkaart", OVChipkaart.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

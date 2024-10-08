package nl.hu.DP;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Date;

public class ReizigerDAOHibernate implements ReizigerDAO {
    private SessionFactory sessionFactory;

    public ReizigerDAOHibernate(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }

    private Long getNextReizigerId(Session session) {
        Number maxId = (Number) session.createQuery("SELECT MAX(r.id) FROM Reiziger r").uniqueResult();
        return (maxId == null) ? 1L : maxId.longValue() + 1L;
    }


    @Override
    public boolean save(Reiziger reiziger) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            if (reiziger.getId() == null) {
                reiziger.setId(getNextReizigerId(session));
            }

            session.save(reiziger);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(reiziger);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            if (reiziger.getAdres() != null) {
                Adres adres = reiziger.getAdres();
                session.delete(adres);
            }

            session.delete(reiziger);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) { transaction.rollback(); }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Reiziger findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Reiziger.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGeboortedatum(Date geboortedatum) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Reiziger WHERE geboortedatum = :geboortedatum", Reiziger.class)
                    .setParameter("geboortedatum", geboortedatum)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Reiziger> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Reiziger", Reiziger.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

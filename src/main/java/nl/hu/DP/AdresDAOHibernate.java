package nl.hu.DP;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class AdresDAOHibernate implements AdresDAO {
    private SessionFactory sessionFactory;

    public AdresDAOHibernate(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }

    // vorige opdracht(en) heb ik heel moeilijk gedaan met de ID's voor adres en reiziger, dit keer gebruik ik de volgende twee methoden om de ID's te bepalen
    private Long getNextAdresId(Session session) {
        Long maxId = (long) session.createQuery("SELECT MAX(a.id) FROM Adres a").uniqueResult();
        return (maxId == null) ? 1l : maxId + 1l;
    }

    private Long getNextReizigerId(Session session) {
        Number maxId = (Number) session.createQuery("SELECT MAX(r.id) FROM Reiziger r").uniqueResult();
        return (maxId == null) ? 1L : maxId.longValue() + 1L;
    }

    @Override
    public boolean save(Adres adres) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            if (adres.getReiziger() != null) {
                Reiziger reiziger = adres.getReiziger();
                if (reiziger.getId() == null) {
                    reiziger.setId(getNextReizigerId(session));
                    session.save(reiziger);
                } else {
                    session.update(reiziger);
                }
            }

            if (adres.getId() == null) {
                adres.setId(getNextAdresId(session));
            }

            session.save(adres);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(adres);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(adres);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Adres findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Adres.class, id);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Adres.class, reiziger.getId());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

        @Override
        public List<Adres> findAll () {
            List<Adres> adressen = new ArrayList<>();
            try (Session session = sessionFactory.openSession()) {
                adressen = session.createQuery("from Adres", Adres.class).list();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return adressen;
        }
    }

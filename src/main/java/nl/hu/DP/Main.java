// Main.java
package nl.hu.DP;

import nl.hu.DP.application.AdresDAOHibernate;
import nl.hu.DP.application.OVChipkaartDAOHibernate;
import nl.hu.DP.application.ProductDAOHibernate;
import nl.hu.DP.application.ReizigerDAOHibernate;
import nl.hu.DP.domain.Adres;
import nl.hu.DP.domain.OVChipkaart;
import nl.hu.DP.domain.Reiziger;
import nl.hu.DP.domain.Product;
import nl.hu.DP.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        ReizigerDAOHibernate reizigerDAO = new ReizigerDAOHibernate(session);
        AdresDAOHibernate adresDAO = new AdresDAOHibernate(session);
        OVChipkaartDAOHibernate ovChipkaartDAO = new OVChipkaartDAOHibernate(session);
        ProductDAOHibernate productDAO = new ProductDAOHibernate(session);

        try {
            Reiziger reiziger = new Reiziger();
            reiziger.setId(reizigerDAO.getNextReizigerId());
            reiziger.setVoorletters("A");
            reiziger.setAchternaam("Smith");
            reiziger.setGeboortedatum(java.sql.Date.valueOf("1985-05-15"));

            Adres adres = new Adres();
            adres.setAdresId(adresDAO.getNextAdresId());
            adres.setPostcode("5678 CD");
            adres.setHuisnummer("78");
            adres.setStraat("Second Street");
            adres.setWoonplaats("Utrecht");
            reiziger.setAdres(adres);

            boolean reizigerSaved = reizigerDAO.save(reiziger);
            if (!reizigerSaved) {
                throw new Exception("Failed to save Reiziger.");
            }

            OVChipkaart ovChipkaart = new OVChipkaart();
            ovChipkaart.setKaartNummer(ovChipkaartDAO.getNextOVChipkaartId());
            ovChipkaart.setGeldigTot(java.sql.Date.valueOf("2026-06-30"));
            ovChipkaart.setKlasse(2);
            ovChipkaart.setSaldo(75.0);
            ovChipkaart.setReiziger(reiziger);
            reiziger.addOVChipkaart(ovChipkaart);

            boolean ovChipkaartSaved = ovChipkaartDAO.save(ovChipkaart);
            if (!ovChipkaartSaved) {
                throw new Exception("Failed to save OVChipkaart.");
            }

            Product product1 = new Product();
            product1.setProductNummer(productDAO.getNextProductNummer());
            product1.setNaam("Product X");
            product1.setBeschrijving("Description for Product X");
            product1.setPrijs(150L);
            product1.addOVChipkaart(ovChipkaart);

            boolean product1Saved = productDAO.save(product1);
            if (!product1Saved) {
                throw new Exception("Failed to save Product X.");
            }

            Product product2 = new Product();
            product2.setProductNummer(productDAO.getNextProductNummer());
            product2.setNaam("Product Y");
            product2.setBeschrijving("Description for Product Y");
            product2.setPrijs(250L);
            product2.addOVChipkaart(ovChipkaart);

            boolean product2Saved = productDAO.save(product2);
            if (!product2Saved) {
                throw new Exception("Failed to save Product Y.");
            }

            session.getTransaction().commit();
            System.out.println("Entities saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }
}

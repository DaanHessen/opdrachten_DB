package nl.hu.DP;

import nl.hu.DP.application.AdresDAOPsql;
import nl.hu.DP.application.OVChipkaartDAOPsql;
import nl.hu.DP.application.ProductDAOPsql;
import nl.hu.DP.application.ReizigerDAOPsql;
import nl.hu.DP.domain.Adres;
import nl.hu.DP.domain.Reiziger;
import nl.hu.DP.domain.OVChipkaart;
import nl.hu.DP.domain.Product;
import nl.hu.DP.repository.AdresDAO;
import nl.hu.DP.repository.ReizigerDAO;
import nl.hu.DP.repository.OVChipkaartDAO;
import nl.hu.DP.repository.ProductDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "Kq)44Ne@";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            ReizigerDAO reizigerDAO = new ReizigerDAOPsql(connection);
            AdresDAO adresDAO = new AdresDAOPsql(connection);
            OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOPsql(connection);
            ProductDAO productDAO = new ProductDAOPsql(connection, ovChipkaartDAO);

            Reiziger reiziger = new Reiziger();
            reiziger.setVoorletters("A");
            reiziger.setTussenvoegsel("van");
            reiziger.setAchternaam("Smith");
            reiziger.setGeboortedatum(new java.sql.Date(System.currentTimeMillis()));

            Adres adres = new Adres();
            adres.setPostcode("5678 CD");
            adres.setHuisnummer("78");
            adres.setStraat("Second Street");
            adres.setWoonplaats("Utrecht");
            adres.setReiziger(reiziger);
            reiziger.setAdres(adres);

            boolean reizigerSaved = reizigerDAO.save(reiziger);
            if (reizigerSaved) {
                System.out.println("Reiziger saved successfully with ID: " + reiziger.getId());
            } else {
                System.out.println("Failed to save Reiziger.");
                return;
            }

            OVChipkaart ovChipkaart = new OVChipkaart();
            ovChipkaart.setGeldigTot(new java.sql.Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000)); // Valid for 1 year
            ovChipkaart.setKlasse(2);
            ovChipkaart.setSaldo(75.0);
            ovChipkaart.setReizigerId(reiziger.getId());
            Long nextKaartNummer = ((OVChipkaartDAOPsql) ovChipkaartDAO).getNextKaartNummer();
            ovChipkaart.setKaartnummer(nextKaartNummer);
            ovChipkaart.setReiziger(reiziger);
            reiziger.addOVChipkaart(ovChipkaart);

            boolean ovChipkaartSaved = ovChipkaartDAO.save(ovChipkaart);
            if (ovChipkaartSaved) {
                System.out.println("OVChipkaart saved successfully with Kaartnummer: " + ovChipkaart.getKaartnummer());
            } else {
                System.out.println("Failed to save OVChipkaart.");
                return;
            }

            Product product1 = new Product();
            Long nextProductNummer1 = ((ProductDAOPsql) productDAO).getNextProductNummer();
            product1.setProductNummer(nextProductNummer1);
            product1.setNaam("Product X");
            product1.setBeschrijving("Description for Product X");
            product1.setPrijs(150L);
            product1.addOVChipkaart(ovChipkaart);

            Product product2 = new Product();
            Long nextProductNummer2 = ((ProductDAOPsql) productDAO).getNextProductNummer();
            product2.setProductNummer(nextProductNummer2);
            product2.setNaam("Product Y");
            product2.setBeschrijving("Description for Product Y");
            product2.setPrijs(250L);
            product2.addOVChipkaart(ovChipkaart);

            boolean product1Saved = productDAO.save(product1);
            if (product1Saved) {
                System.out.println("Product X saved successfully with ProductNummer: " + product1.getProductNummer());
            } else {
                System.out.println("Failed to save Product X.");
            }

            boolean product2Saved = productDAO.save(product2);
            if (product2Saved) {
                System.out.println("Product Y saved successfully with ProductNummer: " + product2.getProductNummer());
            } else {
                System.out.println("Failed to save Product Y.");
            }

            List<Reiziger> reizigers = reizigerDAO.findAll();
            System.out.println("\nAll Reizigers:");
            for (Reiziger r : reizigers) {
                System.out.println("ID: " + r.getId() + ", Name: " + r.getVoorletters() + " " + r.getTussenvoegsel() + " " + r.getAchternaam());
            }

            List<Adres> adressen = adresDAO.findAll();
            System.out.println("\nAll Adressen:");
            for (Adres a : adressen) {
                System.out.println("Adres ID: " + a.getId() + ", Postcode: " + a.getPostcode() + ", Straat: " + a.getStraat() + ", Woonplaats: " + a.getWoonplaats());
            }

            List<OVChipkaart> ovChipkaarten = ovChipkaartDAO.findAll();
            System.out.println("\nAll OVChipkaarten:");
            for (OVChipkaart o : ovChipkaarten) {
                System.out.println("Kaartnummer: " + o.getKaartnummer() + ", Saldo: " + o.getSaldo() + ", Klasse: " + o.getKlasse());
            }

            List<Product> products = productDAO.findAll();
            System.out.println("\nAll Products:");
            for (Product p : products) {
                System.out.println("Product Nummer: " + p.getProductNummer() + ", Naam: " + p.getNaam() + ", Prijs: " + p.getPrijs());
            }

            List<Product> associatedProducts = productDAO.findByOVChipkaart(ovChipkaart);
            System.out.println("\nProducts associated with OVChipkaart " + ovChipkaart.getKaartnummer() + ":");
            for (Product p : associatedProducts) {
                System.out.println("Product Nummer: " + p.getProductNummer() + ", Naam: " + p.getNaam());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

//package nl.hu.DP;
//
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;
//import org.junit.jupiter.api.*;
//
//import java.util.List;
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class AdresDAOTest {
//    private SessionFactory sessionFactory;
//    private AdresDAO adresDAO;
//    private ReizigerDAO reizigerDAO;
//
//    @BeforeAll
//    public void setup() {
//        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
//        adresDAO = new AdresDAOHibernate(sessionFactory);
//        reizigerDAO = new ReizigerDAOHibernate(sessionFactory);
//    }
//
//    @AfterAll
//    public void tearDown() {
//        if (sessionFactory != null) {
//            sessionFactory.close();
//        }
//    }
//
//    @Test
//    public void testSaveReiziger() {
//        Reiziger reiziger = new Reiziger();
//        reiziger.setVoorletters("John");
//        reiziger.setTussenvoegsel("");
//        reiziger.setAchternaam("Doe");
//        reiziger.setGeboortedatum(new Date());
//
//        boolean saved = reizigerDAO.save(reiziger);
//        assertTrue(saved);
//
//        Reiziger foundReiziger = reizigerDAO.findById(reiziger.getId());
//        assertNotNull(foundReiziger);
//        assertEquals("John", foundReiziger.getVoorletters());
//    }
//
//
//    @Test
//    public void testUpdateReiziger() {
//        Reiziger reiziger = new Reiziger("John", "", "Doe", new Date());
//        boolean saved = reizigerDAO.save(reiziger);
//        assertTrue(saved);
//
//        reiziger.setAchternaam("Smith");
//        boolean updated = reizigerDAO.update(reiziger);
//        assertTrue(updated);
//
//        Reiziger updatedReiziger = reizigerDAO.findById(reiziger.getId());
//        assertNotNull(updatedReiziger);
//        assertEquals("Smith", updatedReiziger.getAchternaam());
//    }
//
//    @Test
//    public void testDeleteReiziger() {
//        Reiziger reiziger = new Reiziger("John", "", "Doe", new Date());
//        boolean saved = reizigerDAO.save(reiziger);
//        assertTrue(saved);
//
//        boolean deleted = reizigerDAO.delete(reiziger);
//        assertTrue(deleted);
//
//        Reiziger deletedReiziger = reizigerDAO.findById(reiziger.getId());
//        assertNull(deletedReiziger);
//    }
//
//    @Test
//    public void testSaveAdres() {
//        Reiziger reiziger = new Reiziger("Jane", "", "Doe", new Date());
//        reizigerDAO.save(reiziger);
//
//        Adres adres = new Adres("1234AB", "10", "Main Street", "Amsterdam", reiziger);
//        boolean saved = adresDAO.save(adres);
//        assertTrue(saved);
//
//        long adresId = adres.getId();
//
//        Adres foundAdres = adresDAO.findById(adresId);
//        assertNotNull(foundAdres);
//        assertEquals("1234AB", foundAdres.getPostcode());
//    }
//
//    @Test
//    public void testFindAllReizigers() {
//        List<Reiziger> reizigers = reizigerDAO.findAll();
//        assertNotNull(reizigers);
//        assertTrue(!reizigers.isEmpty());
//    }
//
//    @Test
//    public void testFindByGeboortedatum() {
//        Date geboortedatum = new Date();
//        List<Reiziger> reizigers = reizigerDAO.findByGeboortedatum(geboortedatum);
//        assertNotNull(reizigers);
//    }
//}

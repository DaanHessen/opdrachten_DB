//package nl.hu.DP;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.junit.jupiter.api.*;
//
//import org.hibernate.cfg.Configuration;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//
//public class OVChipkaartDAOHibernateTest {
//    private static SessionFactory sessionFactory;
//    private Session session;
//    private OVChipkaartDAO ovChipkaartDAO;
//
//    @BeforeAll
//    public static void setup() {
//        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
//    }
//
//    @BeforeEach
//    public void startSession() {
//        session = sessionFactory.openSession();
//        ovChipkaartDAO = new OVChipkaartDAOHibernate(sessionFactory);
//    }
//
//    @AfterAll
//    public static void closeSession() {
//        if (sessionFactory != null) {
//            sessionFactory.close();
//        }
//    }
//
//    @AfterEach
//    public void tearDown() {
//        Transaction transaction = null;
//
//        try {
//            transaction = session.beginTransaction();
//            session.createQuery("DELETE FROM OVChipkaart").executeUpdate();
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null) transaction.rollback();
//            e.printStackTrace();
//        }
//    }
//
//
//    @Test
//    public void testSave() {
//        Reiziger reiziger = new Reiziger();
//        reiziger.setId(10L);
//
//        OVChipkaart ovChipkaart = new OVChipkaart();
//        ovChipkaart.setKaartnummer(1234567899L);
//        ovChipkaart.setGeldigTot("2024-12-31");
//        ovChipkaart.setKlasse(2L);
//        ovChipkaart.setSaldo(100L);
//        reiziger.setOVChipkaarten(List.of(ovChipkaart));
//        ovChipkaart.setReiziger(reiziger);
//
//        boolean result = ovChipkaartDAO.save(ovChipkaart);
//
//        assertTrue(result);
//    }
//
//    @Test
//    public void testUpdate() {
//        Reiziger reiziger = new Reiziger();
//        reiziger.setId(11L);
//
//        OVChipkaart ovChipkaart = new OVChipkaart();
//        ovChipkaart.setKaartnummer(1234567891L);
//        ovChipkaart.setGeldigTot("2024-12-31");
//        ovChipkaart.setKlasse(2L);
//        ovChipkaart.setSaldo(100L);
//        reiziger.setOVChipkaarten(List.of(ovChipkaart));
//        ovChipkaart.setReiziger(reiziger);
//
//        ovChipkaartDAO.save(ovChipkaart);
//
//        ovChipkaart.setSaldo(200L);
//        boolean updated = ovChipkaartDAO.update(ovChipkaart);
//
//        assertTrue(updated);
//    }
//
//    @Test
//    public void testDelete() {
//        Reiziger reiziger = new Reiziger();
//        reiziger.setId(12L);
//
//        OVChipkaart ovChipkaart = new OVChipkaart();
//        ovChipkaart.setKaartnummer(1234567892L);
//        ovChipkaart.setGeldigTot("2024-12-31");
//        ovChipkaart.setKlasse(2L);
//        ovChipkaart.setSaldo(100L);
//        reiziger.setOVChipkaarten(List.of(ovChipkaart));
//        ovChipkaart.setReiziger(reiziger);
//
//        boolean deleted = ovChipkaartDAO.delete(ovChipkaart);
//
//        assertTrue(deleted);
//    }
//
//    @Test
//    public void testFindById() {
//        Reiziger reiziger = new Reiziger();
//        reiziger.setId(13L);
//
//        OVChipkaart ovChipkaart = new OVChipkaart();
//        ovChipkaart.setKaartnummer(1234567893L);
//        ovChipkaart.setGeldigTot("2024-12-31");
//        ovChipkaart.setKlasse(2L);
//        ovChipkaart.setSaldo(100L);
//        reiziger.setOVChipkaarten(List.of(ovChipkaart));
//        ovChipkaart.setReiziger(reiziger);
//
//        ovChipkaartDAO.save(ovChipkaart);
//
//        OVChipkaart found = ovChipkaartDAO.findById(ovChipkaart.getKaartnummer());
//
//        assertTrue(found != null);
//    }
//
//    @Test
//    public void testFindByGbdatum() {
//        Reiziger reiziger = new Reiziger();
//        reiziger.setId(14L);
//
//        OVChipkaart ovChipkaart = new OVChipkaart();
//
//        ovChipkaart.setGeldigTot("2024-12-31");
//        ovChipkaart.setKlasse(2L);
//        ovChipkaart.setSaldo(100L);
//        reiziger.setOVChipkaarten(List.of(ovChipkaart));
//        ovChipkaart.setReiziger(reiziger);
//
//        ovChipkaartDAO.save(ovChipkaart);
//
//        List<OVChipkaart> found = ovChipkaartDAO.findByGbdatum(ovChipkaart.getGeldigTot());
//        assertTrue(found != null);
//    }
//
//    @Test
//    public void testFindByReiziger() {
//        Reiziger reiziger = new Reiziger();
//        reiziger.setId(15L);
//
//        OVChipkaart ovChipkaart = new OVChipkaart();
//        ovChipkaart.setKaartnummer(1234567895L);
//        ovChipkaart.setGeldigTot("2024-12-31");
//        ovChipkaart.setKlasse(2L);
//        ovChipkaart.setSaldo(100L);
//        reiziger.setOVChipkaarten(List.of(ovChipkaart));
//        ovChipkaart.setReiziger(reiziger);
//
//        ovChipkaartDAO.save(ovChipkaart);
//
//        List<OVChipkaart> found = ovChipkaartDAO.findByReiziger(reiziger);
//        assertTrue(found != null);
//    }
//
//    @Test
//    public void testFindAll() {
//        List<OVChipkaart> found = ovChipkaartDAO.findAll();
//        assertTrue(found != null);
//    }
//}

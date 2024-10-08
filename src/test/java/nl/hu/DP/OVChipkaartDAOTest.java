//package nl.hu.DP;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//public class OVChipkaartDAOTest {
//
//    private OVChipkaartDAOPsql ovChipkaartDAO;
//    private Connection connection;
//
//    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
//    private static final String DB_USER = "postgres";
//    private static final String DB_PASSWORD = "Kq)44Ne@";
//
//    @BeforeEach
//    public void setup() throws SQLException {
//        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//        ovChipkaartDAO = new OVChipkaartDAOPsql(connection);
//    }
//
//    @AfterEach
//    public void tearDown() throws SQLException {
//        String cleanUpSql = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
//        try (PreparedStatement pstmt = connection.prepareStatement(cleanUpSql)) {
//            pstmt.setLong(1, Long.parseLong("1234567890"));
//            pstmt.executeUpdate();
//        }
//        if (connection != null) {
//            connection.close();
//        }
//    }
//
//    @Test
//    public void testSave() throws SQLException {
//        Reiziger reiziger = new Reiziger();
//        reiziger.setId(5L);
//
//        OVChipkaart ovChipkaart = new OVChipkaart();
//        ovChipkaart.setKaartnummer(1234567890L);
//        ovChipkaart.setGeldigTot("2024-12-31");
//        ovChipkaart.setKlasse(2L);
//        ovChipkaart.setSaldo(10000L);
//        ovChipkaart.setReiziger(reiziger);
//
//        boolean result = ovChipkaartDAO.save(ovChipkaart);
//        assertTrue(result);
//
//        OVChipkaart savedChipkaart = ovChipkaartDAO.findById(1234567890L);
//        assertNotNull(savedChipkaart);
//        assertEquals(1234567890L, savedChipkaart.getKaartnummer());
//        assertEquals("2024-12-31", savedChipkaart.getGeldigTot());
//        assertEquals(10000L, savedChipkaart.getSaldo());
//    }
//
//    @Test
//    public void testUpdate() throws SQLException {
//        Reiziger reiziger = new Reiziger();
//        reiziger.setId(5L);
//
//        OVChipkaart ovChipkaart = new OVChipkaart();
//        ovChipkaart.setKaartnummer(1234567890L);
//        ovChipkaart.setGeldigTot("2024-12-31");
//        ovChipkaart.setKlasse(2L);
//        ovChipkaart.setSaldo(10000L);
//        ovChipkaart.setReiziger(reiziger);
//
//        boolean saved = ovChipkaartDAO.save(ovChipkaart);
//        assertTrue(saved);
//
//        ovChipkaart.setSaldo(20000L);
//        boolean updated = ovChipkaartDAO.update(ovChipkaart);
//        assertTrue(updated);
//
//        OVChipkaart updatedChipkaart = ovChipkaartDAO.findById(1234567890L);
//        assertNotNull(updatedChipkaart);
//        assertEquals(20000L, updatedChipkaart.getSaldo());
//    }
//
//    @Test
//    public void testDelete() throws SQLException {
//        Reiziger reiziger = new Reiziger();
//        reiziger.setId(5L);
//
//        OVChipkaart ovChipkaart = new OVChipkaart();
//        ovChipkaart.setKaartnummer(1234567890L);
//        ovChipkaart.setGeldigTot("2024-12-31");
//        ovChipkaart.setKlasse(2L);
//        ovChipkaart.setSaldo(10000L);
//        ovChipkaart.setReiziger(reiziger);
//
//        boolean saved = ovChipkaartDAO.save(ovChipkaart);
//        assertTrue(saved);
//
//        boolean deleted = ovChipkaartDAO.delete(ovChipkaart);
//        assertTrue(deleted);
//
//        OVChipkaart deletedChipkaart = ovChipkaartDAO.findById(1234567890L);
//        assertNull(deletedChipkaart);
//    }
//
//    @Test
//    public void testFindById() throws SQLException {
//        Reiziger reiziger = new Reiziger();
//        reiziger.setId(5L);
//
//        OVChipkaart ovChipkaart = new OVChipkaart();
//        ovChipkaart.setKaartnummer(1234567890L);
//        ovChipkaart.setGeldigTot("2024-12-31");
//        ovChipkaart.setKlasse(2L);
//        ovChipkaart.setSaldo(10000L);
//        ovChipkaart.setReiziger(reiziger);
//
//        boolean saved = ovChipkaartDAO.save(ovChipkaart);
//        assertTrue(saved);
//
//        OVChipkaart foundChipkaart = ovChipkaartDAO.findById(1234567890L);
//        assertNotNull(foundChipkaart);
//        assertEquals(1234567890L, foundChipkaart.getKaartnummer());
//        assertEquals("2024-12-31", foundChipkaart.getGeldigTot());
//        assertEquals(10000L, foundChipkaart.getSaldo());
//    }
//
//    @Test
//    public void testFindByGbdatum() throws SQLException {
//        Reiziger reiziger = new Reiziger();
//        reiziger.setId(5L);
//
//        OVChipkaart ovChipkaart = new OVChipkaart();
//        ovChipkaart.setKaartnummer(1234567890L);
//        ovChipkaart.setGeldigTot("2024-12-31");
//        ovChipkaart.setKlasse(2L);
//        ovChipkaart.setSaldo(10000L);
//        ovChipkaart.setReiziger(reiziger);
//
//        boolean saved = ovChipkaartDAO.save(ovChipkaart);
//        assertTrue(saved);
//
//        OVChipkaart foundChipkaart = ovChipkaartDAO.findByGbdatum("2024-12-31").get(0);
//        assertNotNull(foundChipkaart);
//        assertEquals(1234567890L, foundChipkaart.getKaartnummer());
//        assertEquals("2024-12-31", foundChipkaart.getGeldigTot());
//        assertEquals(10000L, foundChipkaart.getSaldo());
//    }
//
//    @Test
//    public void testFindByReiziger() throws SQLException {
//        Reiziger reiziger = new Reiziger();
//        reiziger.setId(5L);
//
//        OVChipkaart ovChipkaart = new OVChipkaart();
//        ovChipkaart.setKaartnummer(1234567890L);
//        ovChipkaart.setGeldigTot("2024-12-31");
//        ovChipkaart.setKlasse(2L);
//        ovChipkaart.setSaldo(10000L);
//        ovChipkaart.setReiziger(reiziger);
//
//        boolean saved = ovChipkaartDAO.save(ovChipkaart);
//        assertTrue(saved);
//
//        OVChipkaart foundChipkaart = ovChipkaartDAO.findByReiziger(reiziger).get(0);
//        assertNotNull(foundChipkaart);
//        assertEquals(90537L, foundChipkaart.getKaartnummer());
//        assertEquals("2019-12-31", foundChipkaart.getGeldigTot());
//        assertEquals(20L, foundChipkaart.getSaldo());
//    }
//
//    @Test
//    public void testFindAll() throws SQLException {
//        Reiziger reiziger = new Reiziger();
//        reiziger.setId(5L);
//
//        OVChipkaart ovChipkaart = new OVChipkaart();
//        ovChipkaart.setKaartnummer(1234567890L);
//        ovChipkaart.setGeldigTot("2024-12-31");
//        ovChipkaart.setKlasse(2L);
//        ovChipkaart.setSaldo(10000L);
//        ovChipkaart.setReiziger(reiziger);
//
//        boolean saved = ovChipkaartDAO.save(ovChipkaart);
//        assertTrue(saved);
//
//        int initialSize = ovChipkaartDAO.findAll().size();
//        assertTrue(initialSize > 0);
//    }
//}

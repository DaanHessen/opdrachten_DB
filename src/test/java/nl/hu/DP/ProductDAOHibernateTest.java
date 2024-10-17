//package nl.hu.DP;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.junit.jupiter.api.*;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class ProductDAOHibernateTest {
//
//    private SessionFactory sessionFactory;
//    private Session session;
//    private ProductDAOHibernate productDAO;
//    private OVChipkaartDAOHibernate ovChipkaartDAO;
//    private ReizigerDAOHibernate reizigerDAO;
//
//    @BeforeAll
//    public void setUp() {
//        // Initialize SessionFactory
//        sessionFactory = HibernateUtil.getSessionFactory();
//    }
//
//    @BeforeEach
//    public void openSession() {
//        // Open a new Session before each test
//        session = sessionFactory.openSession();
//        // Begin Transaction
//        session.beginTransaction();
//        // Initialize DAOs with the current session
//        ovChipkaartDAO = new OVChipkaartDAOHibernate(session);
//        productDAO = new ProductDAOHibernate(session);
//        reizigerDAO = new ReizigerDAOHibernate(session);
//    }
//
//    @AfterEach
//    public void closeSession() {
//        // Rollback Transaction after each test to maintain test isolation
//        if (session.getTransaction().isActive()) {
//            session.getTransaction().rollback();
//        }
//        // Close Session
//        session.close();
//    }
//
//    @AfterAll
//    public void tearDown() {
//        // Shutdown SessionFactory after all tests
//        HibernateUtil.shutdown();
//    }
//
//
//    @Test
//    public void testFindByOVChipkaart() {
//        // Step 1: Create a new Reiziger
//        Reiziger reiziger = new Reiziger();
//        reiziger.setVoorletters("A");
//        reiziger.setAchternaam("Smith");
//        reiziger.setGeboortedatum(java.sql.Date.valueOf("1985-05-15"));
//
//        // Optionally, create and set an Adres
//        Adres adres = new Adres();
//        adres.setPostcode("5678 CD");
//        adres.setHuisnummer("78");
//        adres.setStraat("Second Street");
//        adres.setWoonplaats("Utrecht");
//        // Establish bidirectional relationship
//        adres.setReiziger(reiziger);
//        reiziger.setAdres(adres);
//
//        // Step 2: Create a new OVChipkaart and associate it with the Reiziger
//        OVChipkaart ovChipkaart = new OVChipkaart();
//        // Remove manual setting of kaartnummer if it's auto-generated
//        // ovChipkaart.setKaartnummer(987654321L); // Let the database handle it
//        ovChipkaart.setGeldigTot(java.sql.Date.valueOf("2026-06-30"));
//        ovChipkaart.setKlasse(2);
//        ovChipkaart.setSaldo(75.0);
//
//        // Establish bidirectional relationship
//        reiziger.addOVChipkaart(ovChipkaart);
//
//        // Step 3: Persist the Reiziger (which cascades to OVChipkaart and Adres)
//        boolean reizigerSaved = reizigerDAO.save(reiziger);
//        assertTrue(reizigerSaved, "Reiziger should be saved successfully");
//
//        // Step 4: Create and associate Products with the OVChipkaart
//        Product product1 = new Product();
//        product1.setNaam("Product X");
//        product1.setBeschrijving("Description for Product X");
//        product1.setPrijs(150L);
//        product1.addOVChipkaart(ovChipkaart);
//
//        Product product2 = new Product();
//        product2.setNaam("Product Y");
//        product2.setBeschrijving("Description for Product Y");
//        product2.setPrijs(250L);
//        product2.addOVChipkaart(ovChipkaart);
//
//        // Step 5: Persist the Products
//        boolean product1Saved = productDAO.save(product1);
//        boolean product2Saved = productDAO.save(product2);
//        assertTrue(product1Saved, "Product 1 should be saved successfully");
//        assertTrue(product2Saved, "Product 2 should be saved successfully");
//
//        // Step 6: Retrieve Products by OVChipkaart
//        List<Product> products = productDAO.findByOVChipkaart(ovChipkaart);
//        assertNotNull(products, "Products list should not be null");
//        assertEquals(2, products.size(), "There should be two products associated with the OVChipkaart");
//
//        // Additional Assertions to verify correctness
//        assertTrue(products.contains(product1), "Products should contain Product X");
//        assertTrue(products.contains(product2), "Products should contain Product Y");
//    }
//
//    @Test
//    public void testUpdateProduct() {
//        // Step 1: Create and persist a Product
//        Product product = new Product();
//        product.setNaam("Original Product");
//        product.setBeschrijving("Original Description");
//        product.setPrijs(200L);
//        // Assuming no associations for simplicity
//        boolean saved = productDAO.save(product);
//        assertTrue(saved, "Product should be saved successfully");
//
//        // Step 2: Update the Product's name and price
//        product.setNaam("Updated Product");
//        product.setPrijs(250L);
//
//        boolean updated = productDAO.update(product);
//        assertTrue(updated, "Product should be updated successfully");
//
//        // Step 3: Retrieve the updated Product to verify changes
//        Product retrieved = productDAO.findById(product.getProductNummer());
//        assertNotNull(retrieved, "Retrieved product should not be null");
//        assertEquals("Updated Product", retrieved.getNaam(), "Product name should be updated");
//        assertEquals(250L, retrieved.getPrijs(), "Product price should be updated");
//    }
//
//
//    @Test
//    public void testDeleteProduct() {
//        // Step 1: Create and persist a Product
//        Product product = new Product();
//        product.setNaam("Delete Product");
//        product.setBeschrijving("Description to delete");
//        product.setPrijs(150L);
//        boolean saved = productDAO.save(product);
//        assertTrue(saved, "Product should be saved successfully");
//
//        // Step 2: Delete the Product
//        boolean deleted = productDAO.delete(product);
//        assertTrue(deleted, "Product should be deleted successfully");
//
//        // Step 3: Attempt to retrieve the deleted Product
//        Product retrieved = productDAO.findById(product.getProductNummer());
//        assertNull(retrieved, "Deleted product should not be found");
//    }
//
//
//    @Test
//    public void testFindById() {
//        // Save a product to find
//        Product product = new Product();
//        product.setNaam("Find Product");
//        product.setBeschrijving("Description to find");
//        product.setPrijs(300L);
//
//        boolean saved = productDAO.save(product);
//        assertTrue(saved, "Product should be saved successfully");
//
//        // Find the product by ID
//        Product found = productDAO.findById(product.getProductNummer());
//        assertNotNull(found, "Product should be found by ID");
//        assertEquals("Find Product", found.getNaam(), "Product name should match");
//    }
//
//    @Test
//    public void testFindAll() {
//        // Ensure the database is clean
//        List<Product> initialProducts = productDAO.findAll();
//        for (Product p : initialProducts) {
//            productDAO.delete(p);
//        }
//
//        // Save multiple products
//        Product product1 = new Product();
//        product1.setNaam("Product 1");
//        product1.setBeschrijving("Description 1");
//        product1.setPrijs(100L);
//        productDAO.save(product1);
//
//        Product product2 = new Product();
//        product2.setNaam("Product 2");
//        product2.setBeschrijving("Description 2");
//        product2.setPrijs(200L);
//        productDAO.save(product2);
//
//        // Retrieve all products
//        List<Product> products = productDAO.findAll();
//        assertEquals(2, products.size(), "There should be two products in the database");
//    }
//}

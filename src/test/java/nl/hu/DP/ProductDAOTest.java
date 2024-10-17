package nl.hu.DP;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDAOTest {

    private static Connection connection;
    private static ProductDAOPsql productDAO;
    private static OVChipkaartDAOPsql ovChipkaartDAO;
    private static ReizigerDAOPsql reizigerDAO;

    @BeforeAll
    static void setUp() throws SQLException {
        // Establish a connection to the test database
        // Update the URL, username, and password as per your setup
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Kq)44Ne@");

        // Initialize DAOs
        reizigerDAO = new ReizigerDAOPsql(connection);
        ovChipkaartDAO = new OVChipkaartDAOPsql(connection);
        productDAO = new ProductDAOPsql(connection, ovChipkaartDAO);
    }

    @AfterAll
    static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @BeforeEach
    void cleanDatabase() throws SQLException {
        // Only delete specific test data, ensuring that foreign key constraints are respected
        try (var stmt = connection.createStatement()) {
            // Optionally, clean only the test data from ov_chipkaart_product and product tables
            stmt.executeUpdate("DELETE FROM ov_chipkaart_product WHERE product_nummer >= 100"); // Assumes test data uses product_nummer >= 100
            stmt.executeUpdate("DELETE FROM product WHERE product_nummer >= 100");

            // Avoid deleting anything from the reiziger or adres tables that might cause foreign key constraint violations
            // If needed, you can clean specific rows from the reiziger table in a controlled manner
        }
    }

    @Test
    void testSaveProductWithoutOVChipkaart() throws SQLException {
        // Creating a new Reiziger
        Reiziger reiziger = new Reiziger("G", "van", "Rijn", Date.valueOf("2002-09-17"));
        boolean reizigerSaved = reizigerDAO.save(reiziger);
        assertTrue(reizigerSaved, "Reiziger should be saved");

        // Creating a new Product without OVChipkaart
        Product product = new Product(101L, "TestProduct", "This is a test product", 200L);
        boolean saved = productDAO.save(product);

        assertTrue(saved, "Product should be saved");

        Product retrievedProduct = productDAO.findById(101L);
        assertNotNull(retrievedProduct, "Product should be retrievable");
        assertEquals("TestProduct", retrievedProduct.getNaam(), "Product name should match");
    }

    @Test
    void testSaveProductWithOVChipkaart() throws SQLException {
        // Creating a new Reiziger
        Reiziger reiziger = new Reiziger("B", "van", "Rijn", Date.valueOf("2002-10-22"));
        boolean reizigerSaved = reizigerDAO.save(reiziger);
        assertTrue(reizigerSaved, "Reiziger should be saved");

        // Creating a new Product and OVChipkaart
        Product product = new Product(102L, "ProductWithCard", "Test product with OVChipkaart", 300L);
        OVChipkaart ovChipkaart = new OVChipkaart(100L, Date.valueOf("2025-12-12"), 1, 100.0, reiziger);
        product.addOVChipkaart(ovChipkaart);

        boolean saved = productDAO.save(product);
        assertTrue(saved, "Product with OVChipkaart should be saved");

        // Retrieve the product by ID
        Product retrievedProduct = productDAO.findById(102L);
        assertNotNull(retrievedProduct, "Product should be retrievable");
        assertEquals("ProductWithCard", retrievedProduct.getNaam(), "Product name should match");

        // Optionally, verify the relationship in the join table
        List<Product> products = productDAO.findByOVChipkaart(ovChipkaart);
        assertTrue(products.contains(retrievedProduct), "Relationship between Product and OVChipkaart should exist");
    }

    @Test
    void testUpdateProduct() throws SQLException {
        // Create and save a Reiziger first
        Reiziger reiziger = new Reiziger("H", null, "Lubben", Date.valueOf("1998-08-11"));
        boolean reizigerSaved = reizigerDAO.save(reiziger);
        assertTrue(reizigerSaved, "Reiziger should be saved");

        // Create and save a product first
        Product product = new Product(103L, "OriginalName", "Original description", 400L);
        OVChipkaart ovChipkaart = new OVChipkaart(200L, Date.valueOf("2025-12-12"), 1, 200.0, reiziger);
        product.addOVChipkaart(ovChipkaart);
        boolean saved = productDAO.save(product);
        assertTrue(saved, "Product should be saved");

        // Now update the product
        product.setNaam("UpdatedName");
        product.setBeschrijving("Updated description");
        boolean updated = productDAO.update(product);
        assertTrue(updated, "Product should be updated");

        // Retrieve the updated product and check its name
        Product updatedProduct = productDAO.findById(103L);
        assertNotNull(updatedProduct);
        assertEquals("UpdatedName", updatedProduct.getNaam(), "Product name should be updated");
        assertEquals("Updated description", updatedProduct.getBeschrijving(), "Product description should be updated");
        assertEquals(400.0, updatedProduct.getPrijs(), 0.001, "Product price should remain unchanged");

        // Optionally, verify that the OVChipkaart relationship remains
        List<Product> products = productDAO.findByOVChipkaart(ovChipkaart);
        assertTrue(products.contains(updatedProduct), "Relationship between Product and OVChipkaart should exist");
    }

    @Test
    void testDeleteProduct() throws SQLException {
        // Create and save a Reiziger first
        Reiziger reiziger = new Reiziger("F", null, "Memari", Date.valueOf("2002-12-03"));
        boolean reizigerSaved = reizigerDAO.save(reiziger);
        assertTrue(reizigerSaved, "Reiziger should be saved");

        // Create and save a product first
        Product product = new Product(104L, "DeleteMe", "To be deleted", 50L);
        OVChipkaart ovChipkaart = new OVChipkaart(300L, Date.valueOf("2025-12-12"), 1, 300.0, reiziger);
        product.addOVChipkaart(ovChipkaart);
        boolean saved = productDAO.save(product);
        assertTrue(saved, "Product should be saved");

        // Now delete the product
        boolean deleted = productDAO.delete(product);
        assertTrue(deleted, "Product should be deleted");

        // Try to retrieve the product, it should no longer exist
        Product deletedProduct = productDAO.findById(104L);
        assertNull(deletedProduct, "Product should not exist after deletion");
    }

    @Test
    void testFindAllProducts() throws SQLException {
        // Create and save a Reiziger first
        Reiziger reiziger1 = new Reiziger("G", null, "Piccardo", Date.valueOf("2002-12-03"));
        Reiziger reiziger2 = new Reiziger("K", "de", "Vries", Date.valueOf("1999-05-15"));
        boolean reiziger1Saved = reizigerDAO.save(reiziger1);
        boolean reiziger2Saved = reizigerDAO.save(reiziger2);
        assertTrue(reiziger1Saved && reiziger2Saved, "Reizigers should be saved");

        // Create and save multiple products
        Product product1 = new Product(105L, "Product1", "Test product 1", 150L);
        OVChipkaart ovChipkaart1 = new OVChipkaart(400L, Date.valueOf("2025-12-12"), 1, 400.0, reiziger1);
        product1.addOVChipkaart(ovChipkaart1);

        Product product2 = new Product(106L, "Product2", "Test product 2", 250L);
        OVChipkaart ovChipkaart2 = new OVChipkaart(500L, Date.valueOf("2025-12-12"), 1, 500.0, reiziger2);
        product2.addOVChipkaart(ovChipkaart2);

        boolean saved1 = productDAO.save(product1);
        boolean saved2 = productDAO.save(product2);
        assertTrue(saved1 && saved2, "Products should be saved");

        // Retrieve all products
        List<Product> products = productDAO.findAll();
        assertNotNull(products, "Products list should not be null");
        assertFalse(products.isEmpty(), "Products list should not be empty");
        assertTrue(products.size() >= 2, "There should be at least two products");
    }
}

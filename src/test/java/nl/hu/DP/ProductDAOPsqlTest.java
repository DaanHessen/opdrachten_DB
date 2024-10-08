package nl.hu.DP;

import org.junit.jupiter.api.*;
import java.sql.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductDAOPsqlTest {
    private Connection connection;
    private ProductDAOPsql productDAO;

    @BeforeAll
    public void init() throws SQLException {
        // Initialize the database connection
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Kq)44Ne@");
        productDAO = new ProductDAOPsql(connection);
    }

    @BeforeEach
    public void setUp() throws SQLException {
        // Clean up tables to ensure a fresh state
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM ov_chipkaart_product"); // If exists
            stmt.executeUpdate("DELETE FROM product");
            stmt.executeUpdate("DELETE FROM ov_chipkaart"); // Ensure no OVChipkaart is left
            // Add more DELETE statements if there are other related tables
        }

        // Insert necessary test data
        // Insert an OVChipkaart first
        String insertOVSQL = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertOVSQL)) {
            pstmt.setLong(1, 100L);
            pstmt.setString(2, "2025-12-31");
            pstmt.setLong(3, 1L);
            pstmt.setLong(4, 500L);
            pstmt.setLong(5, 1L); // Assuming a Reiziger with ID 1 exists
            pstmt.executeUpdate();
        }

        // Insert a Product associated with the OVChipkaart
        Product product = new Product(1L, "Test Product", "Test Description", 100L);
        OVChipkaart ovChipkaart = new OVChipkaart();
        ovChipkaart.setKaartnummer(100L);
        product.setOvChipkaart(ovChipkaart);
        Assertions.assertTrue(productDAO.save(product), "Product should be saved successfully");
    }

    @AfterAll
    public void tearDownAll() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testDeleteProduct() {
        // Find the product
        Product product = productDAO.findById(1L);
        Assertions.assertNotNull(product, "Product should exist before deletion");

        // Delete the product
        boolean deleted = productDAO.delete(product);
        Assertions.assertTrue(deleted, "Product should be deleted successfully");

        // Verify deletion
        Product deletedProduct = productDAO.findById(1L);
        Assertions.assertNull(deletedProduct, "Product should no longer exist after deletion");
    }

    // Additional test methods...
}

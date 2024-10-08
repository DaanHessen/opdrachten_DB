package nl.hu.DP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {
    private Connection connection;

    public ProductDAOPsql(Connection connection) {
        this.connection = connection;
    }

    /**
     * Saves a Product to the database.
     *
     * @param product The Product to save.
     * @return True if the operation was successful, false otherwise.
     */
    @Override
    public boolean save(Product product) {
        String insertProductSQL = "INSERT INTO product (product_nummer, naam, beschrijving, prijs, kaartnummer) VALUES (?, ?, ?, ?, ?)";

        try {
            // Start transaction
            connection.setAutoCommit(false);

            // Insert into product table
            try (PreparedStatement pstmt = connection.prepareStatement(insertProductSQL)) {
                pstmt.setLong(1, product.getProductNummer());
                pstmt.setString(2, product.getNaam());
                pstmt.setString(3, product.getBeschrijving());
                pstmt.setLong(4, product.getPrijs());

                if (product.getOvChipkaarts() != null) {
                    pstmt.setLong(5, product.getOvChipkaarts().getKaartnummer());
                } else {
                    pstmt.setNull(5, java.sql.Types.BIGINT);
                }

                pstmt.executeUpdate();
            }

            // Commit transaction
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // Rollback transaction on error
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            try {
                // Restore default commit behavior
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Updates an existing Product in the database.
     *
     * @param product The Product with updated information.
     * @return True if the operation was successful, false otherwise.
     */
    @Override
    public boolean update(Product product) {
        String updateProductSQL = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ?, kaartnummer = ? WHERE product_nummer = ?";

        try {
            // Start transaction
            connection.setAutoCommit(false);

            // Update product table
            try (PreparedStatement pstmt = connection.prepareStatement(updateProductSQL)) {
                pstmt.setString(1, product.getNaam());
                pstmt.setString(2, product.getBeschrijving());
                pstmt.setLong(3, product.getPrijs());

                if (product.getOvChipkaarts() != null) {
                    pstmt.setLong(4, product.getOvChipkaarts().getKaartnummer());
                } else {
                    pstmt.setNull(4, java.sql.Types.BIGINT);
                }

                pstmt.setLong(5, product.getProductNummer());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Updating product failed, no rows affected.");
                }
            }

            // Commit transaction
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // Rollback transaction on error
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            try {
                // Restore default commit behavior
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Deletes a Product from the database.
     *
     * @param product The Product to delete.
     * @return True if the operation was successful, false otherwise.
     */
    @Override
    public boolean delete(Product product) {
        String deleteProductSQL = "DELETE FROM product WHERE product_nummer = ?";

        try {
            // Start transaction
            connection.setAutoCommit(false);

            // Delete from product table
            try (PreparedStatement pstmt = connection.prepareStatement(deleteProductSQL)) {
                pstmt.setLong(1, product.getProductNummer());
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Deleting product failed, no rows affected.");
                }
            }

            // Commit transaction
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // Rollback transaction on error
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            try {
                // Restore default commit behavior
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Finds a Product by its product number.
     *
     * @param productNummer The product number to search for.
     * @return The Product if found, otherwise null.
     */
    @Override
    public Product findById(Long productNummer) {
        Product product = null;
        String selectProductSQL = "SELECT product_nummer, naam, beschrijving, prijs, kaartnummer FROM product WHERE product_nummer = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(selectProductSQL)) {
            pstmt.setLong(1, productNummer);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Long kaartnummer = rs.getLong("kaartnummer");
                    if (rs.wasNull()) {
                        kaartnummer = null;
                    }

                    product = new Product(
                            rs.getLong("product_nummer"),
                            rs.getString("naam"),
                            rs.getString("beschrijving"),
                            rs.getLong("prijs")
                    );

                    if (kaartnummer != null) {
                        OVChipkaart ovChipkaart = findOVChipkaartByKaartnummer(kaartnummer);
                        product.setOvChipkaarts(ovChipkaart);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }

    /**
     * Finds all Products associated with a specific OVChipkaart.
     *
     * @param ovChipkaart The OVChipkaart whose Products are to be retrieved.
     * @return A list of associated Products.
     */
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        List<Product> producten = new ArrayList<>();
        String selectProductsSQL = "SELECT product_nummer, naam, beschrijving, prijs FROM product WHERE kaartnummer = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(selectProductsSQL)) {
            pstmt.setLong(1, ovChipkaart.getKaartnummer());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getLong("product_nummer"),
                            rs.getString("naam"),
                            rs.getString("beschrijving"),
                            rs.getLong("prijs")
                    );
                    product.setOvChipkaarts(ovChipkaart); // Set the associated OVChipkaart
                    producten.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return producten;
    }

    /**
     * Retrieves all Products from the database.
     *
     * @return A list of all Products.
     */
    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String selectAllSQL = "SELECT product_nummer, naam, beschrijving, prijs, kaartnummer FROM product";

        try (PreparedStatement pstmt = connection.prepareStatement(selectAllSQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Long productNummer = rs.getLong("product_nummer");
                String naam = rs.getString("naam");
                String beschrijving = rs.getString("beschrijving");
                Long prijs = rs.getLong("prijs");
                Long kaartnummer = rs.getLong("kaartnummer");
                if (rs.wasNull()) {
                    kaartnummer = null; // Handle NULL kaartnummer
                }

                Product product = new Product(productNummer, naam, beschrijving, prijs);

                if (kaartnummer != null) {
                    OVChipkaart ovChipkaart = findOVChipkaartByKaartnummer(kaartnummer);
                    product.setOvChipkaart(ovChipkaart);
                }

                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally, you can log the exception or rethrow it
        }

        return products;
    }

    /**
     * Helper method to find an OVChipkaart by its kaartnummer.
     *
     * @param kaartnummer The kaartnummer of the OVChipkaart.
     * @return The OVChipkaart if found, otherwise null.
     */
    private OVChipkaart findOVChipkaartByKaartnummer(Long kaartnummer) {
        if (kaartnummer == null) {
            return null;
        }

        String selectOVSQL = "SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id FROM ov_chipkaart WHERE kaart_nummer = ?";
        OVChipkaart ovChipkaart = null;

        try (PreparedStatement pstmt = connection.prepareStatement(selectOVSQL)) {
            pstmt.setLong(1, kaartnummer);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String geldigTot = rs.getString("geldig_tot");
                    Long klasse = rs.getLong("klasse");
                    Long saldo = rs.getLong("saldo");
                    Long reizigerId = rs.getLong("reiziger_id");

                    // Assuming you have a ReizigerDAO to fetch Reiziger by ID
                    Reiziger reiziger = findReizigerById(reizigerId);

                    ovChipkaart = new OVChipkaart();
                    ovChipkaart.setKaartnummer(kaartnummer);
                    ovChipkaart.setGeldigTot(geldigTot);
                    ovChipkaart.setKlasse(klasse);
                    ovChipkaart.setSaldo(saldo);
                    ovChipkaart.setReiziger(reiziger);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally, you can log the exception or rethrow it
        }

        return ovChipkaart;
    }

    /**
     * Helper method to find a Reiziger by its ID.
     *
     * @param reizigerId The ID of the Reiziger.
     * @return The Reiziger if found, otherwise null.
     */
    private Reiziger findReizigerById(Long reizigerId) {
        if (reizigerId == null) {
            return null;
        }

        String selectReizigerSQL = "SELECT id, naam FROM reiziger WHERE id = ?";
        Reiziger reiziger = null;

        try (PreparedStatement pstmt = connection.prepareStatement(selectReizigerSQL)) {
            pstmt.setLong(1, reizigerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Long id = rs.getLong("id");
                    String naam = rs.getString("naam");

                    reiziger = new Reiziger();
                    reiziger.setId(id);
                    reiziger.setNaam(naam);
                    // Set other fields as necessary
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally, you can log the exception or rethrow it
        }

        return reiziger;
    }
}

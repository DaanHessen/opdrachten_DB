package nl.hu.DP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {
    private Connection connection;
    private OVChipkaartDAO ovChipkaartDAO;

    public ProductDAOPsql(Connection connection, OVChipkaartDAO ovChipkaartDAO) {
        this.connection = connection;
        this.ovChipkaartDAO = ovChipkaartDAO;
    }

    @Override
    public boolean save(Product product) {
        String insertProductSQL = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) " +
                "VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (product_nummer) DO UPDATE SET naam = EXCLUDED.naam, beschrijving = EXCLUDED.beschrijving, prijs = EXCLUDED.prijs";
        String insertRelationSQL = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?) ON CONFLICT DO NOTHING";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement pstmtProduct = connection.prepareStatement(insertProductSQL)) {
                pstmtProduct.setLong(1, product.getProductNummer());
                pstmtProduct.setString(2, product.getNaam());
                pstmtProduct.setString(3, product.getBeschrijving());
                pstmtProduct.setDouble(4, product.getPrijs());
                pstmtProduct.executeUpdate();
            }

            try (PreparedStatement pstmtRelation = connection.prepareStatement(insertRelationSQL)) {
                for (OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
                    OVChipkaart existingCard = ovChipkaartDAO.findByKaartNummer(ovChipkaart.getKaartnummer());
                    if (existingCard == null) {
                        boolean saved = ovChipkaartDAO.save(ovChipkaart);
                        if (!saved) {
                            throw new SQLException("Failed to save OVChipkaart with kaart_nummer: " + ovChipkaart.getKaartnummer());
                        }
                    }

                    pstmtRelation.setLong(1, ovChipkaart.getKaartnummer());
                    pstmtRelation.setLong(2, product.getProductNummer());
                    pstmtRelation.addBatch();
                }
                pstmtRelation.executeBatch();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean update(Product product) {
        String updateProductSQL = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?";
        String deleteOVSQL = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        String insertOVSQL = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement pstmtProduct = connection.prepareStatement(updateProductSQL)) {
                pstmtProduct.setString(1, product.getNaam());
                pstmtProduct.setString(2, product.getBeschrijving());
                pstmtProduct.setDouble(3, product.getPrijs());
                pstmtProduct.setLong(4, product.getProductNummer());
                pstmtProduct.executeUpdate();
            }

            try (PreparedStatement pstmtDelete = connection.prepareStatement(deleteOVSQL)) {
                pstmtDelete.setLong(1, product.getProductNummer());
                pstmtDelete.executeUpdate();
            }

            try (PreparedStatement pstmtInsert = connection.prepareStatement(insertOVSQL)) {
                for (OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
                    OVChipkaart existingCard = ovChipkaartDAO.findByKaartNummer(ovChipkaart.getKaartnummer());
                    if (existingCard == null) {
                        boolean saved = ovChipkaartDAO.save(ovChipkaart);
                        if (!saved) {
                            throw new SQLException("Failed to save OVChipkaart with kaart_nummer: " + ovChipkaart.getKaartnummer());
                        }
                    }

                    pstmtInsert.setLong(1, ovChipkaart.getKaartnummer());
                    pstmtInsert.setLong(2, product.getProductNummer());
                    pstmtInsert.addBatch();
                }
                pstmtInsert.executeBatch();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean delete(Product product) {
        String deleteProductSQL = "DELETE FROM product WHERE product_nummer = ?";
        String deleteOVSQL = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement pstmtRelation = connection.prepareStatement(deleteOVSQL)) {
                pstmtRelation.setLong(1, product.getProductNummer());
                pstmtRelation.executeUpdate();
            }

            try (PreparedStatement pstmtProduct = connection.prepareStatement(deleteProductSQL)) {
                pstmtProduct.setLong(1, product.getProductNummer());
                pstmtProduct.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Product findById(Long productNummer) {
        Product product = null;
        String selectProductSQL = "SELECT product_nummer, naam, beschrijving, prijs FROM product WHERE product_nummer = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(selectProductSQL)) {
            pstmt.setLong(1, productNummer);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    product = new Product(
                            rs.getLong("product_nummer"),
                            rs.getString("naam"),
                            rs.getString("beschrijving"),
                            rs.getLong("prijs")
                    );

                    String selectRelationsSQL = "SELECT kaart_nummer FROM ov_chipkaart_product WHERE product_nummer = ?";
                    try (PreparedStatement pstmtRelations = connection.prepareStatement(selectRelationsSQL)) {
                        pstmtRelations.setLong(1, productNummer);
                        try (ResultSet rsRelations = pstmtRelations.executeQuery()) {
                            while (rsRelations.next()) {
                                long kaartNummer = rsRelations.getLong("kaart_nummer");
                                OVChipkaart ovChipkaart = ovChipkaartDAO.findByKaartNummer(kaartNummer);
                                if (ovChipkaart != null) {
                                    product.addOVChipkaart(ovChipkaart);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        List<Product> producten = new ArrayList<>();
        String sql = "SELECT p.product_nummer, p.naam, p.beschrijving, p.prijs " +
                "FROM product p " +
                "JOIN ov_chipkaart_product op ON p.product_nummer = op.product_nummer " +
                "WHERE op.kaart_nummer = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, ovChipkaart.getKaartnummer());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getLong("product_nummer"),
                            rs.getString("naam"),
                            rs.getString("beschrijving"),
                            rs.getLong("prijs")
                    );
                    producten.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return producten;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String selectAllSQL = "SELECT product_nummer, naam, beschrijving, prijs FROM product";

        try (PreparedStatement pstmt = connection.prepareStatement(selectAllSQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Product product = new Product(
                        rs.getLong("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getLong("prijs")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }
}

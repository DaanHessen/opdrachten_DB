package nl.hu.DP;

import java.util.List;

public interface ProductDAO {
    boolean save(Product product);
    boolean update(Product product);
    boolean delete(Product product);
    Product findById(Long productNummer);
    List<Product> findByOVChipkaart(OVChipkaart ovChipkaart);
    List<Product> findAll(); // Ensure this is correctly spelled
}

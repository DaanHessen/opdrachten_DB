package nl.hu.DP.repository;

import nl.hu.DP.domain.OVChipkaart;
import nl.hu.DP.domain.Product;

import java.util.List;

public interface ProductDAO {
    boolean save(Product product);
    boolean update(Product product);
    boolean delete(Product product);
    Product findById(long productNummer);

    List<Product> findByOVChipkaart(OVChipkaart ovChipkaart);

    List<Product> findAll();
    long getNextProductNummer();
}
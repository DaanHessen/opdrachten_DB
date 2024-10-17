package nl.hu.DP.application;

import nl.hu.DP.domain.OVChipkaart;
import nl.hu.DP.domain.Product;
import nl.hu.DP.repository.ProductDAO;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ProductDAOHibernate implements ProductDAO {
    private EntityManager entityManager;

    public ProductDAOHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public long getNextProductNummer() {
        Long maxId = (Long) entityManager.createQuery("SELECT MAX(p.productNummer) FROM Product p").getSingleResult();
        return (maxId != null ? maxId + 1 : 1);
    }

    @Override
    public boolean save(Product product) {
        try {
            entityManager.persist(product);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Product product) {
        try {
            entityManager.merge(product);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Product product) {
        try {
            Product managedProduct = entityManager.find(Product.class, product.getProductNummer());
            if (managedProduct != null) {
                entityManager.remove(managedProduct);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Product findById(long productNummer) {
        try {
            return entityManager.find(Product.class, productNummer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        try {
            TypedQuery<Product> query = entityManager.createQuery(
                    "SELECT p FROM Product p JOIN p.ovChipkaarten o WHERE o = :ovChipkaart", Product.class);
            query.setParameter("ovChipkaart", ovChipkaart);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> findAll() {
        try {
            TypedQuery<Product> query = entityManager.createQuery(
                    "SELECT p FROM Product p", Product.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
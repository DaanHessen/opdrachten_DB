package nl.hu.DP;

import java.sql.SQLException;
import java.util.List;

public interface AdresDAO {
    boolean save(Adres adres) throws SQLException;
    boolean update(Adres adres);
    boolean delete(Adres adres) throws SQLException;
    Adres findById(long adres_id);
    Adres findByReiziger(Reiziger reiziger);
    List<Adres> findAll();
}


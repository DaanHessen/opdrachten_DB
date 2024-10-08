package nl.hu.DP;

import java.util.List;

public interface AdresDAO {
    boolean save(Adres adres);
    boolean update(Adres adres);
    boolean delete(Adres adres);
    Adres findById(long adres_id);
    Adres findByReiziger(Reiziger reiziger);
    List<Adres> findAll();
}


package nl.hu.DP.repository;

import nl.hu.DP.domain.Adres;

import java.util.List;

public interface AdresDAO {
    boolean save(Adres adres);
    boolean update(Adres adres);
    boolean delete(Adres adres);
    Adres findById(long adresId);
    List<Adres> findAll();
    long getNextAdresId();
}
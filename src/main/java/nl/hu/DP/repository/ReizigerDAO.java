package nl.hu.DP.repository;

import nl.hu.DP.domain.Reiziger;

import java.util.Date;
import java.util.List;

public interface ReizigerDAO {
    boolean save(Reiziger reiziger);
    boolean update(Reiziger reiziger);
    boolean delete(Reiziger reiziger);
    Reiziger findById(long reiziger_id);
    List<Reiziger> findByGeboortedatum(Date geboortedatum);
    List<Reiziger> findAll();
}
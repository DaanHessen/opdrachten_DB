package nl.hu.DP.repository;

import nl.hu.DP.domain.OVChipkaart;
import nl.hu.DP.domain.Reiziger;

import java.util.List;

public interface OVChipkaartDAO {
    boolean save(OVChipkaart ovChipkaart);
    boolean update(OVChipkaart ovChipkaart);
    boolean delete(OVChipkaart ovChipkaart);
    OVChipkaart findById(long kaartNummer);

    List<OVChipkaart> findByReiziger(Reiziger reiziger);

    List<OVChipkaart> findAll();
    long getNextOVChipkaartId();
}
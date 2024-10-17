package nl.hu.DP.repository;

import nl.hu.DP.domain.OVChipkaart;
import nl.hu.DP.domain.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {
    boolean save(OVChipkaart ovChipkaart) throws SQLException;
    boolean update(OVChipkaart ovChipkaart) throws SQLException;
    boolean delete(OVChipkaart ovChipkaart) throws SQLException;
    OVChipkaart findByKaartNummer(long kaartnummer) throws SQLException;
    List<OVChipkaart> findAll() throws SQLException;
    List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;
}

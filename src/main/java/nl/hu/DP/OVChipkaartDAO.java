package nl.hu.DP;

import java.util.List;

public interface OVChipkaartDAO {
    boolean save(OVChipkaart ovChipkaart);
    boolean update(OVChipkaart ovChipkaart);
    boolean delete(OVChipkaart ovChipkaart);
    OVChipkaart findById(long ovChipkaart_id);
    List<OVChipkaart> findAll();
    List<OVChipkaart> findByGbdatum(String gbdatum);
    List<OVChipkaart> findByReiziger(Reiziger reiziger);
}

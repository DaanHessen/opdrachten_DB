package nl.hu.DP;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection connection;

    public OVChipkaartDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        String sql = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON CONFLICT (kaart_nummer) DO NOTHING";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, ovChipkaart.getKaartnummer());
            pstmt.setDate(2, ovChipkaart.getGeldigTot());
            pstmt.setInt(3, ovChipkaart.getKlasse());
            pstmt.setDouble(4, ovChipkaart.getSaldo());
            pstmt.setLong(5, ovChipkaart.getReizigerId());
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {
        String sql = "UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? WHERE kaart_nummer = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, ovChipkaart.getGeldigTot());
            pstmt.setInt(2, ovChipkaart.getKlasse());
            pstmt.setDouble(3, ovChipkaart.getSaldo());
            pstmt.setLong(4, ovChipkaart.getReizigerId());
            pstmt.setLong(5, ovChipkaart.getKaartnummer());
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        String sql = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, ovChipkaart.getKaartnummer());
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public OVChipkaart findByKaartNummer(long kaartnummer) throws SQLException {
        String sql = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, kaartnummer);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    OVChipkaart ovChipkaart = new OVChipkaart();
                    ovChipkaart.setKaartnummer(rs.getLong("kaart_nummer"));
                    ovChipkaart.setGeldigTot(rs.getDate("geldig_tot"));
                    ovChipkaart.setKlasse(rs.getInt("klasse"));
                    ovChipkaart.setSaldo(rs.getDouble("saldo"));
                    ovChipkaart.setReizigerId(rs.getLong("reiziger_id"));
                    return ovChipkaart;
                }
            }
        }
        return null;
    }

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        String sql = "SELECT * FROM ov_chipkaart";
        List<OVChipkaart> kaarten = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                OVChipkaart ovChipkaart = new OVChipkaart();
                ovChipkaart.setKaartnummer(rs.getLong("kaart_nummer"));
                ovChipkaart.setGeldigTot(rs.getDate("geldig_tot"));
                ovChipkaart.setKlasse(rs.getInt("klasse"));
                ovChipkaart.setSaldo(rs.getDouble("saldo"));
                ovChipkaart.setReizigerId(rs.getLong("reiziger_id"));
                kaarten.add(ovChipkaart);
            }
        }
        return kaarten;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        String sql = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
        List<OVChipkaart> kaarten = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, reiziger.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OVChipkaart ovChipkaart = new OVChipkaart();
                    ovChipkaart.setKaartnummer(rs.getLong("kaart_nummer"));
                    ovChipkaart.setGeldigTot(rs.getDate("geldig_tot"));
                    ovChipkaart.setKlasse(rs.getInt("klasse"));
                    ovChipkaart.setSaldo(rs.getDouble("saldo"));
                    ovChipkaart.setReizigerId(rs.getLong("reiziger_id"));
                    kaarten.add(ovChipkaart);
                }
            }
        }
        return kaarten;
    }
}

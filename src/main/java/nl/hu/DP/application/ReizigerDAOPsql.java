package nl.hu.DP.application;

import nl.hu.DP.domain.Reiziger;
import nl.hu.DP.repository.ReizigerDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection connection;

    public ReizigerDAOPsql(Connection connection) {
        this.connection = connection;
    }

    private Long getNextReizigerId() throws SQLException {
        String query = "SELECT MAX(reiziger_id) AS max_id FROM reiziger";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                long maxId = rs.getLong("max_id");
                return (rs.wasNull()) ? 1L : maxId + 1L;
            }
        }
        return 1L;
    }


    @Override
    public boolean save(Reiziger reiziger) {
        String insertSQL = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";
        try {
            connection.setAutoCommit(false);

            if (reiziger.getId() == null) {
                reiziger.setId(getNextReizigerId());
            }

            try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
                pstmt.setLong(1, reiziger.getId());
                pstmt.setString(2, reiziger.getVoorletters());
                pstmt.setString(3, reiziger.getTussenvoegsel());
                pstmt.setString(4, reiziger.getAchternaam());
                pstmt.setDate(5, new java.sql.Date(reiziger.getGeboortedatum().getTime()));
                pstmt.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        String updateSQL = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                pstmt.setString(1, reiziger.getVoorletters());
                pstmt.setString(2, reiziger.getTussenvoegsel());
                pstmt.setString(3, reiziger.getAchternaam());
                pstmt.setDate(4, new java.sql.Date(reiziger.getGeboortedatum().getTime()));
                pstmt.setLong(5, reiziger.getId());
                pstmt.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        String deleteSQL = "DELETE FROM reiziger WHERE reiziger_id = ?";
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
                pstmt.setLong(1, reiziger.getId());
                pstmt.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
    }

    @Override
    public Reiziger findById(long id) {
        String selectSQL = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        Reiziger reiziger = null;

        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    reiziger = new Reiziger();
                    reiziger.setId(rs.getLong("reiziger_id"));
                    reiziger.setVoorletters(rs.getString("voorletters"));
                    reiziger.setTussenvoegsel(rs.getString("tussenvoegsel"));
                    reiziger.setAchternaam(rs.getString("achternaam"));
                    reiziger.setGeboortedatum(rs.getDate("geboortedatum"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reiziger;
    }

    @Override
    public List<Reiziger> findByGeboortedatum(Date geboortedatum) {
        String selectSQL = "SELECT * FROM reiziger WHERE geboortedatum = ?";
        List<Reiziger> reizigers = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setDate(1, new java.sql.Date(geboortedatum.getTime()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reiziger reiziger = new Reiziger();
                    reiziger.setId(rs.getLong("reiziger_id"));
                    reiziger.setVoorletters(rs.getString("voorletters"));
                    reiziger.setTussenvoegsel(rs.getString("tussenvoegsel"));
                    reiziger.setAchternaam(rs.getString("achternaam"));
                    reiziger.setGeboortedatum(rs.getDate("geboortedatum"));
                    reizigers.add(reiziger);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() {
        String selectSQL = "SELECT * FROM reiziger";
        List<Reiziger> reizigers = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Reiziger reiziger = new Reiziger();
                reiziger.setId(rs.getLong("reiziger_id"));
                reiziger.setVoorletters(rs.getString("voorletters"));
                reiziger.setTussenvoegsel(rs.getString("tussenvoegsel"));
                reiziger.setAchternaam(rs.getString("achternaam"));
                reiziger.setGeboortedatum(rs.getDate("geboortedatum"));
                reizigers.add(reiziger);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reizigers;
    }
}

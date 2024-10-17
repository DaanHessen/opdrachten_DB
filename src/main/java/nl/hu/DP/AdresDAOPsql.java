package nl.hu.DP;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    private Connection connection;

    public AdresDAOPsql(Connection connection) {
        this.connection = connection;
    }

    private Long getNextAdresId() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT MAX (adres_id) FROM adres");
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                Long maxId = rs.getLong("max_id");
                return (maxId == null) ? 1L : maxId + 1L;
            }
        }
        return 1L;
    }

    private Long getNextReizigerId() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT MAX (reiziger_id) FROM reiziger");
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                Long maxId = rs.getLong("max_id");
                return (maxId == null) ? 1L : maxId + 1L;
            }
        }
        return 1L;
    }

    @Override
    public boolean save(Adres adres) {
        String saveAdres = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)";
        String saveReiziger = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            if (adres.getReiziger() != null) {
                Reiziger reiziger = adres.getReiziger();
                if (reiziger.getId() == null) {
                    reiziger.setId(getNextReizigerId());
                    try (PreparedStatement statement = connection.prepareStatement(saveReiziger)) {
                        statement.setLong(1, reiziger.getId());
                        statement.setString(2, reiziger.getVoorletters());
                        statement.setString(3, reiziger.getTussenvoegsel());
                        statement.setString(4, reiziger.getAchternaam());
                        statement.setDate(5, reiziger.getGeboortedatum());
                        statement.executeUpdate();
                    }
                }
            }

            if (adres.getId() == null) {
                adres.setId(getNextAdresId());
            }

            try (PreparedStatement statement = connection.prepareStatement(saveAdres)) {
                statement.setLong(1, adres.getId());
                statement.setString(2, adres.getPostcode());
                statement.setString(3, adres.getHuisnummer());
                statement.setString(4, adres.getStraat());
                statement.setString(5, adres.getWoonplaats());
                statement.setLong(6, adres.getReiziger().getId());
                statement.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException f) {
                f.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException g) {
                g.printStackTrace();
            }
        }
    }

    @Override
    public boolean update(Adres adres) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?")) {
            statement.setString(1, adres.getPostcode());
            statement.setString(2, adres.getHuisnummer());
            statement.setString(3, adres.getStraat());
            statement.setString(4, adres.getWoonplaats());
            statement.setLong(5, adres.getReiziger().getId());
            statement.setLong(6, adres.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM adres WHERE adres_id = ?")) {
            statement.setLong(1, adres.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Adres findById(long adres_id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM adres WHERE adres_id = ?")) {
            statement.setLong(1, adres_id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Adres(
                            rs.getString("postcode"),
                            rs.getString("huisnummer"),
                            rs.getString("straat"),
                            rs.getString("woonplaats"),
                            findReizigerById(rs.getLong("reiziger_id"))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM adres WHERE reiziger_id = ?")) {
            statement.setLong(1, reiziger.getId());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Adres(
                            rs.getString("postcode"),
                            rs.getString("huisnummer"),
                            rs.getString("straat"),
                            rs.getString("woonplaats"),
                            reiziger
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Adres> findAll() {
        List<Adres> adressen = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM adres");
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Adres adres = new Adres(
                        rs.getString("postcode"),
                        rs.getString("huisnummer"),
                        rs.getString("straat"),
                        rs.getString("woonplaats"),
                        findReizigerById(rs.getLong("reiziger_id"))
                );
                adressen.add(adres);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adressen;
    }

    private Reiziger findReizigerById(long reizigerId) {
        return null;
    }
}
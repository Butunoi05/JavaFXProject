package Repository;

import Domain.Comanda;
import Domain.Produs;
import org.sqlite.SQLiteDataSource;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ComandaDBRepository extends Repository.RepoGeneric<Comanda> {
    private final String JDBC_URL = "jdbc:sqlite:DB1.sqlite";
    private Connection connection;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ComandaDBRepository() throws SQLException, Repository.RepositoryException {
        openConnection();
        createTables();
        loadDataInMemory();
    }

    private void openConnection() throws SQLException {
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl(JDBC_URL);
        if (connection == null || connection.isClosed()) {
            connection = ds.getConnection();
        }
    }
    private void createTables() {
        try (Statement st = connection.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS comanda (" +
                    "id INTEGER PRIMARY KEY," +
                    "dataLivrare TEXT" +
                    ")");
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table 'comanda'", e);
        }
        try (Statement st = connection.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS comanda_produs (" +
                    "comanda_id INTEGER," +
                    "produs_id INTEGER," +
                    "FOREIGN KEY(comanda_id) REFERENCES comanda(id)," +
                    "FOREIGN KEY(produs_id) REFERENCES produs(id)" +
                    ")");
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table 'comanda_produs'", e);
        }
    }
    public void adauga(Comanda comanda) throws Repository.RepositoryException {
        if (comanda != null) {
            try {
                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO comanda (id, dataLivrare) VALUES (?, ?)")) {
                    statement.setInt(1, comanda.getId());
                    String dataStr = dateFormat.format(comanda.getDataLivrare());
                    statement.setString(2, dataStr);
                    statement.executeUpdate();
                }
                ArrayList<Produs> produse = (ArrayList<Produs>) comanda.getProdusList();
                if (produse != null) {
                    for (Produs produs : produse) {
                        try (PreparedStatement statement = connection.prepareStatement(
                                "INSERT INTO comanda_produs (comanda_id, produs_id) VALUES (?,?)")) {
                            statement.setInt(1, comanda.getId());
                            statement.setInt(2, produs.getId());
                            statement.executeUpdate();
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error adding comanda", e);
            }
        }
        super.adauga(comanda);
    }
    public void stergereId(int id) {
        try {
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM comanda_produs WHERE comanda_id = ?")) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM comanda WHERE id = ?")) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting comanda", e);
        }
        super.stergereId(id);
    }
    public List<Comanda> getAll() {
        List<Comanda> comenzi = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM comanda");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String dataStr = resultSet.getString("dataLivrare");
                Date dataLivrare = dateFormat.parse(dataStr);
                ArrayList<Produs> produse = getProduseForComanda(id);
                Comanda comanda = new Comanda(id, produse, dataLivrare);
                comenzi.add(comanda);
            }
        } catch (SQLException | ParseException e) {
            throw new RuntimeException("Error fetching all comenzi", e);
        }
        return comenzi;
    }
    private ArrayList<Produs> getProduseForComanda(int comandaId) {
        ArrayList<Produs> produse = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT produs_id FROM comanda_produs WHERE comanda_id = ?")) {
            statement.setInt(1, comandaId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {

                    int produsId = rs.getInt("produs_id");

                    Produs produs = getProdusById(produsId);
                    if (produs != null) {
                        produse.add(produs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching produse for comanda", e);
        }
        return produse;
    }
    private Produs getProdusById(int produsId) {
        Produs produs = null;

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM produs WHERE id = ?")) {
            statement.setInt(1, produsId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");

                    String categorie = rs.getString("categorie");

                    String nume = rs.getString("nume");

                    int pret = rs.getInt("pret");

                    produs = new Produs(id, categorie, nume, pret);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching produs by id", e);
        }
        return produs;
    }
    private void loadDataInMemory() throws RepositoryException {
        for (Comanda comanda : getAll()) {
            super.adauga(comanda);
        }
    }
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package Repository;


import Domain.Produs;
import Repository.RepoGeneric;
import Repository.RepositoryException;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdusDBRepository extends RepoGeneric<Produs> {
    private final String JDBC_URL = "jdbc:sqlite:DB1.sqlite";
    private Connection connection;

    public ProdusDBRepository() throws SQLException, RepositoryException {
        openConnection();
        createTable();
        //initData();
        loadDataInMemory();
    }

    private void openConnection() throws SQLException {
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl(JDBC_URL);
        if (connection == null || connection.isClosed()) {
            connection = ds.getConnection();
        }
    }

    private void createTable() {
        try (Statement st = connection.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS produs (" +
                    "id int ," +
                    "categorie varchar(100)," +
                    "nume varchar(100)," +
                    "pret int)");
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table", e);
        }
    }

////    private void initData() {
////        // Example data initialization (optional)
////        List<Produs> produse = List.of(
////                new Produs("Electronics", "Laptop", 3500),
////                new Produs("Groceries", "Apple", 3),
////                new Produs("Clothing", "T-shirt", 50)
////        );
//
//        for (Produs produs : produse) {
//            try {
//                adauga(produs);
//            } catch (RepositoryException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void adauga(Produs produs) throws RepositoryException {
        if (produs != null) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO produs (id,categorie, nume, pret) VALUES (?,?, ?, ?)")) {
                statement.setInt(1, produs.getId());
                statement.setString(2, produs.getCategorie());
                statement.setString(3, produs.getNume());
                statement.setInt(4, produs.getPret());
                statement.executeUpdate();

                // Obține ultimul id generat
                try (Statement lastIdStatement = connection.createStatement();
                     ResultSet resultSet = lastIdStatement.executeQuery("SELECT last_insert_rowid()")) {
                    if (resultSet.next()) {
                        produs.setId(resultSet.getInt(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error adding produs", e);
            }
        }
        super.adauga(produs);
    }

    public void stergereId(int id) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) as count from comanda_produs where produs_id= ?")) {
            statement.setInt(1, id);
            try(ResultSet rs=statement.executeQuery()){
                if(rs.next() && rs.getInt("count")>0){
                    throw new RepositoryException("Produsul cu id "+id+" se afla intr-o comanda si nu poate sa fie sters");
                }
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting produs", e);
        }
        try(PreparedStatement statement=connection.prepareStatement(
                "DELETE FROM produs WHERE id= ?")){
            statement.setInt(1,id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        super.stergereId(id);
    }

    public List<Produs> getAll() {
        List<Produs> produse = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM produs");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String categorie = resultSet.getString("categorie");
                String nume = resultSet.getString("nume");
                int pret = resultSet.getInt("pret");

                produse.add(new Produs(id, categorie, nume, pret));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all produse", e);
        }

        return produse;
    }

    private void loadDataInMemory() throws RepositoryException {
        for (Produs produs : getAll()) {
            super.adauga(produs);
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

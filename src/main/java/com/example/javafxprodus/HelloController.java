package com.example.javafxprodus;

import Domain.Comanda;
import Domain.Produs;
import Repository.ComandaDBRepository;
import Repository.ProdusDBRepository;
import Repository.RepositoryException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HelloController {
    // Tabelul și coloanele pentru Produse
    @FXML private TableView<Produs> produsTable;
    @FXML private TableColumn<Produs, Number> idProdusColumn;
    @FXML private TableColumn<Produs, String> categorieProdusColumn;
    @FXML private TableColumn<Produs, String> numeProdusColumn;
    @FXML private TableColumn<Produs, Number> pretProdusColumn;

    // Controale pentru adăugarea unui produs
    @FXML private TextField produsIdField;
    @FXML private TextField produsCategorieField;
    @FXML private TextField produsNumeField;
    @FXML private TextField produsPretField;
    @FXML private Button addProdusButton;

    // Controale pentru ștergerea unui produs după ID
    @FXML private TextField deleteProductIdField;
    @FXML private Button deleteProductByIdButton;

    // Tabelul și coloanele pentru Comenzi
    @FXML private TableView<Comanda> comandaTable;
    @FXML private TableColumn<Comanda, Number> idComandaColumn;
    @FXML private TableColumn<Comanda, String> dataLivrareColumn;
    @FXML private TableColumn<Comanda, String> produseColumn;

    // Controale pentru adăugarea unei comenzi
    @FXML private TextField comandaIdField;
    @FXML private DatePicker comandaDatePicker;
    @FXML private ListView<Produs> comandaProduseListView;
    @FXML private Button addComandaButton;

    // Controale pentru ștergerea unei comenzi după ID
    @FXML private TextField deleteOrderIdField;
    @FXML private Button deleteOrderByIdButton;

    // Liste observabile pentru afișare
    private ObservableList<Produs> produseList = FXCollections.observableArrayList();
    private ObservableList<Comanda> comenziList = FXCollections.observableArrayList();

    // Repository-urile pentru persistenta datelor
    private ProdusDBRepository produsRepo;
    private ComandaDBRepository comandaRepo;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @FXML
    private void initialize() {
        // Configurarea coloanelor pentru tabelul de produse
        idProdusColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));
        categorieProdusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie()));
        numeProdusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNume()));
        pretProdusColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPret()));

        // Configurarea coloanelor pentru tabelul de comenzi
        idComandaColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));
        dataLivrareColumn.setCellValueFactory(cellData -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return new SimpleStringProperty(sdf.format(cellData.getValue().getDataLivrare()));
        });
        produseColumn.setCellValueFactory(cellData -> {
            List<Produs> produse = cellData.getValue().getProdusList();
            StringBuilder sb = new StringBuilder();
            for (Produs p : produse) {
                sb.append(p.getNume()).append(", ");
            }
            String prodStr = sb.toString();
            if (prodStr.endsWith(", ")) {
                prodStr = prodStr.substring(0, prodStr.length() - 2);
            }
            return new SimpleStringProperty(prodStr);
        });

        // Setăm modelul pentru ListView și permit selecția multiplă
        comandaProduseListView.setItems(produseList);
        comandaProduseListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Inițializare repository-uri și încărcare date din baza de date
        try {
            produsRepo = new ProdusDBRepository();
            comandaRepo = new ComandaDBRepository();

            produseList.clear();
            produseList.addAll(produsRepo.getAll());

            comenziList.clear();
            comenziList.addAll(comandaRepo.getAll());
        } catch (SQLException | RepositoryException e) {
            showAlert("Eroare DB", e.getMessage());
            e.printStackTrace();
        }

        produsTable.setItems(produseList);
        comandaTable.setItems(comenziList);
    }

    @FXML
    private void handleAddProduct() {
        try {
            int id = Integer.parseInt(produsIdField.getText().trim());
            // Verifică dacă există deja un produs cu acest ID
            boolean exists = produseList.stream().anyMatch(p -> p.getId() == id);
            if (exists) {
                showAlert("ID existent", "Un produs cu acest ID există deja.");
                return;
            }
            String categorie = produsCategorieField.getText().trim();
            String nume = produsNumeField.getText().trim();
            int pret = Integer.parseInt(produsPretField.getText().trim());

            Produs produsNou = new Produs(id, categorie, nume, pret);
            produsRepo.adauga(produsNou);
            produseList.add(produsNou);

            // Curăță câmpurile
            produsIdField.clear();
            produsCategorieField.clear();
            produsNumeField.clear();
            produsPretField.clear();
        } catch (NumberFormatException ex) {
            showAlert("Date invalide", "ID și Pret trebuie să fie numere întregi.");
        } catch (RepositoryException e) {
            showAlert("Eroare DB", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteProductById() {
        try {
            int id = Integer.parseInt(deleteProductIdField.getText().trim());
            // Caută produsul în lista UI
            Produs produsDeSters = produseList.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
            if (produsDeSters == null) {
                showAlert("Produs inexistent", "Nu există niciun produs cu ID-ul " + id);
                return;
            }
            // Șterge produsul din baza de date
            produsRepo.stergereId(id);
            // Șterge din lista UI
            produseList.remove(produsDeSters);
            deleteProductIdField.clear();
        } catch (NumberFormatException ex) {
            showAlert("Date invalide", "ID trebuie să fie număr întreg.");
        } catch (RuntimeException e) {
            showAlert("Eroare ștergere", e.getMessage());
        }
    }

    @FXML
    private void handleAddOrder() {
        try {
            int id = Integer.parseInt(comandaIdField.getText().trim());
            boolean exists = comenziList.stream().anyMatch(c -> c.getId() == id);
            if (exists) {
                showAlert("ID existent", "O comandă cu acest ID există deja.");
                return;
            }
            if (comandaDatePicker.getValue() == null) {
                showAlert("Date lipsă", "Selectați data livrării.");
                return;
            }
            Date dataLivrare = dateFormat.parse(comandaDatePicker.getValue().toString());

            ObservableList<Produs> produseSelectate = comandaProduseListView.getSelectionModel().getSelectedItems();
            if (produseSelectate.isEmpty()) {
                showAlert("Date lipsă", "Selectați cel puțin un produs pentru comandă.");
                return;
            }
            ArrayList<Produs> produseComanda = new ArrayList<>(produseSelectate);

            Comanda comandaNoua = new Comanda(id, produseComanda, dataLivrare);
            comandaRepo.adauga(comandaNoua);
            comenziList.add(comandaNoua);

            // Curăță câmpurile
            comandaIdField.clear();
            comandaDatePicker.setValue(null);
            comandaProduseListView.getSelectionModel().clearSelection();
        } catch (NumberFormatException | ParseException ex) {
            showAlert("Date invalide", "Asigurați-vă că ați introdus un ID valid și o dată corectă.");
        } catch (RepositoryException e) {
            showAlert("Eroare DB", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteOrderById() {
        try {
            int id = Integer.parseInt(deleteOrderIdField.getText().trim());
            Comanda comandaDeSters = comenziList.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
            if (comandaDeSters == null) {
                showAlert("Comandă inexistentă", "Nu există nicio comandă cu ID-ul " + id);
                return;
            }
            // Șterge comanda din baza de date
            comandaRepo.stergereId(id);
            // Șterge din lista UI
            comenziList.remove(comandaDeSters);
            deleteOrderIdField.clear();
        } catch (NumberFormatException ex) {
            showAlert("Date invalide", "ID trebuie să fie număr întreg.");
        } catch (RuntimeException e) {
            showAlert("Eroare ștergere", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

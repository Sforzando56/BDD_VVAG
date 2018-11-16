package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import metier.Categorie;
import metier.SalleVente;
import metier.Vente;
import persistence.Requester;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class FXMLDocumentController implements Initializable {

    @FXML
    private ListView<SalleVente> listViewSalles = new ListView<>();

    private ObservableList<SalleVente> salles;

    @FXML
    private TableView<Vente> ventesTables;

    @FXML
    private TableColumn<Vente, String> nomProduitColumn;

    @FXML
    private TableColumn<Vente, Float> prixColumn;

    @FXML
    private TableColumn<Vente, Date> dateColumn;

    private Stage ajoutVenteStage;

    private Requester requester;

    public void initialize(URL url, ResourceBundle rb) {
        requester = Requester.getInstance();

        salles = requester.getSallesVentes();

        listViewSalles.setItems(salles);
        listViewSalles.setCellFactory(param -> new ListCell<SalleVente>() {
            @Override
            protected void updateItem(SalleVente item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText("Salle " + item.getCategorie().getNom());
                }
            }
        });

        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prixEnCours"));
        nomProduitColumn.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("fin"));

        listViewSalles.getSelectionModel().selectedItemProperty().
                addListener(new ChangeListener<SalleVente>() {
                    @Override
                    public void changed(ObservableValue<? extends SalleVente> observableValue, SalleVente oldSalleVente, SalleVente newSalleVente) {
                        ventesTables.setItems(requester.getVentesBySalle(newSalleVente.getIdSalle()));
                    }
                });
    }

    @FXML
    private void onAjouterVente() {
        ajoutVenteStage = new Stage();
        ajoutVenteStage.setTitle("Ajouter une vente");
        FXMLLoader fxmloader = new FXMLLoader(getClass().getResource("/vue/AjoutVente.fxml"));
        Parent root;
        try {
            root = fxmloader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);
        ajoutVenteStage.setScene(scene);

        ajoutVenteStage.show();
    }


}

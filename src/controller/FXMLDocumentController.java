package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import metier.Categorie;
import metier.SalleVente;
import metier.Vente;

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

    public void initialize(URL url, ResourceBundle rb) {
        Categorie categorie = new Categorie("Mobilier", "Mobilier");
        Categorie categorie2 = new Categorie("Bijoux", "bijoux");
        SalleVente salle = new SalleVente(1, true, true, categorie);
        SalleVente salle2 = new SalleVente(1, true, true, categorie2);
        salles = FXCollections.observableArrayList(salle, salle2);
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
                        ventesTables.setItems(newSalleVente.getVentes());
                    }
                });
    }


}

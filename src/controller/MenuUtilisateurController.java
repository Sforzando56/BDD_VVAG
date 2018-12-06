package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import metier.SalleVente;
import metier.Vente;
import persistence.Requester;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.ResourceBundle;

public class MenuUtilisateurController implements Initializable {

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


    private Requester requester;

    private SalleVente salleSelec;

    public void initialize(URL url, ResourceBundle rb) {
        requester = Requester.getInstance();

        listViewSalles.getSelectionModel().selectedItemProperty().
                addListener(new ChangeListener<SalleVente>() {
                    @Override
                    public void changed(ObservableValue<? extends SalleVente> observableValue, SalleVente oldSalleVente, SalleVente newSalleVente) {
                        ventesTables.setItems(requester.getVentesBySalle(newSalleVente.getIdSalle()));
                        salleSelec = newSalleVente;
                    }
                });
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prixDepart"));
        nomProduitColumn.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("fin"));
        listViewSalles.setCellFactory(param -> new ListCell<SalleVente>() {
            @Override
            protected void updateItem(SalleVente item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });
        salles = requester.getSallesVentes();
        listViewSalles.setItems(salles);
    }

    public void updateTableView(SalleVente salleVente){
        ventesTables.setItems(Requester.getInstance().getVentesBySalle(salleVente.getIdSalle()));
    }

    @FXML
    private void onAjouterVente() {
        Stage ajoutVenteStage = new Stage();
        ajoutVenteStage.setTitle("Ajouter une vente");
        FXMLLoader fxmloader = new FXMLLoader(getClass().getResource("/vue/AjoutProduit.fxml"));
        AjoutProduitController controller = new AjoutProduitController( ajoutVenteStage);
        fxmloader.setController(controller);
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

    @FXML
    public void onVenteClicked(MouseEvent e){
        if(e.getClickCount() != 2){
            return;
        }
        Vente vente = ventesTables.getSelectionModel().getSelectedItem();
        vente = requester.getVenteAvecProduitConnu(vente.getIdVente(), vente.getProduit());
        Timestamp now = Timestamp.from(Instant.now());
        if (now.compareTo(vente.getFin()) > 0){
            afficheVenteFinie(vente);
        }
        else {
            afficheVenteEnCours(vente);
        }
    }

    private void afficheVenteEnCours(Vente vente){
        Stage enchereVenteStage = new Stage();
        enchereVenteStage.setTitle("Enchérir sur vente");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/EnchereVente.fxml"));
        EnchereVenteController enchereVenteController = new EnchereVenteController(vente, enchereVenteStage, salleSelec, this);
        loader.setController(enchereVenteController);
        Parent root;
        try {
            root = loader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);
        enchereVenteStage.setScene(scene);

        enchereVenteStage.show();
    }

    private void afficheVenteFinie(Vente vente){
        Stage venteFinieStage = new Stage();
        venteFinieStage.setTitle("Bilan vente");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/VenteFinie.fxml"));
        VenteFinieController venteFinieController = new VenteFinieController(vente, salleSelec);
        loader.setController(venteFinieController);
        Parent root;
        try {
            root = loader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);
        venteFinieStage.setScene(scene);

        venteFinieStage.show();
    }
}

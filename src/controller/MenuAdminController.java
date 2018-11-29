package controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import metier.Produit;
import persistence.Requester;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuAdminController implements Initializable {

    @FXML
    ListView<Produit> listeProduitsView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listeProduitsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listeProduitsView.setItems(Requester.getInstance().getProduitsNonEnVente());
        listeProduitsView.setCellFactory(param -> new ListCell<Produit>() {
            @Override
            protected void updateItem(Produit item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getNom() + " " + item.getPrixRevient());
                }
            }
        });
        MenuItem creerSalleItem = new MenuItem("Créer salle");
        creerSalleItem.setOnAction(this::creerSalle);
        ContextMenu contextMenu = new ContextMenu(creerSalleItem);
        listeProduitsView.setContextMenu(contextMenu);
    }

    private void creerSalle(Event event){
        Stage ajoutSalleStage = new Stage();
        ajoutSalleStage.setTitle("Créer Salle");
        AjoutSalleController controller = new AjoutSalleController(listeProduitsView.getSelectionModel().getSelectedItems(), ajoutSalleStage, this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/AjoutSalle.fxml"));
        loader.setController(controller);
        Parent root;
        try {
            root = loader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);
        ajoutSalleStage.setScene(scene);
        ajoutSalleStage.show();
    }

    public void updateList(){
        listeProduitsView.setItems(Requester.getInstance().getProduitsNonEnVente());
    }

}

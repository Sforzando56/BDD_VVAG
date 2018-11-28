package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import metier.*;
import org.apache.commons.lang3.StringUtils;
import persistence.Requester;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.ResourceBundle;

import static utils.AlertCreator.showAlert;

public class AjoutProduitController implements Initializable {

    @FXML
    AnchorPane anchorPane;

    @FXML
    TextField emailTextfield;

    @FXML
    TextField nomProduitTextfield;

    @FXML
    TextField quantiteTextfield;

    @FXML
    TextField prixRevientTextfield;

    @FXML
    TextField addressTextfield;

    @FXML
    TextField nomTextfield;

    @FXML
    TextField prenomTextfield;

    private Requester requester;


    private Stage ajoutVenteStage;


    AjoutProduitController(Stage ajoutVenteStage) {
        this.ajoutVenteStage = ajoutVenteStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.requester = Requester.getInstance();
    }


    private boolean validateFormulaire() {
        boolean error = false;
        if (nomTextfield.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez votre nom");
            error = true;
        }
        if (prenomTextfield.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez votre prénom");
            error = true;
        }
        if (addressTextfield.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez votre adresse");
            error = true;
        }

        if (nomProduitTextfield.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez le nom du produit");
            error = true;
        }
        if (quantiteTextfield.getText().isEmpty() || !StringUtils.isNumeric(quantiteTextfield.getText())) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez le stock du produit à vendre");
            error = true;
        }
        if (prixRevientTextfield.getText().isEmpty() || !StringUtils.isNumeric(prixRevientTextfield.getText())) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez le prix de revient du produit");
            error = true;
        }
        return error;
    }

    @FXML
    private void onValiderAjout() {
        boolean error = validateFormulaire();
        if (!error) {
            Utilisateur utilisateur = new Utilisateur(emailTextfield.getText(), nomTextfield.getText(), prenomTextfield.getText(), addressTextfield.getText());
            requester.upsertUtilisateur(utilisateur);
            Produit produit = new Produit(0, nomProduitTextfield.getText(), Float.parseFloat(prixRevientTextfield.getText()), Integer.parseInt(quantiteTextfield.getText()), null);
            requester.insertProduit(produit, utilisateur.getEmail());
            ajoutVenteStage.close();
            showAlert(Alert.AlertType.CONFIRMATION, anchorPane.getScene().getWindow(), "Confirmation", "Produit ajoutée");
        }
    }
}

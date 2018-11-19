package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import metier.Produit;
import metier.Utilisateur;
import metier.Vente;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class AjoutVenteController implements Initializable {

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
    TextField codePostalTextfield;

    @FXML
    TextField addressTextfield;

    @FXML
    TextField categorieTextfield;

    @FXML
    TextField nomTextfield;

    @FXML
    TextField prenomTextfield;

    @FXML
    TextField prixDepartTextfield;

    @FXML
    DatePicker datePicker;

    @FXML
    RadioButton montante;

    @FXML
    RadioButton revocable;

    @FXML
    RadioButton dureeLimitee;

    @FXML
    RadioButton encheresLibres;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void onValiderAjout() {
        if (nomTextfield.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez votre nom");
        }
        if (prenomTextfield.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez votre prénom");
        }
        if (addressTextfield.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez votre adresse");
        }
        if (codePostalTextfield.getText().isEmpty() || !StringUtils.isNumeric(codePostalTextfield.getText())) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez votre code postal, qui est numérique");
        }
        if (categorieTextfield.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez la catégorie");
        }
        if (nomProduitTextfield.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez le nom du produit");
        }
        if (quantiteTextfield.getText().isEmpty() || !StringUtils.isNumeric(quantiteTextfield.getText())) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez le stock du produit à vendre");
        }
        if (prixRevientTextfield.getText().isEmpty() || !StringUtils.isNumeric(prixRevientTextfield.getText())) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez le prix de revient du produit");
        }

        if (prixDepartTextfield.getText().isEmpty() || !StringUtils.isNumeric(prixDepartTextfield.getText())) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez le prix de départ du produit");
        }

        Utilisateur utilisateur = new Utilisateur(emailTextfield.getText(), nomTextfield.getText(), prenomTextfield.getText(), addressTextfield.getText(), Integer.parseInt(codePostalTextfield.getText()));
        Produit produit = new Produit(0, nomProduitTextfield.getText(), Float.parseFloat(prixRevientTextfield.getText()), Integer.parseInt(quantiteTextfield.getText()));
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}

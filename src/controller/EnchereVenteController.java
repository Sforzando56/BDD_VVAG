package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import metier.Enchere;
import metier.Utilisateur;
import metier.Vente;
import org.apache.commons.lang3.StringUtils;
import persistence.Requester;

import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ResourceBundle;

import static utils.AlertCreator.showAlert;

public class EnchereVenteController implements Initializable {

    @FXML
    AnchorPane anchorPane;

    @FXML
    Label prixEnCoursLabel;

    @FXML
    Label nomProduitLabel;

    @FXML
    Label fullnameVendeurLabel;

    @FXML
    Label finLabel;

    @FXML
    Label stockLabel;

    @FXML
    TextField emailTextfield;

    @FXML
    TextField prenomTextfield;

    @FXML
    TextField adresseTextField;

    @FXML
    TextField nomTextfield;

    @FXML
    TextField quantProposeeTextfield;

    @FXML
    TextField prixAchatTextfield;

    private Vente vente;

    private Enchere derniereEnchere;

    private Stage enchereVenteStage;

    EnchereVenteController(Vente vente, Stage stage, Enchere enchere) {
        this.vente = vente;
        this.derniereEnchere = enchere;
        enchereVenteStage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Utilisateur utilisateur = Requester.getInstance().getUtilisateurFromVente(vente.getIdVente());
        fullnameVendeurLabel.setText(utilisateur.getPrenom() + " " + utilisateur.getNom());
        nomProduitLabel.setText(vente.getProduit().getNom());
        prixEnCoursLabel.setText(String.valueOf(derniereEnchere.getPrixAchat()));
        stockLabel.setText(String.valueOf(vente.getProduit().getStock()));
        finLabel.setText(String.valueOf(vente.getFin()));
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
        if (adresseTextField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez votre adresse");
            error = true;
        }
        if (emailTextfield.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez la catégorie");
            error = true;
        }
        if (prixAchatTextfield.getText().isEmpty() || !StringUtils.isNumeric(prixAchatTextfield.getText())) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez le stock du produit à vendre");
            error = true;
        }
        if (quantProposeeTextfield.getText().isEmpty() || !StringUtils.isNumeric(quantProposeeTextfield.getText())) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez le prix de revient du produit");
            error = true;
        }
        if (Float.parseFloat(prixAchatTextfield.getText()) <= derniereEnchere.getPrixAchat()){
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Le prix proposé doit dépassé le prix en cours");
            error = true;
        }
        return error;
    }

    @FXML
    public void onEncherit() {
        boolean error = validateFormulaire();
        if (error) {
            return;
        }
        Utilisateur utilisateur = new Utilisateur(emailTextfield.getText(), nomTextfield.getText(), prenomTextfield.getText(), adresseTextField.getText());
        Requester.getInstance().upsertUtilisateur(utilisateur);
        Enchere enchere = new Enchere(
                0,
                vente.getIdVente(),
                Float.parseFloat(prixAchatTextfield.getText()),
                Timestamp.from(Instant.now()),
                Integer.parseInt(quantProposeeTextfield.getText()),
                utilisateur.getEmail()
        );
        Requester.getInstance().insertEnchere(enchere);
        enchereVenteStage.close();
        showAlert(Alert.AlertType.CONFIRMATION, anchorPane.getScene().getWindow(), "Confirmation", "Enchere Confirmee");
    }
}

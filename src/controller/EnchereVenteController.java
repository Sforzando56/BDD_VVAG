package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import metier.Enchere;
import metier.SalleVente;
import metier.Utilisateur;
import metier.Vente;
import org.apache.commons.lang3.StringUtils;
import persistence.Requester;
import utils.AlertCreator;

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

    private SalleVente salleVente;

    private Vente vente;

    private Enchere derniereEnchere;

    private Stage enchereVenteStage;

    EnchereVenteController(Vente vente, Stage stage, Enchere enchere, SalleVente salleVente) {
        this.vente = vente;
        this.derniereEnchere = enchere;
        this.salleVente = salleVente;
        enchereVenteStage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Utilisateur utilisateur = Requester.getInstance().getUtilisateurFromProduit(vente.getProduit());
        fullnameVendeurLabel.setText(utilisateur.getPrenom() + " " + utilisateur.getNom());
        nomProduitLabel.setText(vente.getProduit().getNom());
        if (derniereEnchere == null) {
            prixEnCoursLabel.setText(String.valueOf(vente.getPrixDepart()));
        } else {
            prixEnCoursLabel.setText(String.valueOf(derniereEnchere.getPrixAchat()));
        }
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
        float prixAComp;
        if (derniereEnchere != null) {
            prixAComp = derniereEnchere.getPrixAchat();
        } else {
            prixAComp = vente.getPrixDepart();
        }
        if (Float.parseFloat(prixAchatTextfield.getText()) <= prixAComp) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Le prix proposé doit dépassé le prix en cours");
            error = true;
        }

        if ((Integer.parseInt(quantProposeeTextfield.getText()) > vente.getProduit().getStock()) || (Integer.parseInt(quantProposeeTextfield.getText()) <= 0)) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "La quant ne peut dépasser le stock et > 0");
            error = true;
        }
        return error;
    }

    @FXML
    public void onEncherit() {
        Timestamp now = Timestamp.from(Instant.now());
        if (now.compareTo(vente.getFin()) > 0) {
            AlertCreator.showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur", "La vente est désormais terminée");
            enchereVenteStage.close();
            return;
        }

        boolean error = validateFormulaire();
        if (error) {
            return;
        }
        boolean peutEncherir = true;
        if (!salleVente.isEnchereLibre()) {
            ObservableList<Enchere> encheres = Requester.getInstance().getEncheresByVente(vente.getIdVente());
            for (Enchere enchere : encheres) {
                if (enchere.getEmailUtilisateur().equals(emailTextfield.getText())) {
                    peutEncherir = false;
                }
            }
        }

        if (!peutEncherir) {
            AlertCreator.showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur", "Vous ne pouvez plus enchérir sur cet objet");
            enchereVenteStage.close();
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
        if (!salleVente.isDureeLim()) {
            Instant instant = Instant.now();
            instant = instant.plusSeconds(600); //Date de fin est maintenant plus 10mn
            vente.setFin(Timestamp.from(instant));
            Requester.getInstance().updateDateVente(vente);
        }
        Requester.getInstance().insertEnchere(enchere);
        enchereVenteStage.close();
        showAlert(Alert.AlertType.CONFIRMATION, anchorPane.getScene().getWindow(), "Confirmation", "Enchere Confirmee");
        enchereVenteStage.close();
    }
}

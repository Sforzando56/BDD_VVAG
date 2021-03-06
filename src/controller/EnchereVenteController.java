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

    private MenuUtilisateurController menuUtilisateurController;

    EnchereVenteController(Vente vente, Stage stage, SalleVente salleVente, MenuUtilisateurController menuUtilisateurController) {
        this.vente = vente;
        this.salleVente = salleVente;
        enchereVenteStage = stage;
        this.menuUtilisateurController = menuUtilisateurController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.derniereEnchere = Requester.getInstance().getDerniereEnchere(vente.getIdVente());
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
        if (salleVente.isMontante() && Float.parseFloat(prixAchatTextfield.getText()) <= prixAComp) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Le prix proposé doit dépasser le prix en cours");
            error = true;
        }
        if(!salleVente.isMontante() && Float.parseFloat(prixAchatTextfield.getText()) > prixAComp) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Le prix proposé doit être inférieur où égal au prix en cours");
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
        boolean error = validateFormulaire();
        if (error) {
            return;
        }
        boolean peutEncherir = true;
        if (!salleVente.isEnchereLibre()) {
            ObservableList<Enchere> encheres = Requester.getInstance().getEncheresOrdreByVente(vente.getIdVente());
            for (Enchere enchere : encheres) {
                if (enchere.getEmailUtilisateur().equals(emailTextfield.getText())) {
                    peutEncherir = false;
                }
            }
        }

        if (!peutEncherir) {
            enchereVenteStage.close();
            AlertCreator.showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur", "Vous ne pouvez plus enchérir sur cet objet");
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

        if (!Requester.getInstance().insertEnchere(enchere, vente.getProduit(), salleVente.isDureeLim())) {
            enchereVenteStage.close();
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur date", "Date de fin dépassé");
        } else {
            enchereVenteStage.close();
            menuUtilisateurController.updateTableView(salleVente);
            showAlert(Alert.AlertType.CONFIRMATION, anchorPane.getScene().getWindow(), "Confirmation", "Enchere Confirmee");
        }
    }
}

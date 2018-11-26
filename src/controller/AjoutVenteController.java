package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import metier.*;
import org.apache.commons.lang3.StringUtils;
import persistence.Requester;

import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static utils.AlertCreator.showAlert;

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

    private Requester requester;

    private ObservableList<SalleVente> salleVentes;

    private Stage ajoutVenteStage;

    private FXMLDocumentController fxmlDocumentController;

    AjoutVenteController(ObservableList<SalleVente> salles, Stage ajoutVenteStage, FXMLDocumentController fxmlDocumentController) {
        this.salleVentes = salles;
        this.ajoutVenteStage = ajoutVenteStage;
        this.fxmlDocumentController = fxmlDocumentController;
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
        if (categorieTextfield.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez la catégorie");
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

        if (prixDepartTextfield.getText().isEmpty() || !StringUtils.isNumeric(prixDepartTextfield.getText())) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez le prix de départ du produit");
            error = true;
        }
        if (dureeLimitee.isSelected() && datePicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez la date de fin puisque duree limitée est sélectionné");
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
            String[] categories = categorieTextfield.getText().split("-");
            List<Categorie> categorieList = new ArrayList<>();
            for (String categorie : categories) {
                categorieList.add(new Categorie(categorie, "des"));
            }
            boolean salleTrouvee = false;

            Produit produit = new Produit(0, nomProduitTextfield.getText(), Float.parseFloat(prixRevientTextfield.getText()), Integer.parseInt(quantiteTextfield.getText()), categorieList);
            Timestamp fin;
            if (dureeLimitee.isSelected()) {
                fin = Timestamp.valueOf(datePicker.getValue().atTime(0, 0));
            } else {
                Instant instant = Instant.now();
                instant = instant.plusSeconds(600); //Date de fin est maintenant plus 10mn
                fin = Timestamp.from(instant);
            }
            Vente vente = new Vente(0, Float.parseFloat(prixDepartTextfield.getText()), fin, produit, 0);
            requester.insertProduitAndCategories(produit, utilisateur.getEmail());
            for (SalleVente salleVente : this.salleVentes) {
                if (Arrays.asList(categories).contains(salleVente.getCategorie().getNom())
                        && salleVente.isMontante() == montante.isSelected()
                        && salleVente.isDureeLim() == dureeLimitee.isSelected()
                        && salleVente.isRevocable() == revocable.isSelected()
                        && salleVente.isEnchereLibre() == encheresLibres.isSelected()
                ) {
                    vente.setIdSalle(salleVente.getIdSalle());
                    salleTrouvee = true;

                }
            }

            if (!salleTrouvee) {
                try {
                    int idSalle = this.requester.insertSalle(
                            new SalleVente(
                                    0,
                                    montante.isSelected(),
                                    revocable.isSelected(),
                                    dureeLimitee.isSelected(),
                                    encheresLibres.isSelected(),
                                    produit.getCategories().get(0)
                            )
                    );
                    vente.setIdSalle(idSalle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            requester.insertVente(vente);
            ajoutVenteStage.close();
            showAlert(Alert.AlertType.CONFIRMATION, anchorPane.getScene().getWindow(), "Confirmation", "Vente ajoutée");
        }
        this.fxmlDocumentController.update();
    }
}

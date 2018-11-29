package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import metier.Categorie;
import metier.Produit;
import metier.SalleVente;
import metier.Vente;
import org.apache.commons.lang3.StringUtils;
import persistence.Requester;

import java.sql.Timestamp;
import java.time.Instant;

import static utils.AlertCreator.showAlert;

public class AjoutSalleController {

    @FXML
    AnchorPane anchorPane;

    @FXML
    DatePicker datePicker;

    @FXML
    RadioButton descendante;

    @FXML
    RadioButton revocable;

    @FXML
    RadioButton dureeLimitee;

    @FXML
    RadioButton encheresNonLibres;

    @FXML
    TextField prixDepartTextfield;

    @FXML
    TextField categorieTextfield;

    @FXML
    TextArea descriptionCategorieTextArea;


    private ObservableList<Produit> produits;

    private Stage ajoutSalleStage;

    private MenuAdminController menuAdminController;

    public AjoutSalleController(ObservableList<Produit> produits, Stage ajoutSalleStage, MenuAdminController controller) {
        this.produits = produits;
        this.ajoutSalleStage = ajoutSalleStage;
        menuAdminController = controller;
    }

    private boolean validateFormulaire() {
        boolean error = false;
        if (categorieTextfield.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez la catégorie");
            error = true;
        }

        if (descriptionCategorieTextArea.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez la description");
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
    public void onAjoutSalle() {
        if (validateFormulaire()) {
            return;
        }

        Categorie categorie = new Categorie(categorieTextfield.getText(), descriptionCategorieTextArea.getText());
        Requester.getInstance().upsertCategorie(categorie);

        SalleVente salleVente = new SalleVente(
                0,
                !descendante.isSelected(),
                revocable.isSelected(),
                dureeLimitee.isSelected(),
                !encheresNonLibres.isSelected(),
                categorie
        );

        int idSalle;
        try {
            idSalle = Requester.getInstance().insertSalle(salleVente);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Timestamp fin;
        if (dureeLimitee.isSelected()) {
            fin = Timestamp.valueOf(datePicker.getValue().atTime(0, 0));
        } else {
            Instant instant = Instant.now();
            instant = instant.plusSeconds(600); //Date de fin est maintenant plus 10mn
            fin = Timestamp.from(instant);
        }
        for (Produit produit : produits) {
            Requester.getInstance().insertCategorieProduit(categorie, produit);
            Vente vente = new Vente(
                    0,
                    Float.parseFloat(prixDepartTextfield.getText()),
                    fin,
                    produit,
                    idSalle
            );
            Requester.getInstance().insertVente(vente);
        }
        showAlert(Alert.AlertType.CONFIRMATION, anchorPane.getScene().getWindow(), "Confirmation", "Salle ajoutée");
        menuAdminController.updateList();
        ajoutSalleStage.close();
    }
}

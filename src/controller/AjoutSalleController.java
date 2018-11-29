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
    TextField heureTextfield;

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
        if (dureeLimitee.isSelected() && heureTextfield.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Entrez heure de fin puisque duree limitée est sélectionné");
            error = true;
        }
        return error;
    }

    private Integer[] parseHeureFin(String heureFin){
        String tabh[] = heureFin.split(":");
        if (tabh.length != 2){
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Mauvais format heure fin");
            return null;
        }
        if(!StringUtils.isNumeric(tabh[0]) || !StringUtils.isNumeric(tabh[1]) || tabh[0].length() != 2 || tabh[1].length() != 2){
            showAlert(Alert.AlertType.ERROR, anchorPane.getScene().getWindow(), "Erreur formulaire", "Mauvais format heure fin");
            return null;
        }
        Integer tabfin[] = {Integer.parseInt(tabh[0]), Integer.parseInt(tabh[1])};
        return tabfin;
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

        Timestamp fin;
        if (dureeLimitee.isSelected()) {
            Integer tabh[] = parseHeureFin(heureTextfield.getText());
            if (tabh == null){
                return;
            }
            fin = Timestamp.valueOf(datePicker.getValue().atTime(tabh[0], tabh[1]));
        } else {
            Instant instant = Instant.now();
            instant = instant.plusSeconds(600); //Date de fin est maintenant plus 10mn
            fin = Timestamp.from(instant);
        }
        int idSalle;
        try {
            idSalle = Requester.getInstance().insertSalle(salleVente);
        } catch (Exception e) {
            e.printStackTrace();
            return;
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
        menuAdminController.updateList();
        ajoutSalleStage.close();
        showAlert(Alert.AlertType.CONFIRMATION, anchorPane.getScene().getWindow(), "Confirmation", "Salle ajoutée");
    }
}

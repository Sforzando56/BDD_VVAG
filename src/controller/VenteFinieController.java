package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import metier.Enchere;
import metier.SalleVente;
import metier.Utilisateur;
import metier.Vente;
import persistence.Requester;
import utils.AlertCreator;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class VenteFinieController implements Initializable {

    @FXML
    Label nomProduitLabel;

    @FXML
    private TableView<Enchere> gagnantsTableView;

    @FXML
    private TableColumn<Enchere, String> emailGagnantColumn;

    @FXML
    private TableColumn<Enchere, Float> prixProposeColumn;

    @FXML
    private TableColumn<Enchere, Integer> quantProposeeColumn;

    @FXML
    private TableColumn<Enchere, Integer> prixTotalColumn;

    private Vente vente;

    private SalleVente salleVente;


    VenteFinieController(Vente vente, SalleVente salleVente) {
        this.vente = vente;
        this.salleVente = salleVente;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailGagnantColumn.setCellValueFactory(new PropertyValueFactory<>("emailUtilisateur"));
        prixProposeColumn.setCellValueFactory(new PropertyValueFactory<>("prixAchat"));
        quantProposeeColumn.setCellValueFactory(new PropertyValueFactory<>("quantProposee"));
        prixTotalColumn.setCellValueFactory(new PropertyValueFactory<>("prixTotal"));
        afficher();
    }

    private void afficher() {
        nomProduitLabel.setText(vente.getProduit().getNom());
        ObservableList<Enchere> gagnants = FXCollections.observableArrayList();
        ObservableList<Enchere> encheres = Requester.getInstance().getEncheresByVente(vente.getIdVente());
        int stockPropose = 0;
        boolean dejaPresent;
        for (Enchere enchere : encheres) {
            dejaPresent = false;
            if (enchere.getQuantProposee() + stockPropose > vente.getProduit().getStock()) {
                int quantPossible = vente.getProduit().getStock() - stockPropose;
                enchere.setQuantProposee(quantPossible);
                gagnants.add(enchere);
                break;
            }
            for (Enchere enchere1 : gagnants) {
                if (enchere.getEmailUtilisateur().equals(enchere1.getEmailUtilisateur())) {
                    dejaPresent = true;
                    break;
                }
            }
            if (!dejaPresent) {
                gagnants.add(enchere);
                stockPropose += enchere.getQuantProposee();
            }
        }
    }
}

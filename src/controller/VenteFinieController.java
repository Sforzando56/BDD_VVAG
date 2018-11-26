package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import metier.Enchere;
import metier.Utilisateur;
import metier.Vente;
import persistence.Requester;

import java.net.URL;
import java.util.ResourceBundle;

public class VenteFinieController implements Initializable {

    @FXML
    Label prixFinalLabel;

    @FXML
    Label fullnameGagnantLabel;

    @FXML
    Label nomProduitLabel;

    private Vente vente;

    private Enchere derniereEnchere;


    VenteFinieController(Vente vente, Enchere enchere) {
        this.vente = vente;
        this.derniereEnchere = enchere;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        afficher();
    }

    private void afficher() {
        nomProduitLabel.setText(vente.getProduit().getNom());
        prixFinalLabel.setText(String.valueOf(derniereEnchere.getPrixAchat()));
        Utilisateur utilisateur = Requester.getInstance().getUtilisateur(derniereEnchere.getEmailUtilisateur());
        fullnameGagnantLabel.setText(utilisateur.getPrenom() + " " + utilisateur.getNom());
    }
}

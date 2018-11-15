package metier;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.util.List;

public class Utilisateur {

    private StringProperty email;

    private StringProperty nom;

    private StringProperty prenom;

    private StringProperty adresse;

    private IntegerProperty codePostal;

    private ListProperty<Produit> produits;

    private ListProperty<Enchere> encheres;

    public Utilisateur(String email, String nom, String prenom, String adresse, int codePostal, ObservableList<Produit> produits, ObservableList<Enchere> encheres) {
        this.email = new SimpleStringProperty(email);
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.adresse = new SimpleStringProperty(adresse);
        this.codePostal = new SimpleIntegerProperty(codePostal);
        this.produits = new SimpleListProperty<Produit>(produits);
        this.encheres = new SimpleListProperty<Enchere>(encheres);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getNom() {
        return nom.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public String getPrenom() {
        return prenom.get();
    }

    public StringProperty prenomProperty() {
        return prenom;
    }

    public String getAdresse() {
        return adresse.get();
    }

    public StringProperty adresseProperty() {
        return adresse;
    }

    public int getCodePostal() {
        return codePostal.get();
    }

    public IntegerProperty codePostalProperty() {
        return codePostal;
    }

    public ObservableList<Produit> getProduits() {
        return produits.get();
    }

    public ListProperty<Produit> produitsProperty() {
        return produits;
    }

    public ObservableList<Enchere> getEncheres() {
        return encheres.get();
    }

    public ListProperty<Enchere> encheresProperty() {
        return encheres;
    }
}

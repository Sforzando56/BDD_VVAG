package metier;

import javafx.beans.property.*;


public class Utilisateur {

    private StringProperty email;

    private StringProperty nom;

    private StringProperty prenom;

    private StringProperty adresse;

    public Utilisateur(String email, String nom, String prenom, String adresse) {
        this.email = new SimpleStringProperty(email);
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.adresse = new SimpleStringProperty(adresse);
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
}

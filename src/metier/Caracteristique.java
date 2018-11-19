package metier;

import javafx.beans.property.StringProperty;

public class Caracteristique {

    private int idCaracteristique;

    private StringProperty nom;

    private StringProperty description;

    public Caracteristique(int idCaracteristique, StringProperty nom, StringProperty description) {
        this.idCaracteristique = idCaracteristique;
        this.nom = nom;
        this.description = description;
    }

    public int getIdCaracteristique() {
        return idCaracteristique;
    }

    public String getNom() {
        return nom.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}

package metier;

import javafx.beans.property.StringProperty;

public class Caracteristique {

    private int idCaracteristique;

    private StringProperty nom;

    private StringProperty description;

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

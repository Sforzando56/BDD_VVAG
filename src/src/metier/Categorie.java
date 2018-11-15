package metier;

import javafx.beans.property.StringProperty;

public class Categorie {

    private StringProperty nom;

    private StringProperty description;

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

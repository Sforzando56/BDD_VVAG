package metier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import persistence.BddConnection;

public class Categorie {
    private StringProperty nom;
    private StringProperty description;

    public Categorie(String nom, String description) {
        this.nom = new SimpleStringProperty(nom);
        this.description = new SimpleStringProperty(description);
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

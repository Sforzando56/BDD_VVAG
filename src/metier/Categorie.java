package metier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Categorie {
    private StringProperty nom;
    private StringProperty description;
    
	public Categorie(String nom, String description) {
		this.nom = new SimpleStringProperty(nom);
		this.description = new SimpleStringProperty(description);
	}
	
	
	public static Categorie create(String nom, String description) throws SQLException {
		try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("INSERT INTO Categorie "
				+ "(nom, description) VALUES "
				+ "(?, ?)")){
			stmt.setString(1, nom);
			stmt.setString(2, description);

			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return new Categorie(nom, description);
			}
			else {
				throw new IllegalArgumentException("Problème à l'insertion de la catégorie " + nom);
			}
		}
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

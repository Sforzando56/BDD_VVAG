import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Categorie {
	private String nom;
	private String description;

	public Categorie(String nom, String description) {
		super();
		this.nom = nom;
		this.description = description;
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
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

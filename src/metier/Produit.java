package metier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import persistence.BddConnection;

public class Produit {
    private IntegerProperty idProduit;
    private StringProperty nom;
    private FloatProperty prixRevient;
    private IntegerProperty stock;
    private StringProperty emailUtilisateur;
    private StringProperty nomCategorie;

	public Produit(int idProduit, String nom, double prixRevient, int stock,
			String emailUtilisateur, String nomCategorie) {
		this.idProduit = new SimpleIntegerProperty(idProduit);
		this.nom = new SimpleStringProperty(nom);
		this.prixRevient = new SimpleFloatProperty((float) prixRevient);
		this.stock = new SimpleIntegerProperty(stock);
		this.emailUtilisateur = new SimpleStringProperty(emailUtilisateur);
		this.nomCategorie = new SimpleStringProperty(nomCategorie);
	}
	
	public static Produit create(int id_produit, String nom, double prix_revient, int stock, String email_utilisateur, String nom_categorie) throws SQLException {
		try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("INSERT INTO Produit"
				+ "(id_produit, nom, prix_revient, stock, email_utilisateur, nom_categorie) VALUES "
				+ "(?, ?, ?, ?, ?, ?)")){
			stmt.setInt(1, id_produit);
			stmt.setString(2, nom);
			stmt.setDouble(3, prix_revient);
			stmt.setInt(4, stock);
			stmt.setString(5, email_utilisateur);
			stmt.setString(6, nom_categorie);

			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return new Produit(id_produit, nom, prix_revient, stock, email_utilisateur, nom_categorie);
			}
			else {
				throw new IllegalArgumentException("Problème à l'insertion de la catégorie " + nom);
			}
		}
	}

	public static Produit load(int id_produit) throws SQLException {
		try (Statement stmt = BddConnection.getConnection().createStatement()) {
			ResultSet rs = stmt.executeQuery("SELECT * FROM Produit WHERE id_produit = '"+ id_produit +"'");
			if (rs.next()) {
				double prix_revient = rs.getDouble("prix_revient");
				String nom = rs.getString("nom");
				int stock = rs.getInt("stock");
				String email_utilisateur = rs.getString("email_utilisateur");
				String nom_categorie = rs.getString("nom_categorie");

				return new Produit(id_produit, nom, prix_revient, stock, email_utilisateur, nom_categorie);
			} else {
				throw new IllegalArgumentException("Le produit "+id_produit+" n'existe pas");
			}
		}
	}


    public IntegerProperty getIdProduit() {
        return idProduit;
    }

    public String getNom() {
        return nom.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public float getPrixRevient() {
        return prixRevient.get();
    }

    public FloatProperty prixRevientProperty() {
        return prixRevient;
    }

    public int getStock() {
        return stock.get();
    }

    public IntegerProperty stockProperty() {
        return stock;
    }
}

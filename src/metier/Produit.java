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

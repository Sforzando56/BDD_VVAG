package metier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.StringProperty;
import persistence.BddConnection;

public class Vente {
    private int idVente;
    private FloatProperty prixDepart;
    private FloatProperty prixEnCours;
    private Timestamp fin;
    private Produit produit;

    

    public Vente(int idVente, double prixDepart, Timestamp fin, Produit produit) {
		this.idVente = idVente;
		this.prixDepart = new SimpleFloatProperty((float) prixDepart);
		this.fin = fin;
		this.produit = produit;
	}

    public Vente(int idVente, float prixDepart, Timestamp fin, Produit produit) {
        this.idVente = idVente;
        this.prixDepart = new SimpleFloatProperty(prixDepart);
        this.fin = fin;
        this.produit = produit;
    }

    public int getIdVente() {
        return idVente;
    }

    public float getPrixDepart() {
        return prixDepart.get();
    }

    public FloatProperty prixDepartProperty() {
        return prixDepart;
    }

    public Timestamp getFin() {
        return fin;
    }

    public Produit getProduit() {
        return produit;
    }

    public StringProperty nomProduitProperty() {
        return getProduit().nomProperty();
    }

    public FloatProperty getPrixEnCours() {
        return prixEnCours;
    }


}

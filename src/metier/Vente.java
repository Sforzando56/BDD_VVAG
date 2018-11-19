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


	public static ArrayList<Vente> seek(int idSalle) throws SQLException{
		ArrayList<Vente> ventes = new ArrayList<Vente>();
		try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("SELECT * FROM Vente WHERE id_salle = ?")) {
			stmt.setInt(1, idSalle);
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id_vente = rs.getInt("id_vente");
				double prix_depart = rs.getInt("prix_dep");
				Timestamp fin = rs.getTimestamp("date_fin");
				int id_produit = rs.getInt("id_produit");
				int id_salle = rs.getInt("id_salle");
				ventes.add(new Vente(id_vente, prix_depart, fin, Produit.load(id_produit)));
			}

		}
		return ventes;
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

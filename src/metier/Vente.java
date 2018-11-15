package metier;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;

import java.sql.Timestamp;

public class Vente {

    private int idVente;

    private FloatProperty prixDepart;

    private Timestamp fin;

    private ListProperty<Enchere> encheres;

    private Produit produit;

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

    public ObservableList<Enchere> getEncheres() {
        return encheres.get();
    }

    public ListProperty<Enchere> encheresProperty() {
        return encheres;
    }

    public Produit getProduit() {
        return produit;
    }
}

package metier;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.util.List;

public class Vente {

    private int idVente;

    private FloatProperty prixDepart;

    private FloatProperty prixEnCours;

    private Timestamp fin;

    private ListProperty<Enchere> encheres;

    private Produit produit;

    public Vente(int idVente, float prixDepart, Timestamp fin, Produit produit) {
        this.idVente = idVente;
        this.prixDepart = new SimpleFloatProperty(prixDepart);
        this.fin = fin;
        this.encheres = new SimpleListProperty<>();
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

    public ObservableList<Enchere> getEncheres() {
        return encheres.get();
    }

    public ListProperty<Enchere> encheresProperty() {
        return encheres;
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

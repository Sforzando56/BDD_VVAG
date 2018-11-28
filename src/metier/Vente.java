package metier;

import javafx.beans.property.*;

import java.sql.Timestamp;

public class Vente {
    private int idVente;
    private FloatProperty prixDepart;
    private Timestamp fin;
    private Produit produit;
    private int idSalle;

    public Vente(int idVente, float prixDepart, Timestamp fin, Produit produit, int idSalle) {
        this.idVente = idVente;
        this.prixDepart = new SimpleFloatProperty(prixDepart);
        this.fin = fin;
        this.produit = produit;
        this.idSalle = idSalle;
    }

    public int getIdSalle() {
        return idSalle;
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

    public void setFin(Timestamp fin) {
        this.fin = fin;
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
    }
}

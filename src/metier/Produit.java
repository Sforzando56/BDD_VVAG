package metier;

import javafx.beans.property.*;

public class Produit {

    private int idProduit;

    private StringProperty nom;

    private FloatProperty prixRevient;

    private IntegerProperty stock;

    public Produit(int idProduit, String nom, float prixRevient, int stock) {
        this.idProduit = idProduit;
        this.nom = new SimpleStringProperty(nom);
        this.prixRevient = new SimpleFloatProperty(prixRevient);
        this.stock = new SimpleIntegerProperty(stock);
    }

    public int getIdProduit() {
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

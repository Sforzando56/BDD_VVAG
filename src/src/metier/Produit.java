package metier;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Produit {

    private int idProduit;

    private StringProperty nom;

    private FloatProperty prixRevient;

    private IntegerProperty stock;

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

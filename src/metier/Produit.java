package metier;


import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


import java.util.List;

public class Produit {
    private int idProduit;
    private StringProperty nom;
    private FloatProperty prixRevient;
    private IntegerProperty stock;
    private List<Categorie> categories;


    public Produit(int idProduit, String nom, float prixRevient, int stock, List<Categorie> categories) {
        this.idProduit = idProduit;
        this.nom = new SimpleStringProperty(nom);
        this.prixRevient = new SimpleFloatProperty(prixRevient);
        this.stock = new SimpleIntegerProperty(stock);
        this.categories = categories;
    }

    public void setStock(int stock) {
    	this.stock.set(stock);
    }
    
    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }
    public List<Categorie> getCategories() {
        return categories;
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

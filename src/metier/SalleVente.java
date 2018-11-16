package metier;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;

public class SalleVente {

    private int idSalle;

    private boolean montante;

    private boolean revocable;

    private boolean dureeLim;

    private boolean enchreLibre;

    private Categorie categorie;

    private ListProperty<Vente> ventes;

    public SalleVente(int idSalle, boolean montante, boolean revocable, Categorie categorie) {
        this.idSalle = idSalle;
        this.montante = montante;
        this.revocable = revocable;
        this.categorie = categorie;
        this.ventes = new SimpleListProperty<>();
    }

    public ObservableList<Vente> getVentes() {
        return ventes.get();
    }

    public ListProperty<Vente> ventesProperty() {
        return ventes;
    }


    public int getIdSalle() {
        return idSalle;
    }

    public boolean isMontante() {
        return montante;
    }

    public boolean isRevocable() {
        return revocable;
    }

    public Categorie getCategorie() {
        return categorie;
    }
}

package metier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SalleVente {
    private int idSalle;
    private boolean montante;
    private boolean revocable;
    private Categorie categorie;
    
    

    public SalleVente(int idSalle, boolean montante, boolean revocable, Categorie categorie) {
		this.idSalle = idSalle;
		this.montante = montante;
		this.revocable = revocable;
		this.categorie = categorie;
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

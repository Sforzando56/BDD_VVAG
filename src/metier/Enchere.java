package metier;

import java.sql.Timestamp;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Enchere {
    private int idEnchere;
    private int idVente;
    private FloatProperty prixAchat;
    private Timestamp dateEnchere;
    private IntegerProperty quantProposee;
    private StringProperty emailUtilisateur;

    public Enchere(int idEnchere, int idVente, float prixAchat, Timestamp dateEnchere, int quantProposee, String emailUtilisateur) {
        this.idEnchere = idEnchere;
        this.idVente = idVente;
        this.prixAchat = new SimpleFloatProperty(prixAchat);
        this.dateEnchere = dateEnchere;
        this.quantProposee = new SimpleIntegerProperty(quantProposee);
        this.emailUtilisateur = new SimpleStringProperty(emailUtilisateur);
    }

	public int getIdEnchere() {
		return idEnchere;
	}

	public int getIdVente() {
		return idVente;
	}

	public float getPrixAchat() {
		return prixAchat.get();
	}

	public FloatProperty prixAchatProperty() {
		return prixAchat;
	}

	public Timestamp getDateEnchere() {
		return dateEnchere;
	}

	public int getQuantProposee() {
		return quantProposee.get();
	}

	public IntegerProperty quantProposeeProperty() {
		return quantProposee;
	}

	public String getEmailUtilisateur() {
		return emailUtilisateur.get();
	}

	public StringProperty emailUtilisateurProperty() {
		return emailUtilisateur;
	}

	public void setQuantProposee(int quantProposee) {
		this.quantProposee.set(quantProposee);
	}

	public FloatProperty prixTotalProperty(){
    	return new SimpleFloatProperty(quantProposee.get() * prixAchat.get());
	}
}

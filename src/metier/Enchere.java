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

	public FloatProperty getPrixAchat() {
		return prixAchat;
	}

	public Timestamp getDateEnchere() {
		return dateEnchere;
	}

	public IntegerProperty getQuantProposee() {
		return quantProposee;
	}

	public StringProperty getEmailUtilisateur() {
		return emailUtilisateur;
	}
}

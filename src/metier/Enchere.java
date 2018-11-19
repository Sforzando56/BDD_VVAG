package metier;

import javafx.beans.property.IntegerProperty;

import java.sql.Timestamp;

public class Enchere {

    private int idEnchere;

    private IntegerProperty prixAchat;

    private Timestamp dateEnchere;

    private IntegerProperty quantProposee;

    public Enchere(int idEnchere, IntegerProperty prixAchat, Timestamp dateEnchere, IntegerProperty quantProposee) {
        this.idEnchere = idEnchere;
        this.prixAchat = prixAchat;
        this.dateEnchere = dateEnchere;
        this.quantProposee = quantProposee;
    }

    public IntegerProperty getPrixAchat() {
        return prixAchat;
    }

    public Timestamp getDateEnchere() {
        return dateEnchere;
    }

    public IntegerProperty getQuantProposee() {
        return quantProposee;
    }
}

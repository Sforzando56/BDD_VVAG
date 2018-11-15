package metier;

public class SalleVente {

    private int idSalle;

    private boolean montante;

    private boolean revocable;

    private Categorie categorie;

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

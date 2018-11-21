package metier;


public class SalleVente {

    private int idSalle;

    private boolean montante;

    private boolean revocable;

    private boolean dureeLim;

    private boolean enchereLibre;

    private Categorie categorie;

    public SalleVente(int idSalle, boolean montante, boolean revocable, boolean dureeLim, boolean enchereLibre, Categorie categorie) {
        this.idSalle = idSalle;
        this.montante = montante;
        this.revocable = revocable;
        this.categorie = categorie;
        this.dureeLim = dureeLim;
        this.enchereLibre = enchereLibre;
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

    public boolean isDureeLim() {
        return dureeLim;
    }

    public boolean isEnchereLibre() {
        return enchereLibre;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    @Override
    public String toString() {
        return "Salle " + getCategorie().getNom();
    }
}

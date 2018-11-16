package persistence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import metier.*;

import java.sql.*;

public class Requester {

    private static Requester instance;

    private boolean intToBool(int a) {
        return a == 1;
    }

    public static Requester getInstance() {
        if (instance == null) {
            instance = new Requester();
        }

        return instance;
    }

    public ObservableList<SalleVente> getSallesVentes() {
        ObservableList<SalleVente> salles = FXCollections.observableArrayList();

        String selectSallesSQL = "SELECT ID_SALLE, MONTANTE, DUREE_LIM, REVOCABLE, NOM_CATEGORIE, DESCRIPTION, ENCHERE_LIBRE" +
                " from SALLE S, CATEGORIE C WHERE s.nom_categorie = c.nom";

        try {
            Connection dbConnection = BddConnection.getConnection();
            Statement statement = dbConnection.createStatement();

            ResultSet rs = statement.executeQuery(selectSallesSQL);
            while (rs.next()) {
                Categorie categorie = new Categorie(rs.getString("nom_categorie"), rs.getString("description"));
                salles.add(
                        new SalleVente(rs.getInt("id_salle"), this.intToBool(rs.getInt("montante")), this.intToBool(rs.getInt("revocable")), categorie)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salles;
    }

    public ObservableList<Vente> getVentesBySalle(int idSalle) {
        ObservableList<Vente> ventes = FXCollections.observableArrayList();

        String selectSallesSQL = "SELECT ID_VENTE, P.ID_PRODUIT, PRIX_DEP, PRIX_REVIENT, DATE_FIN, STOCK, NOM, NOM_CATEGORIE from VENTE V, PRODUIT P " +
                "WHERE V.ID_PRODUIT = P.ID_PRODUIT AND V.ID_SALLE = " + Integer.toString(idSalle);

        try {
            Connection dbConnection = BddConnection.getConnection();
            Statement statement = dbConnection.createStatement();

            ResultSet rs = statement.executeQuery(selectSallesSQL);
            while (rs.next()) {
                Produit produit = new Produit(rs.getInt("id_produit"), rs.getString("nom"), rs.getFloat("prix_revient"), rs.getInt("stock"));
                ventes.add(
                        new Vente(rs.getInt("id_vente"), rs.getFloat("prix_dep"), rs.getTimestamp("date_fin"), produit)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ventes;
    }

    public void upsertUtilisateur(Utilisateur utilisateur) {
        String upsertUser = "MERGE INTO USER USING dual ON (\"email\" = \"" + utilisateur.getEmail() + "\") " +
                "WHEN NOT MATCHED THEN INSERT (\"nom\", \"prenom\", \"code_postal\", \"adresse\") VALUES (" + utilisateur.getNom() + ","
                + utilisateur.getPrenom() + "," + utilisateur.getCodePostal() + "," + utilisateur.getAdresse() + ")";
        try {
            Connection dbConnection = BddConnection.getConnection();
            Statement statement = dbConnection.createStatement();

            statement.executeQuery(upsertUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package persistence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import metier.Categorie;
import metier.SalleVente;

import java.sql.*;

public class Requester {

    private static Requester instance;

    private boolean intToBool(int a) {
        return a == 1;
    }

    private static Requester getInstance(){
        if (instance == null){
            instance = new Requester();
        }

        return instance;
    }

    public  ObservableList<SalleVente> getSallesVentes() {
        Connection dbConnection = null;
        Statement statement = null;
        ObservableList<SalleVente> salles = FXCollections.observableArrayList();
        ;
        String selectSallesSQL = "SELECT ID_SALLE, MONTANTE, DUREE_LIM, REVOCABLE, NOM_CATEGORIE, DESCRIPTION, ENCHERE_LIBRE" +
                " from Salle s, Categorie c WHERE s.nom_categorie = c.nom;";

        try {
            dbConnection = BddConnection.getConnection();
            statement = dbConnection.createStatement();

            ResultSet rs = statement.executeQuery(selectSallesSQL);
            while (rs.next()) {
                Categorie categorie = new Categorie(rs.getString("nom_categorie"), rs.getString("description"));
                salles.add(
                        new SalleVente(rs.getInt("id_salle"), this.intToBool(rs.getInt("montante")), this.intToBool(rs.getInt("revocable")), categorie)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BddConnection.closeConnection(dbConnection, statement);
        }

        return salles;
    }
}

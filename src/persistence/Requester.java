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

    private int boolToInt(boolean a) {
        if (a) {
            return 1;
        }
        return 0;
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
                        new SalleVente(
                                rs.getInt("id_salle"),
                                this.intToBool(rs.getInt("montante")),
                                this.intToBool(rs.getInt("revocable")),
                                this.intToBool(rs.getInt("duree_lim")),
                                this.intToBool(rs.getInt("enchere_libre")),
                                categorie)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salles;
    }

    public ObservableList<Vente> getVentesBySalle(int idSalle) {
        ObservableList<Vente> ventes = FXCollections.observableArrayList();

        String selectSallesSQL = "SELECT ID_VENTE, P.ID_PRODUIT, PRIX_DEP, PRIX_REVIENT, DATE_FIN, STOCK, NOM from VENTE V, PRODUIT P " +
                "WHERE V.ID_PRODUIT = P.ID_PRODUIT AND V.ID_SALLE = " + Integer.toString(idSalle);

        try {
            Connection dbConnection = BddConnection.getConnection();
            Statement statement = dbConnection.createStatement();

            ResultSet rs = statement.executeQuery(selectSallesSQL);
            while (rs.next()) {
                Produit produit = new Produit(rs.getInt("id_produit"), rs.getString("nom"), rs.getFloat("prix_revient"), rs.getInt("stock"), null);
                ventes.add(
                        new Vente(rs.getInt("id_vente"), rs.getFloat("prix_dep"), rs.getTimestamp("date_fin"), produit, idSalle)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ventes;
    }

    public void upsertUtilisateur(Utilisateur utilisateur) {
        try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("INSERT INTO UTILISATEUR"
                + " VALUES "
                + "(?, ?, ?, ?, ?)")) {
            stmt.setString(1, utilisateur.getEmail());
            stmt.setString(2, utilisateur.getNom());
            stmt.setString(3, utilisateur.getPrenom());
            stmt.setInt(4, utilisateur.getCodePostal());
            stmt.setString(5, utilisateur.getAdresse());

            stmt.executeQuery();


        } catch (SQLIntegrityConstraintViolationException e) {
            //User déjà dans la base, on pourrait update
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertProduitAndCategories(Produit produit, String emailUtilisateur) {
        String[] generatedId = {"id_produit"};
        try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("INSERT INTO Produit"
                + "(nom, prix_revient, stock, email_utilisateur) VALUES "
                + "(?, ?, ?, ?)", generatedId)) {
            stmt.setString(1, produit.getNom());
            stmt.setDouble(2, produit.getPrixRevient());
            stmt.setInt(3, produit.getStock());
            stmt.setString(4, emailUtilisateur);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                produit.setIdProduit(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Categorie categorie : produit.getCategories()) {
            this.upsertCategorie(categorie);
            try (PreparedStatement stmt2 = BddConnection.getConnection().prepareStatement(
                    "INSERT INTO CATEGORIEPRODUIT VALUES (?,?)"
            )) {
                stmt2.setString(1, categorie.getNom());
                stmt2.setInt(2, produit.getIdProduit());

                stmt2.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertVente(Vente vente) {
        try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("INSERT INTO Vente"
                + "(DATE_FIN, PRIX_DEP, ID_SALLE, ID_PRODUIT) VALUES "
                + "(?, ?, ?, ?)")) {
            stmt.setTimestamp(1, vente.getFin());
            stmt.setFloat(2, vente.getPrixDepart());
            stmt.setInt(3, vente.getIdSalle());
            stmt.setInt(4, vente.getProduit().getIdProduit());

            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertSalle(SalleVente salleVente) throws Exception {
        String[] generatedId = {"id_salle"};
        try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("INSERT INTO SALLE"
                + "(MONTANTE, DUREE_LIM, REVOCABLE, ENCHERE_LIBRE, NOM_CATEGORIE) VALUES "
                + "(?, ?, ?, ?, ?)", generatedId)) {
            stmt.setInt(1, boolToInt(salleVente.isMontante()));
            stmt.setInt(2, boolToInt(salleVente.isDureeLim()));
            stmt.setInt(3, boolToInt(salleVente.isRevocable()));
            stmt.setInt(4, boolToInt(salleVente.isEnchereLibre()));
            stmt.setString(5, salleVente.getCategorie().getNom());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()){
                return rs.getInt(1);
            }
            else {
                return -1;
            }
        }
    }

    private void upsertCategorie(Categorie categorie) {
        try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("INSERT INTO CATEGORIE"
                + " VALUES "
                + "(?, ?)")) {
            stmt.setString(1, categorie.getNom());
            stmt.setString(2, categorie.getDescription());

            stmt.executeQuery();
        } catch (SQLIntegrityConstraintViolationException e) {
            //Catégorie déjà dans la base, on pourrait update
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

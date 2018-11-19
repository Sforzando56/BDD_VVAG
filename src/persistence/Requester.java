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
    
	public Produit createProduit(int id_produit, String nom, double prix_revient, int stock, String email_utilisateur, String nom_categorie) throws SQLException {
		try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("INSERT INTO Produit"
				+ "(id_produit, nom, prix_revient, stock, email_utilisateur, nom_categorie) VALUES "
				+ "(?, ?, ?, ?, ?, ?)")){
			stmt.setInt(1, id_produit);
			stmt.setString(2, nom);
			stmt.setDouble(3, prix_revient);
			stmt.setInt(4, stock);
			stmt.setString(5, email_utilisateur);
			stmt.setString(6, nom_categorie);

			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return new Produit(id_produit, nom, prix_revient, stock, email_utilisateur, nom_categorie);
			}
			else {
				throw new IllegalArgumentException("Problème à l'insertion de la catégorie " + nom);
			}
		}
	}
    
	public Produit getProduit(int id_produit) throws SQLException {
		try (Statement stmt = BddConnection.getConnection().createStatement()) {
			ResultSet rs = stmt.executeQuery("SELECT * FROM Produit WHERE id_produit = '"+ id_produit +"'");
			if (rs.next()) {
				return new Produit(id_produit, rs.getString("nom"), rs.getDouble("prix_revient"), rs.getInt("stock"), rs.getString("email_utilisateur"), rs.getString("nom_categorie"));
			} else {
				throw new IllegalArgumentException("Le produit "+id_produit+" n'existe pas");
			}
		}
	}
    
    public Categorie createCategorie(String nom, String description) throws SQLException {
        try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("INSERT INTO Categorie "
                + "(nom, description) VALUES "
                + "(?, ?)")) {
            stmt.setString(1, nom);
            stmt.setString(2, description);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Categorie(nom, description);
            } else {
                throw new IllegalArgumentException("Problème à l'insertion de la catégorie " + nom);
            }
        }
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
            	int id_produit = rs.getInt("id_produit");
                Produit produit = new Produit(id_produit, rs.getString("nom"), rs.getFloat("prix_revient"), rs.getInt("stock"), rs.getString("nom_utilisateur"), rs.getString("nom_categorie"));

                ventes.add(
                        new Vente(rs.getInt("id_vente"), rs.getFloat("prix_dep"), rs.getTimestamp("date_fin"), produit)
                );

				String selectCategoriesSQL = "SELECT NOM_CATEGORIE, ID_PRODUIT FROM Categorie WHERE ID_PRODUIT = " + Integer.toString(id_produit);
				statement = dbConnection.createStatement();
				ObservableList<Categorie> categories = FXCollections.observableArrayList();

				ResultSet rs2 = statement.executeQuery(selectCategoriesSQL);
				while (rs2.next()) {
					categories.add(new Categorie(rs2.getString("nom"), rs2.getString("description")));
				}
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

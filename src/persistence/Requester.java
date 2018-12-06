package persistence;

import static javafx.application.Platform.exit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import metier.Categorie;
import metier.Enchere;
import metier.Produit;
import metier.SalleVente;
import metier.Utilisateur;
import metier.Vente;
import utils.AlertCreator;

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

    private Requester() {

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

    public boolean insertEnchere(Enchere enchere, Produit produit, boolean dureeLimitee) {
        BddConnection.setAutoCommitConnexion(false);
        Vente v = getVenteAvecProduitConnu(enchere.getIdVente(), produit);

        Timestamp now = Timestamp.from(Instant.now());
        if (now.compareTo(v.getFin()) < 0) {
            try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("INSERT INTO ENCHERE (PRIX_ACHAT, QUANT_PROPOSEE, DATE_ENCHERE, EMAIL_UTILISATEUR, ID_VENTE)"
                    + " VALUES "
                    + "(?, ?, ?, ?, ?)")) {
                stmt.setDouble(1, enchere.getPrixAchat());
                stmt.setInt(2, enchere.getQuantProposee());
                stmt.setTimestamp(3, enchere.getDateEnchere());
                stmt.setString(4, enchere.getEmailUtilisateur());
                stmt.setInt(5, enchere.getIdVente());

                stmt.executeQuery();
                if (!dureeLimitee) {
                    Instant instant = Instant.now();
                    instant = instant.plusSeconds(600); //Date de fin est maintenant + 10mn
                    v.setFin(Timestamp.from(instant));
                    Requester.getInstance().updateDateVente(v);
                }

                SalleVente salle = getSalleVente(v.getIdSalle());
                if(!salle.isMontante()) {
					PreparedStatement stmt_update = BddConnection.getConnection().prepareStatement("UPDATE Produit SET stock = ? WHERE id_produit = ?");
					stmt_update.setInt(1, produit.getStock() - enchere.getQuantProposee());
					stmt_update.setInt(2, produit.getIdProduit());
					produit.setStock(produit.getStock() - enchere.getQuantProposee());
					stmt_update.executeUpdate();
					if(produit.getStock() == 0) {
						Instant instant = Instant.now();
						v.setFin(Timestamp.from(instant));
						Requester.getInstance().updateDateVente(v);
					}
                }


                BddConnection.getConnection().commit();
                return true;
            } catch (SQLException e) {
                try {
                    BddConnection.getConnection().rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }

    public ObservableList<Enchere> getEncheresOrdreByVente(int idVente) {
        BddConnection.setAutoCommitConnexion(true);
        ObservableList<Enchere> encheres = FXCollections.observableArrayList();

        try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("SELECT * FROM Enchere WHERE id_vente = ? ORDER BY ENCHERE.DATE_ENCHERE DESC")) {
            stmt.setInt(1, idVente);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                encheres.add(new Enchere(rs.getInt("id_enchere"), rs.getInt("id_vente"), (float) rs.getDouble("prix_achat"), rs.getTimestamp("date_enchere"), rs.getInt("quant_proposee"), rs.getString("email_utilisateur")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return encheres;
    }

    public ObservableList<Vente> getVentesBySalle(int idSalle) {
        BddConnection.setAutoCommitConnexion(true);
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
        BddConnection.setAutoCommitConnexion(true);
        try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("INSERT INTO UTILISATEUR"
                + " VALUES "
                + "(?, ?, ?, ?)")) {
            stmt.setString(1, utilisateur.getEmail());
            stmt.setString(2, utilisateur.getNom());
            stmt.setString(3, utilisateur.getPrenom());
            stmt.setString(4, utilisateur.getAdresse());

            stmt.executeQuery();


        } catch (SQLIntegrityConstraintViolationException e) {
            //User déjà dans la base, on pourrait update
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertProduit(Produit produit, String emailUtilisateur) {
        BddConnection.setAutoCommitConnexion(true);
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
    }


    private void updateDateVente(Vente vente) throws SQLException {
        PreparedStatement stmt = BddConnection.getConnection().prepareStatement("UPDATE Vente set "
                + "DATE_FIN = ? WHERE id_vente = ?");
        stmt.setTimestamp(1, vente.getFin());
        stmt.setInt(2, vente.getIdVente());

        stmt.executeQuery();

    }

    public boolean insertSalleEtVentes(SalleVente salleVente, ObservableList<Produit> produits, Timestamp fin, Categorie categorie) {
        String[] generatedId = {"id_salle"};
        BddConnection.setAutoCommitConnexion(false);
        int idSalle;
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

            if (rs.next()) {
                idSalle = rs.getInt(1);
            } else {
                return false;
            }

            for (Produit produit : produits) {
                Requester.getInstance().insertCategorieProduit(categorie, produit);

                float prixDepart = AlertCreator.shoxDialogPrix(produit.getNom());
                Vente vente = new Vente(0, prixDepart, fin, produit, idSalle);
                Requester.getInstance().insertVente(vente);
            }

            BddConnection.getConnection().commit();
            return true;

        } catch (SQLException e) {
            try {
                BddConnection.getConnection().rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }

    private void insertCategorieProduit(Categorie categorie, Produit produit) throws SQLException {
        PreparedStatement stmt2 = BddConnection.getConnection().prepareStatement(
                "INSERT INTO CATEGORIEPRODUIT VALUES (?,?)");
        stmt2.setString(1, categorie.getNom());
        stmt2.setInt(2, produit.getIdProduit());

        stmt2.executeQuery();

    }

    private void insertVente(Vente vente) throws SQLException {
        PreparedStatement stmt = BddConnection.getConnection().prepareStatement("INSERT INTO Vente"
                + "(DATE_FIN, PRIX_DEP, ID_SALLE, ID_PRODUIT) VALUES "
                + "(?, ?, ?, ?)");
        stmt.setTimestamp(1, vente.getFin());
        stmt.setFloat(2, vente.getPrixDepart());
        stmt.setInt(3, vente.getIdSalle());
        stmt.setInt(4, vente.getProduit().getIdProduit());

        stmt.executeQuery();
    }

    public void upsertCategorie(Categorie categorie) {
        BddConnection.setAutoCommitConnexion(true);
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

    public SalleVente getSalleVente(int idSalle) {
        try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("SELECT * FROM Salle WHERE id_salle = ?")) {
            stmt.setInt(1, idSalle);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SalleVente(idSalle, rs.getBoolean("montante"), rs.getBoolean("revocable"), rs.getBoolean("duree_lim"), rs.getBoolean("enchere_libre"), getCategorie(rs.getString("nom_categorie")));
            } else {
                System.out.println("Erreur requete get SalleVente");
                exit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            exit();
        }
        return null;
    }
    
    
    public Categorie getCategorie(String nom) {
        try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("SELECT * FROM Categorie WHERE nom = ?")) {
            stmt.setString(1, nom);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Categorie(nom, rs.getString("description"));
            } else {
                System.out.println("Erreur requete get categorie");
                exit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            exit();
        }
        return null;
    }

    public Vente getVenteAvecProduitConnu(int idVente, Produit produit) {
        BddConnection.setAutoCommitConnexion(true);
        try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("SELECT * FROM Vente WHERE id_vente = ?")) {
            stmt.setInt(1, idVente);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Vente(idVente, rs.getFloat("prix_dep"), rs.getTimestamp("date_fin"), produit, rs.getInt("id_salle"));
            } else {
                System.out.println("Erreur requete get vente");
                exit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            exit();
        }
        return null;
    }

    public Enchere getDerniereEnchere(int idVente) {
        BddConnection.setAutoCommitConnexion(true);
        try (PreparedStatement stmt = BddConnection.getConnection().prepareStatement("SELECT ID_ENCHERE, PRIX_ACHAT, DATE_ENCHERE, QUANT_PROPOSEE, EMAIL_UTILISATEUR " +
                "from (SELECT * FROM Enchere E, Vente V " +
                "WHERE V.id_vente = ? AND E.id_vente = V.id_vente ORDER BY E.date_enchere DESC)  WHERE ROWNUM = 1")) {
            stmt.setInt(1, idVente);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Enchere(
                        rs.getInt("id_enchere"), idVente,
                        rs.getFloat("prix_achat"),
                        rs.getTimestamp("date_enchere"),
                        rs.getInt("quant_proposee"),
                        rs.getString("email_utilisateur")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            exit();
        }

        return null;
    }


    public ObservableList<Produit> getProduitsNonEnVente() {
        ObservableList<Produit> produits = FXCollections.observableArrayList();

        try (PreparedStatement stmt = BddConnection.getConnection()
                .prepareStatement("SELECT * FROM Produit WHERE  ID_PRODUIT NOT IN (Select id_produit from VENTE)")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                produits.add(new Produit(rs.getInt("id_produit"), rs.getString("nom"), rs.getFloat("prix_revient"), rs.getInt("stock"), null));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }

}

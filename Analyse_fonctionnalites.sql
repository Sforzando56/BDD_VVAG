--La mise en place d’une Salle de Vente et la sélection de produits déjà disponibles à la vente et permettant le choix du type d’enchères et du prix de départ :

SELECT * FROM Produit WHERE  ID_PRODUIT NOT IN (Select id_produit from VENTE); --Récupérer les produits qui ne sont pas encore associés à une vente 

-- Insertion de la salle et des ventes (après sélection de un ou plusieurs produits retournés avec la requête précédente)
BEGIN; -- s'il y a une exception on fait un rollback
    INSERT INTO CATEGORIE VALUES (?, ?)

    INSERT INTO Salle(MONTANTE, DUREE_LIM, REVOCABLE, ENCHERE_LIBRE, NOM_CATEGORIE) VALUES (?, ?, ?, ?, ?);
  
  -- Pour chaque produit
    INSERT INTO CategorieProduit (NOM_CATEGORIE, ID_PRODUIT) VALUES (?,?);
    INSERT INTO Vente (DATE_FIN, PRIX_DEP, ID_SALLE, ID_PRODUIT) VALUES (?, ?, ?, ?);
COMMIT;

--L’enchère faite par un utilisateur sur un produit mis en vente dans une Salle de Vente :
BEGIN; -- s'il y a une exception on fait un rollback
  SELECT * FROM Vente WHERE id_vente = ?;
  
  SELECT * FROM Salle WHERE id_salle = ?;

  SELECT count(*) FROM ENCHERE WHERE email_utilisateur = ? 

  -- Si enchère limite le nombre d'enchères à 1 par utilisateur et que la requête précédente ne retoune pas 0 enchère ne peut être faite

  -- Si date_courante < date_fin
    INSERT INTO ENCHERE (PRIX_ACHAT, QUANT_PROPOSEE, DATE_ENCHERE, EMAIL_UTILISATEUR, ID_VENTE) VALUES (?, ?, ?, ?, ?);
  
  
  
  -- Si l'enchère est à durée libre
    UPDATE Vente set DATE_FIN = CURRENT_TIMESTAMP + interval '10' minute  WHERE id_vente = ?;
  
  -- Si la vente est descendante
    UPDATE Produit SET STOCK = STOCK - QUANT_PROPOSEE WHERE ID_PRODUIT = ?;
    -- Si le STOCK == 0
          UPDATE Vente set DATE_FIN = CURRENT_TIMESTAMP WHERE id_vente = ?;
COMMIT;


--Le processus de fin d’enchère déterminant le (ou les) utilisateur(s) ayant remporté une vente, en tenant compte du type d’enchère bien sûr :
BEGIN; -- s'il y a une exception on fait un rollback
  -- Si la vente est montante
    SELECT * FROM Enchere WHERE ID_VENTE = ? ORDER BY ENCHERE.DATE_ENCHERE DESC; 
    UPDATE Produit SET STOCK = STOCK - QUANT_PROPOSEE WHERE ID_PRODUIT = ?;

  -- Si la vente est descendante
    SELECT * FROM ENCHERE WHERE ID_VENTE = ?;

  -- si la vente est révocable et que le prix final est inférieur au prix de revient pas de gagnant
COMMIT;


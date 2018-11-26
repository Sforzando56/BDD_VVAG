DROP TABLE CategorieProduit PURGE;
DROP TABLE CaracteristiqueProduit PURGE;
DROP TABLE Enchere PURGE;
DROP TABLE Vente PURGE;
DROP TABLE Caracteristique PURGE;
DROP TABLE Produit PURGE;
DROP TABLE Salle PURGE;
DROP TABLE Categorie PURGE;
DROP TABLE Utilisateur PURGE;



CREATE TABLE Categorie(
  nom VARCHAR2(30) PRIMARY KEY,
  description VARCHAR2(100) NOT NULL
);

CREATE TABLE Utilisateur(
  email VARCHAR2(30) PRIMARY KEY,
  nom VARCHAR2(30) NOT NULL,
  prenom VARCHAR2(30) NOT NULL,
  adresse VARCHAR2(100) NOT NULL
);

CREATE TABLE Salle(
   id_salle INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   montante NUMBER(1, 0) DEFAULT 1 CHECK(montante = 0 OR montante = 1),
   duree_lim NUMBER(1, 0) DEFAULT 0 CHECK(duree_lim = 0 OR duree_lim=1),
   revocable NUMBER(1, 0) DEFAULT 0 CHECK(revocable = 0 OR revocable=1),
   enchere_libre NUMBER(1,0) DEFAULT 0 CHECK(enchere_libre = 0 OR enchere_libre=1),
   nom_categorie VARCHAR2(30),
   FOREIGN KEY(nom_categorie) REFERENCES Categorie (nom)
);

CREATE TABLE Produit(
  id_produit INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  prix_revient NUMERIC(10, 2),
  stock INT NOT NULL CHECK(stock >= 0),
  email_utilisateur VARCHAR2(30),
  FOREIGN KEY(email_utilisateur) REFERENCES Utilisateur (email)
);

CREATE TABLE Caracteristique(
  nom VARCHAR2(30) NOT NULL,
  valeur VARCHAR2(30),
  id_produit INT,
  PRIMARY KEY(nom, id_produit),
  FOREIGN KEY(id_produit) REFERENCES Produit
);

CREATE TABLE Vente(
  id_vente INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  date_fin TIMESTAMP NOT NULL,
  prix_dep NUMERIC(10, 2) NOT NULL CHECK(prix_dep >= 0),
  id_produit INT,
  id_salle INT,
  FOREIGN KEY(id_produit) REFERENCES Produit,
  FOREIGN KEY(id_salle) REFERENCES Salle
);

CREATE TABLE CategorieProduit(
  nom_categorie VARCHAR2(30), 
  id_produit INT,
  PRIMARY KEY(nom_categorie, id_produit),
  FOREIGN KEY(nom_categorie) REFERENCES Categorie(nom),
  FOREIGN KEY(id_produit) REFERENCES Produit(id_produit)
);

CREATE TABLE Enchere(
  id_enchere INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  date_enchere TIMESTAMP NOT NULL,
  email_utilisateur VARCHAR2(30),
  id_vente INT,
  FOREIGN KEY (email_utilisateur) REFERENCES Utilisateur (email),
  FOREIGN KEY(id_vente) REFERENCES Vente (id_vente)
);

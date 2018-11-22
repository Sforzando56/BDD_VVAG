DROP TABLE CategorieProduit PURGE;
DROP TABLE Enchere PURGE;
DROP TABLE Vente_Revocable PURGE;
DROP TABLE Vente_Duree_Limitee PURGE;
DROP TABLE Vente_Enchere_Unique PURGE;
DROP TABLE Vente_Descendante PURGE;
DROP TABLE Vente PURGE;
DROP TABLE Salle PURGE;
DROP TABLE Caracteristique PURGE;
DROP TABLE Produit PURGE;
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
  code_postal CHAR(5) NOT NULL CHECK(LENGTH(code_postal) = 5),
  adresse VARCHAR2(100) NOT NULL
);

CREATE TABLE Salle(
   id_salle INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   nom_categorie VARCHAR2(30),
   FOREIGN KEY(nom_categorie) REFERENCES Categorie (nom)
);

CREATE TABLE Produit(
  id_produit INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  prix_revient NUMERIC(10, 2),
  stock INT NOT NULL CHECK(stock >= 0),
  email_utilisateur VARCHAR2(30) ,
  nom_categorie VARCHAR2(30) ,
  FOREIGN KEY(email_utilisateur) REFERENCES Utilisateur (email),
  FOREIGN KEY (nom_categorie) REFERENCES Categorie (nom)
);

CREATE TABLE Caracteristique(
  nom VARCHAR2(30),
  valeur NUMERIC(10, 2) NOT NULL CHECK(valeur >= 0),
  id_produit INT,
  PRIMARY KEY(nom, id_produit),
  FOREIGN KEY(id_produit) REFERENCES Produit
);

CREATE TABLE Vente(
  id_vente INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  date_fin TIMESTAMP NOT NULL,
  prix_dep NUMERIC(10, 2) NOT NULL CHECK(prix_dep >= 0),
  id_produit INT,
  FOREIGN KEY(id_produit) REFERENCES Produit 
);

CREATE TABLE Vente_Revocable (
  id_vente INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  FOREIGN KEY(id_vente) REFERENCES Vente
);

CREATE TABLE Vente_Duree_Limitee (
  id_vente INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  FOREIGN KEY(id_vente) REFERENCES Vente
);

CREATE TABLE Vente_Descendante (
  id_vente INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  FOREIGN KEY(id_vente) REFERENCES Vente
);

CREATE TABLE Vente_Enchere_Unique (
  id_vente INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  FOREIGN KEY(id_vente) REFERENCES Vente
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
  prixAchat NUMERIC(10, 2) NOT NULL CHECK(prixAchat > 0),
  quantProposee INT NOT NULL CHECK(quantProposee > 0),
  date_enchere TIMESTAMP NOT NULL,
  email_utilisateur VARCHAR2(30),
  id_vente INT,
  FOREIGN KEY (email_utilisateur) REFERENCES Utilisateur (email),
  FOREIGN KEY(id_vente) REFERENCES Vente (id_vente)
);
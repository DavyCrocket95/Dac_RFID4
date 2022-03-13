package com.example.dac_rfid4.commons;

public class Constants {

    //*********** Constantes Collections et leur champs */
    //Users
    String TABLE_USERS = "Users";     //Table
    String NAME = "name";       //Les champs
    String EMAIL = "email";
    String SIRET = "SIRET";
    String ONLINE = "online";
    String TYPE_USER = "administrator";

    //Produits
    String TABLE_PRODUITS = "Produits";     //Table
    String NAME_PROD = "name";       //Les champs
    String NUM_PROD = "numero";
    String DATE_PROD = "dateProd";      //Date de fabrication


    //Documetns
    String TABLE_DOC = "Doc";             //Table
    String ID_PROD = "idProd";      //Id Produit pour faire le lien avec les documents
    String FORMAT_DOC = "format";   //Txt-Pdf, audio, video, photo
    String TITRE_DOC = "titre";
    String AUTEUR_DOC = "auteur";
    String DATE_DOC = "dateEdition";
    String ORIGINE_DOC = "provenance";
    String CATEGORIE_DOC = "categorie";
    String RESUME_DOC = "resume";
    String ARCHIVE = "archive";

    //KEY
    String KEY_NUMPROD = "numProd";
    String KEY_IDPROD = "idProd";
    String KEY_IDDOC = "idDoc";
    String KEY_NAMEPROD = "nameProd";
    String KEY_PROVPROD = "ProvenanceProd";
    String KEY_CONTENU = "contenu";
    String KEY_ADMIN = "admin";
}

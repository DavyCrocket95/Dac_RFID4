package com.example.dac_rfid4;

public class ModelDoc {
    //nommage doit etre identique que celui du Firebase
    private String titre;
    private String format;
    private String categorie;
    private String auteur;
    private String archive;
    private String resume;
    private String contenuDoc;
    private String IdProd;

    public ModelDoc() {
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getContenuDoc() {
        return contenuDoc;
    }

    public void setContenuDoc(String contenuDoc) {
        this.contenuDoc = contenuDoc;
    }

    public String getIdProd() {
        return IdProd;
    }

    public void setIdProd(String idProd) {
        IdProd = idProd;
    }
}

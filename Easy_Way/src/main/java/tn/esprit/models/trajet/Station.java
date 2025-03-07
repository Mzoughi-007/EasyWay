package tn.esprit.models.trajet;

public class Station {
    private int id,id_admin,id_ligne;
    private String nom,localisation;

    public Station() {}

    public Station(int id, String nom, String localisation, int id_ligne, int id_admin) {
        this.id = id;
        this.nom = nom;
        this.localisation = localisation;
        this.id_admin = id_admin;
        this.id_ligne = id_ligne;
    }

    public Station(String nom, String localisation, int id_ligne, int id_admin) {
        this.nom = nom;
        this.localisation = localisation;
        this.id_admin = id_admin;
        this.id_ligne = id_ligne;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_admin() {
        return id_admin;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    public int getId_ligne() {
        return id_ligne;
    }

    public void setId_ligne(int id_ligne) {
        this.id_ligne = id_ligne;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    @Override
    public String toString() {
        return "Station{" +
                ", nom='" + nom + '\'' +
                ", localisation='" + localisation + '\'' +
                '}'+"\n";
    }
}

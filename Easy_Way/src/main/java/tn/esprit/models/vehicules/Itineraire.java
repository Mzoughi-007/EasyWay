package tn.esprit.models.vehicules;

public class Itineraire {
    private int id;
    private String nom;
    private String description;


    public Itineraire() {}


    public Itineraire(int id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Itineraire{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
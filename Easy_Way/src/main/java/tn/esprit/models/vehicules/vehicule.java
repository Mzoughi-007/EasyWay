package tn.esprit.models.vehicules;

public abstract class vehicule {
    protected int id;
    protected String immatriculation;
    protected int capacite;
    protected Etat etat;
    protected int idTrajet;
    protected TypeVehicule typeVehicule;
    protected int idConducteur;

    public vehicule() {}

    public vehicule(int id, String immatriculation, int capacite, Etat etat, int idTrajet, TypeVehicule typeVehicule, int idConducteur) {
        this.id = id;
        this.immatriculation = immatriculation;
        this.capacite = capacite;
        this.etat = etat;
        this.idTrajet = idTrajet;
        this.typeVehicule = typeVehicule;
        this.idConducteur = idConducteur;
    }

    public int getId() {
        return id;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public int getCapacite() {
        return capacite;
    }

    public Etat getEtat() {
        return etat;
    }

    public int getIdTrajet() {
        return idTrajet;
    }

    public TypeVehicule getTypeVehicule() {
        return typeVehicule;
    }

    public int getIdConducteur() {
        return idConducteur;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public void setCapacite(int capacite) {
        if (capacite < 0) {
            throw new IllegalArgumentException("La capacité ne peut pas être négative.");
        }
        this.capacite = capacite;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public void setIdTrajet(int idTrajet) {
        this.idTrajet = idTrajet;
    }

    public void setTypeVehicule(TypeVehicule typeVehicule) {
        this.typeVehicule = typeVehicule;
    }

    public void setIdConducteur(int idConducteur) {
        this.idConducteur = idConducteur;
    }

    // Méthode abstraite pour forcer les sous-classes à définir leur propre comportement
    public abstract void afficherDetails();

    @Override
    public String toString() {
        return "Vehicule{" +
                "id=" + id +
                ", immatriculation='" + immatriculation + '\'' +
                ", capacite=" + capacite +
                ", etat=" + etat +
                ", idTrajet=" + idTrajet +
                ", typeVehicule=" + typeVehicule +
                ", idConducteur=" + idConducteur +
                '}';
    }
}

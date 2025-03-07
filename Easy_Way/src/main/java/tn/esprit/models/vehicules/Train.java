package tn.esprit.models.vehicules;

public class Train extends vehicule {
    private double longueurReseau;
    private int nombreLignes;
    private int nombreWagons;
    private double vitesseMaximale;
    private String proprietaire;


    public Train() {}


    public Train(int id, String immatriculation, int capacite, Etat etat, int idTrajet, TypeVehicule typeVehicule, int idConducteur,
                 double longueurReseau, int nombreLignes, int nombreWagons, double vitesseMaximale, String proprietaire) {
        super(id, immatriculation, capacite, etat, idTrajet, typeVehicule, idConducteur); // Appel au constructeur de la classe mère
        this.longueurReseau = longueurReseau;
        this.nombreLignes = nombreLignes;
        this.nombreWagons = nombreWagons;
        this.vitesseMaximale = vitesseMaximale;
        this.proprietaire = proprietaire;
    }

    // Getters et Setters
    public double getLongueurReseau() {
        return longueurReseau;
    }

    public void setLongueurReseau(double longueurReseau) {
        this.longueurReseau = longueurReseau;
    }

    public int getNombreLignes() {
        return nombreLignes;
    }

    public void setNombreLignes(int nombreLignes) {
        this.nombreLignes = nombreLignes;
    }

    public int getNombreWagons() {
        return nombreWagons;
    }

    public void setNombreWagons(int nombreWagons) {
        this.nombreWagons = nombreWagons;
    }

    public double getVitesseMaximale() {
        return vitesseMaximale;
    }

    public void setVitesseMaximale(double vitesseMaximale) {
        this.vitesseMaximale = vitesseMaximale;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }


    @Override
    public void afficherDetails() {
        System.out.println("Détails du train :");
        System.out.println("Immatriculation : " + getImmatriculation());
        System.out.println("Capacité : " + getCapacite());
        System.out.println("État : " + getEtat());
        System.out.println("ID Trajet : " + getIdTrajet());
        System.out.println("Type de véhicule : " + getTypeVehicule());
        System.out.println("ID Conducteur : " + getIdConducteur());
        System.out.println("Longueur du réseau : " + longueurReseau + " km");
        System.out.println("Nombre de lignes : " + nombreLignes);
        System.out.println("Nombre de wagons : " + nombreWagons);
        System.out.println("Vitesse maximale : " + vitesseMaximale + " km/h");
        System.out.println("Propriétaire : " + proprietaire);
    }


    @Override
    public String toString() {
        return super.toString() + ", Longueur du réseau=" + longueurReseau + " km" +
                ", Nombre de lignes=" + nombreLignes + ", Nombre de wagons=" + nombreWagons +
                ", Vitesse maximale=" + vitesseMaximale + " km/h, Propriétaire=" + proprietaire;
    }
}
package tn.esprit.models.vehicules;

public class Metro extends vehicule {
    private double longueurReseau;  // Longueur du réseau en km
    private int nombreLignes;  // Nombre de lignes du réseau
    private int nombreRames;  // Nombre total de rames
    private String proprietaire;  // Propriétaire du réseau

    private int idVehicule;


    public Metro() {
        super();
    }

    public Metro(int id, String immatriculation, int capacite, Etat etat, int idTrajet, TypeVehicule typeVehicule, int idConducteur,
                 double longueurReseau, int nombreLignes, int nombreRames, String proprietaire) {
        super(id, immatriculation, capacite, etat, idTrajet, typeVehicule, idConducteur);
        this.longueurReseau = longueurReseau;
        this.nombreLignes = nombreLignes;
        this.nombreRames = nombreRames;
        this.proprietaire = proprietaire;
    }

    // Getters et Setters
    public double getLongueurReseau() {
        return longueurReseau;
    }

    public int getNombreLignes() {
        return nombreLignes;
    }

    public int getNombreRames() {
        return nombreRames;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public void setLongueurReseau(double longueurReseau) {
        this.longueurReseau = longueurReseau;
    }

    public void setNombreLignes(int nombreLignes) {
        this.nombreLignes = nombreLignes;
    }

    public void setNombreRames(int nombreRames) {
        this.nombreRames = nombreRames;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    @Override
    public void afficherDetails() {
        System.out.println("Metro ID: " + id);
        System.out.println("Immatriculation: " + immatriculation);
        System.out.println("Capacité: " + capacite);
        System.out.println("État: " + etat);
        System.out.println("ID Trajet: " + idTrajet);
        System.out.println("Type de Véhicule: " + typeVehicule);
        System.out.println("ID Conducteur: " + idConducteur);
        System.out.println("Longueur Réseau: " + longueurReseau + " km");
        System.out.println("Nombre de Lignes: " + nombreLignes);
        System.out.println("Nombre de Rames: " + nombreRames);
        System.out.println("Propriétaire: " + proprietaire);
    }

    @Override
    public String toString() {
        return super.toString() + ", Longueur réseau=" + longueurReseau + " km" + ", Lignes=" + nombreLignes +
                ", Rames=" + nombreRames + ", Propriétaire=" + proprietaire;
    }
}

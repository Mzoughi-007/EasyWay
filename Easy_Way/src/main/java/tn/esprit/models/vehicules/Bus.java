package tn.esprit.models.vehicules;

public class Bus extends vehicule {
    private int nombrePortes;       // Nombre de portes du bus
    private TypeService typeService; // Type de service (urbain, interurbain, etc.)
    private int nombreDePlaces;     // Nombre de places assises
    private String compagnie;       // Compagnie propriétaire du bus
    private boolean climatisation;  // Indique si le bus est climatisé

    // Constructeur par défaut
    public Bus() {}

    // Constructeur avec tous les attributs
    public Bus(int id, String immatriculation, int capacite, Etat etat, int idTrajet, TypeVehicule typeVehicule, int idConducteur,
               int nombrePortes, TypeService typeService, int nombreDePlaces, String compagnie, boolean climatisation) {
        super(id, immatriculation, capacite, etat, idTrajet, typeVehicule, idConducteur); // Appel au constructeur de la classe mère
        this.nombrePortes = nombrePortes;
        this.typeService = typeService;
        this.nombreDePlaces = nombreDePlaces;
        this.compagnie = compagnie;
        this.climatisation = climatisation;
    }

    // Getters et Setters
    public int getNombrePortes() {
        return nombrePortes;
    }

    public void setNombrePortes(int nombrePortes) {
        this.nombrePortes = nombrePortes;
    }

    public TypeService getTypeService() {
        return typeService;
    }

    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }

    public int getNombreDePlaces() {
        return nombreDePlaces;
    }

    public void setNombreDePlaces(int nombreDePlaces) {
        this.nombreDePlaces = nombreDePlaces;
    }

    public String getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(String compagnie) {
        this.compagnie = compagnie;
    }

    public boolean isClimatisation() {
        return climatisation;
    }

    public void setClimatisation(boolean climatisation) {
        this.climatisation = climatisation;
    }


    @Override
    public void afficherDetails() {
        System.out.println("Détails du bus :");
        System.out.println("Immatriculation : " + getImmatriculation());
        System.out.println("Capacité : " + getCapacite());
        System.out.println("État : " + getEtat());
        System.out.println("ID Trajet : " + getIdTrajet());
        System.out.println("Type de véhicule : " + getTypeVehicule());
        System.out.println("ID Conducteur : " + getIdConducteur());
        System.out.println("Nombre de portes : " + nombrePortes);
        System.out.println("Type de service : " + typeService);
        System.out.println("Nombre de places : " + nombreDePlaces);
        System.out.println("Compagnie : " + compagnie);
        System.out.println("Climatisation : " + (climatisation ? "Oui" : "Non"));
    }


    @Override
    public String toString() {
        return super.toString() + ", Nombre de portes=" + nombrePortes +
                ", Type de service=" + typeService +
                ", Nombre de places=" + nombreDePlaces +
                ", Compagnie='" + compagnie + '\'' +
                ", Climatisation=" + (climatisation ? "Oui" : "Non");
    }
}
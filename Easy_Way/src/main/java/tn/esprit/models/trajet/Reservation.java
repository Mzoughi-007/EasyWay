package tn.esprit.models.trajet;

public class Reservation {
    private int id,nb,user_id;
    private String depart,arret,vehicule;

    public Reservation() {}

    public Reservation(int id, String depart, String arret, String vehicule, int nb, int user_id) {
        this.id = id;
        this.depart = depart;
        this.arret = arret;
        this.vehicule = vehicule;
        this.nb = nb;
        this.user_id = user_id;
    }

    public Reservation(String depart, String arret, String vehicule, int nb, int user_id) {
        this.depart = depart;
        this.arret = arret;
        this.vehicule = vehicule;
        this.nb = nb;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNb() {
        return nb;
    }

    public void setNb(int nb) {
        this.nb = nb;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getArret() {
        return arret;
    }

    public void setArret(String arret) {
        this.arret = arret;
    }

    public String getVehicule() {
        return vehicule;
    }

    public void setVehicule(String vehicule) {
        this.vehicule = vehicule;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "nb=" + nb +
                ", depart='" + depart + '\'' +
                ", arret='" + arret + '\'' +
                ", vehicule='" + vehicule + '\'' +
                '}';
    }
}

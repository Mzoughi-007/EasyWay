package tn.esprit.models.trajet;

public class Trajet {
    private int id, distance,user_id;
    private String depart,arret,etat,heure_depart,heure_arrive,duree;
    public Trajet() {}

    public Trajet(int id, String duree, int distance, String heure_depart, String heure_arrive, String depart, String arret, String etat, int user_id) {
        this.id = id;
        this.duree = duree;
        this.distance = distance;
        this.heure_depart = heure_depart;
        this.heure_arrive = heure_arrive;
        this.depart = depart;
        this.arret = arret;
        this.etat = etat;
        this.user_id = user_id;
    }

    public Trajet(String duree, int distance, String heure_depart, String heure_arrive, String depart, String arret, String etat, int user_id) {
        this.duree = duree;
        this.distance = distance;
        this.heure_depart = heure_depart;
        this.heure_arrive = heure_arrive;
        this.depart = depart;
        this.arret = arret;
        this.etat = etat;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getHeure_depart() {
        return heure_depart;
    }

    public void setHeure_depart(String heure_depart) {
        this.heure_depart = heure_depart;
    }

    public String getHeure_arrive() {
        return heure_arrive;
    }

    public void setHeure_arrive(String heure_arrive) {
        this.heure_arrive = heure_arrive;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    @Override
    public String toString() {
        return "Trajet{" +
                ", duree=" + duree +
                ", distance=" + distance +
                ", heure_depart=" + heure_depart +
                ", heure_arrive=" + heure_arrive +
                ", depart='" + depart + '\'' +
                ", arret='" + arret + '\'' +
                ", etat='" + etat + '\'' +
                '}'+"\n";
    }
}

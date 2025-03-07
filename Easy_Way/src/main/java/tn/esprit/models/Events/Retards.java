package tn.esprit.models.Events;

import java.sql.Timestamp;

public class Retards {

    private int ligneAffectee;
    private Timestamp heurePrevue;
    private Timestamp heureActuelle;
    private String meteo;
    private String traffic;
    private int retardMinutes;

    public Retards(int ligneAffectee, Timestamp heurePrevue, Timestamp heureActuelle, String meteo, String traffic, int retardMinutes) {
        this.ligneAffectee = ligneAffectee;
        this.heurePrevue = heurePrevue;
        this.heureActuelle = heureActuelle;
        this.meteo = meteo;
        this.traffic = traffic;
        this.retardMinutes = retardMinutes;
    }

    public Timestamp getHeurePrevue() {
        return heurePrevue;
    }

    public void setHeurePrevue(Timestamp heurePrevue) {
        this.heurePrevue = heurePrevue;
    }

    public Timestamp getHeureActuelle() {
        return heureActuelle;
    }

    public void setHeureActuelle(Timestamp heureActuelle) {
        this.heureActuelle = heureActuelle;
    }

    public String getMeteo() {
        return meteo;
    }

    public void setMeteo(String meteo) {
        this.meteo = meteo;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public int getRetardMinutes() {
        return retardMinutes;
    }

    public void setRetardMinutes(int retardMinutes) {
        this.retardMinutes = retardMinutes;
    }

    public int getLigneAffectee() {
        return ligneAffectee;
    }

    public void setLigneAffectee(int ligneAffectee) {
        this.ligneAffectee = ligneAffectee;
    }
}




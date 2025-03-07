package tn.esprit.models.Events;

import java.util.Date;

public class Evenements {
    private int id_event, id_ligne_affectee, id_createur ;
    private TypeEvenement type_evenement ;
    private String description ;
    private java.sql.Date date_debut,date_fin;
    private StatusEvenement status_evenement ;

    public Evenements() {}
    public Evenements(int id_event,TypeEvenement type_evenement, int id_ligne_affectee, String description, java.sql.Date date_debut, java.sql.Date date_fin, StatusEvenement status_evenement , int id_createur) {
        this.id_event = id_event;
        this.type_evenement = type_evenement;
        this.id_ligne_affectee = id_ligne_affectee;
        this.description = description;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.status_evenement = status_evenement ;
        this.id_createur = id_createur;
    }
    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public int getId_ligne_affectee() {
        return id_ligne_affectee;
    }

    public void setId_ligne_affectee(int id_ligne_affectee) {
        this.id_ligne_affectee = id_ligne_affectee;
    }

    public TypeEvenement getType_evenement() {
        return type_evenement;
    }

    public void setType_evenement(TypeEvenement type_evenement) {
        this.type_evenement = type_evenement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.sql.Date getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(java.sql.Date date_debut) {
        this.date_debut = date_debut;
    }

    public java.sql.Date getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(java.sql.Date date_fin) {
        this.date_fin = date_fin;
    }

    public StatusEvenement getStatus_evenement() {
        return status_evenement;
    }

    public void setStatus_evenement(StatusEvenement status_evenement) {
        this.status_evenement = status_evenement;
    }

    @Override
    public String toString() {
        return "Evenement [id_event= " + id_event +
                ", id_ligne_affectee= " +id_ligne_affectee +
                ", type d'evenement= "+ type_evenement+
                ", description= "+description+
                ", date debut= "+date_debut+
                ", date fin= "+date_fin+
                ", status d'evenement= "+status_evenement+
                "] \n";
    }


    public int getId_createur() {
        return id_createur;
    }

    public void setId_createur(int id_createur) {
        this.id_createur = id_createur;
    }
}
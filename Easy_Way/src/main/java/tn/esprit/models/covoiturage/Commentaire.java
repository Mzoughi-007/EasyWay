package tn.esprit.models.covoiturage;

import tn.esprit.services.user.ServiceUser;

import java.sql.Date;

public class Commentaire {
    private int id_com;
    private int id_post;
    private int id_user;
    private String contenu;
    private Date date_creat;
    private String nom;// Attribut pour la date de création du commentaire

    // Constructeurs
    public Commentaire() {
    }

    public Commentaire(int id_com, int id_post, int id_user, String contenu, Date date_creat ,  String nom) {
        this.id_com = id_com;
        this.id_post = id_post;
        this.id_user = id_user;
        this.contenu = contenu;
        this.date_creat = date_creat;
        this.nom = nom;
    }

    public Commentaire(int id_post, int id_user, String contenu, Date date_creat ,  String nom) {
        this.id_post = id_post;
        this.id_user = id_user;
        this.contenu = contenu;
        this.date_creat = date_creat;
        this.nom = nom;// Attribuer la date de création
    }


    // Getters et Setters
    public int getId_com() {
        return id_com;
    }

    public void setId_com(int id_com) {
        this.id_com = id_com;
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDate_creat() {
        return date_creat;
    }

    public void setDate_creat(Date date_creat) {
        this.date_creat = date_creat;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    @Override


    public String toString() {
        ServiceUser serviceUser = new ServiceUser();
        String nomUtilisateur = serviceUser.getUserNameById(this.id_user);

        return "Commentaire{" +
                "id_com=" + id_com +
                ", id_post=" + id_post +
                ", username=" + nom +
                ", id_user=" + id_user +
                " (Nom: " + nomUtilisateur + ")" + // Ajout du nom de l'utilisateur
                ", contenu='" + contenu + '\'' +
                ", date_creat=" + date_creat +
                '}';
    }


    public static boolean isSafe(String comment) {
        String[] badWords = {
                // Anglais
                "fuck", "shit", "bitch", "asshole", "bastard", "damn", "crap", "dick", "pussy",
                "cock", "motherfucker", "whore", "slut", "cunt", "faggot", "nigger", "retard",

                // Français
                "merde", "putain", "salope", "connard", "enculé", "bâtard", "nique", "bite",
                "couille", "pd", "bordel", "ta gueule", "fdp", "trou du cul", "chiotte"
        };

        String lowerCaseComment = comment.toLowerCase();

        for (String badWord : badWords) {
            if (lowerCaseComment.contains(badWord)) {
                return false;
            }
        }
        return true;
    }



}

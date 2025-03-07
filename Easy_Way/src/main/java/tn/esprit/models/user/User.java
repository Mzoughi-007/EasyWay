package tn.esprit.models.user;

import java.time.LocalDateTime;

public class User {
    public enum Role {
        Conducteur , Passager , Admin
    }

    private int id_user;
    private String nom;
    private String prenom;
    private String email;
    private String mot_de_passe;
    private int telephonne;  // Corrigé "telephonne" en "telephone"
    private String photo_profil;
    private LocalDateTime dateCreation;
    private Role role;

    public User(int id,String nom, String prenom, String email, String mot_de_passe, int telephonne, String photo_profil, Role role) {
        this.id_user = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mot_de_passe = mot_de_passe;
        this.telephonne = telephonne;
        this.photo_profil = photo_profil;
        this.role = role;
    }
    public User(String nom, String prenom, String email, String mot_de_passe, int telephonne, String photo_profil, Role role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mot_de_passe = mot_de_passe;
        this.telephonne = telephonne;
        this.photo_profil = photo_profil;
        this.role = role;
    }
    // ✅ Ajouter un constructeur qui prend uniquement `id_user`
    public User(int id_user) {
        this.id_user = id_user;
    }



//    // ✅ Constructeur sans ID (pour création d'utilisateur)
//    public User(String nom, String prenom, String email, String mot_de_passe, int telephonne, String photo_profil, Role role) {
//        this(0, nom, prenom, email, mot_de_passe, telephonne, photo_profil, role, LocalDateTime.now());
//    }
//
    // ✅ Constructeur simplifié pour récupération utilisateur (sans mot de passe)
//    public User( String nom, String prenom, String email) {
//        this( nom, prenom, email );
//    }

    // ✅ Constructeur spécial sans téléphone ni photo
    public User(String nom, String prenom, String email, String mot_de_passe, Role role) {
        this(nom, prenom, email, mot_de_passe, 0, null, role);
    }

    public User() {}

    // ✅ Getters et Setters
    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

    public int getTelephonne() {
        return telephonne;
    }

    public void setTelephonne(int telephonne) {
        this.telephonne = telephonne;
    }

    public String getPhoto_profil() {
        return photo_profil;
    }

    public void setPhoto_profil(String photo_profil) {
        this.photo_profil = photo_profil;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    @Override
    public String toString() {
        return "User{" +
                "id_user=" + id_user +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone=" + telephonne +
                ", photo_profil='" + photo_profil + '\'' +
                ", dateCreation=" + dateCreation +
                ", role=" + role +
                '}';
    }
}

package tn.esprit.models.reclamation;

import java.util.Objects;

public class reclamations {

    private int id;
    private String email;
    private categories categorie; // Utilisation de l'objet `categories` au lieu de `int categorieId`
    private String sujet;
    private String statu;
    private String description;
    private String date_creation;
    private int user_id;

    // Constructeur avec ID
    public reclamations(int id, String email, categories categorie, String sujet, String statu, String description, String date_creation, int user_id) {
        this.id = id;
        this.email = email;
        this.categorie = categorie;
        this.sujet = sujet;
        this.statu = statu;
        this.description = description;
        this.date_creation = date_creation;
        this.user_id = user_id;
    }

    // Constructeur sans ID (pour les insertions)
    public reclamations(String email, categories categorie, String sujet, String statu, String description, String date_creation, int user_id) {
        this.email = email;
        this.categorie = categorie;
        this.sujet = sujet;
        this.statu = statu;
        this.description = description;
        this.date_creation = date_creation;
        this.user_id = user_id;
    }

    public reclamations(int user_id) {

        this.user_id = user_id;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public categories getCategorie() {
        return categorie;
    }

    public void setCategorie(categories categorie) {
        this.categorie = categorie;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    public int getUser_id() { return user_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        reclamations that = (reclamations) o;
        return id == that.id && Objects.equals(email, that.email) &&
                Objects.equals(categorie, that.categorie) &&
                Objects.equals(sujet, that.sujet) &&
                Objects.equals(statu, that.statu) &&
                Objects.equals(description, that.description) &&
                Objects.equals(date_creation, that.date_creation) &&
                Objects.equals(user_id, that.user_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, categorie, sujet, statu, description, date_creation, user_id);
    }

    @Override
    public String toString() {
        return "reclamations{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", categorie=" + categorie +
                ", sujet='" + sujet + '\'' +
                ", statu='" + statu + '\'' +
                ", description='" + description + '\'' +
                ", date_creation='" + date_creation + '\'' +
                ", id_user=" + user_id +
                '}';
    }
}

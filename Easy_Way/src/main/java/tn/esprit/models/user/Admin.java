package tn.esprit.models.user;

public class Admin extends User {
    private int id_admin;

    public Admin() {
        super();
    }

    public Admin(int id_admin) {
        this.id_admin = id_admin;
    }

    public Admin(int id_user, String nom, String prenom, String email, String mot_de_passe, int telephonne, String photo_profil) {
        setId_user(id_user);
        setNom(nom);
        setPrenom(prenom);
        setEmail(email);
        setMot_de_passe(mot_de_passe);
        setTelephonne(telephonne);
        setPhoto_profil(photo_profil);
    }
//    public Admin(String nom, String prenom, String email, String mot_de_passe ,int telephonne  , String photo_profil) {
//        super( nom, prenom, email, mot_de_passe , telephonne , photo_profil );
//    }
//    public Admin(int id_admin ,String nom, String prenom, String email, String mot_de_passe) {
//        super( nom, prenom, email, mot_de_passe);
//        this.id_admin=id_admin;
//    }

    public int getId_admin() {
        return id_admin;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    @Override
    public String toString() {
        return "Admin{" +
                //"id_user=" + id_user +
                " nom='" + getNom() + '\'' +
                ", prenom='" + getPrenom() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", mot_de_passe='" + getMot_de_passe() + '\'' +
                ", telephonne=" + getTelephonne() +
                ", photo_profil='" + getPhoto_profil() + '\'' +
                '}';
    }
}

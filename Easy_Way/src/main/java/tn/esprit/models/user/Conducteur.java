package tn.esprit.models.user;

public class Conducteur extends User {
    private int id_conducteur ;
    private String numero_permis;
    private String experience;


    public Conducteur(){}

    public Conducteur(int id_conducteur){
        this.id_conducteur=id_conducteur;
    }

    public Conducteur(int id_user, String nom, String prenom, String email, String mot_de_passe, int telephonne, String photo_profil, String numero_permis, String experience) {
        try {
            System.out.println("Création d'un conducteur avec ID : " + id_user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setId_user(id_user);
        setNom(nom);
        setPrenom(prenom);
        setEmail(email);
        setMot_de_passe(mot_de_passe);
        setTelephonne(telephonne);
        setPhoto_profil(photo_profil);
        this.numero_permis = numero_permis;
        this.experience = experience;
    }
    public Conducteur(int id_user, String numero_permis, String experience) {
        setId_user(id_user);
        this.numero_permis = numero_permis;
        this.experience = experience;
    }




    public Conducteur(String nom, String prenom, String email, String mot_de_passe, int telephonne, String photo_profil, String numero_permis, String experience) {
        super(nom, prenom, email, mot_de_passe, telephonne, photo_profil, Role.Conducteur); // Ajout du rôle
        this.numero_permis = numero_permis;
        this.experience = experience;
    }



    public int getId_conducteur() {
        return id_conducteur;
    }

    public void setId_conducteur(int id_conducteur) {
        this.id_conducteur = id_conducteur;
    }

    public String getNumero_permis() {
        return numero_permis;
    }

    public void setNumero_permis(String numero_permis) {
        this.numero_permis = numero_permis;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "Conducteur{" +
                ", nom='" + getNom() + '\'' +
                ", prenom='" + getPrenom() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", mot_de_passe='" + getMot_de_passe() + '\'' +
                ", telephonne='" + getTelephonne() + '\'' +
               // ", date_creation_compte=" + getDate_creation_compte() +
                ", photo_profil='" + getPhoto_profil() + '\'' +
                ", numero_permis='" + numero_permis + '\'' +
                ", experience='" + experience + '\'' +
                "}\n";
    }
}


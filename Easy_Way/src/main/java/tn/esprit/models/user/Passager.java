package tn.esprit.models.user;

public class Passager extends User {
    private int nbTrajetsEffectues;

    public Passager(String nom, String prenom, String email, String mot_de_passe, int telephonne, String photo_profil) {
        super(nom, prenom, email, mot_de_passe, telephonne, photo_profil, Role.Passager);
        this.nbTrajetsEffectues = 0;
    }

    public Passager(String nom, String prenom, String email, String mot_de_passe) {
        super(nom, prenom, email, mot_de_passe, Role.Passager);
        this.nbTrajetsEffectues = 0;
    }


    public Passager(int id_user, String nom, String prenom, String email, String mot_de_passe, int telephonne, String photo_profil, int nbTrajetsEffectues) {
        super(nom, prenom, email, mot_de_passe, telephonne, photo_profil, Role.Passager);
        super.setId_user(id_user); // Assigner l'ID utilisateur
        this.nbTrajetsEffectues = nbTrajetsEffectues;
    }
    // ✅ Ajout du constructeur avec seulement l'ID
    public Passager(int id_user) {
        super(id_user);  // Si `User` a un champ `id_user`
    }
    // ✅ Ajouter un getter
    public int getId_user() {
        return super.getId_user();  // Hérité de `User`
    }


    public int getNbTrajetsEffectues() {
        return nbTrajetsEffectues;
    }

    public void setNbTrajetsEffectues(int nbTrajetsEffectues) {
        this.nbTrajetsEffectues = nbTrajetsEffectues;
    }

    @Override
    public String toString() {
        return "Passager{" +super.toString()+
                "nbTrajetsEffectues=" + nbTrajetsEffectues +
                '}';
    }
}


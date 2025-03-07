package tn.esprit.services.user;

import tn.esprit.interfaces.IService;
import tn.esprit.models.user.Passager;
import tn.esprit.util.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePassager implements IService<Passager> {
    private Connection cnx;

    public ServicePassager() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    public void add(Passager passager) {
        Connection cnx = null;
        PreparedStatement pstmPassager = null;
        ResultSet rs = null;

        try {
            // 🔹 Récupération de la connexion sans la fermer après
            cnx = MyDataBase.getInstance().getCnx();

            // Vérifier si un passager avec cet email existe déjà dans la table 'passager'
            String checkPassagerQuery = "SELECT COUNT(*) FROM passager WHERE email = ?";
            PreparedStatement checkStmt = cnx.prepareStatement(checkPassagerQuery);
            checkStmt.setString(1, passager.getEmail());
            rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Si un passager avec cet email existe déjà
                System.out.println("❌ Passager avec cet email déjà existant.");
                return; // Terminer la méthode si le passager existe déjà
            }

            // 1️⃣ Insérer directement dans la table 'passager'
            String queryPassager = "INSERT INTO passager (nom, prenom, email, mot_de_passe, telephonne, photo_profil, nbTrajetsEffectues) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmPassager = cnx.prepareStatement(queryPassager);
            pstmPassager.setString(1, passager.getNom());
            pstmPassager.setString(2, passager.getPrenom());
            pstmPassager.setString(3, passager.getEmail());
            pstmPassager.setString(4, passager.getMot_de_passe());
            pstmPassager.setInt(5, passager.getTelephonne());
            pstmPassager.setString(6, passager.getPhoto_profil());
            pstmPassager.setInt(7, 0); // Par défaut, nbTrajetsEffectues = 0

            int rowsInserted = pstmPassager.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Passager ajouté avec succès !");
            } else {
                System.out.println("⚠️ Erreur lors de l'ajout du passager.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Erreur SQL : " + e.getMessage());
        } finally {
            try {
                // ❗ On ferme uniquement les PreparedStatement et ResultSet
                if (rs != null) rs.close();
                if (pstmPassager != null) pstmPassager.close();
                // ⚠️ NE PAS FERMER cnx ici !
            } catch (SQLException e) {
                System.out.println("⚠️ Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
        }
    }


    @Override
    public List<Passager> getAll() {
        List<Passager> passagers = new ArrayList<>();
        String query = "SELECT u.*, p.nbTrajetsEffectues FROM user u JOIN passager p ON u.id_user = p.id_user";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                Passager p = new Passager(
                        rs.getInt("id_user"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getInt("telephonne"),
                        rs.getString("photo_profil"),
                        rs.getInt("nbTrajetsEffectues") // Récupération du nombre de trajets effectués
                );
                passagers.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des passagers : " + e.getMessage());
        }
        return passagers;
    }


    @Override
    public Passager getById(int id_passager) {
        String query = "SELECT p.id_passager, u.id_user, u.nom, u.prenom, u.email, u.mot_de_passe, u.telephonne, u.photo_profil, p.nbTrajetsEffectues " +
                "FROM passager p " +
                "JOIN user u ON p.id_user = u.id_user " +
                "WHERE p.id_passager = ?";

        Passager passager = null;

        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setInt(1, id_passager);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                passager = new Passager(
                        rs.getInt("id_user"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getInt("telephonne"),
                        rs.getString("photo_profil"),
                        rs.getInt("nbTrajetsEffectues") // Ajout de nbTrajetsEffectues
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du passager : " + e.getMessage());
        }

        return passager;
    }



    @Override
    public void update(Passager passager) {
        String queryUser = "UPDATE user SET nom = ?, prenom = ?, email = ?, mot_de_passe = ?, telephonne = ?, photo_profil = ? WHERE id_user = ?";
        String queryPassager = "UPDATE passager SET nom = ?, prenom = ?, email = ?, mot_de_passe = ?, telephonne = ?, photo_profil = ?, nbTrajetsEffectues = ? WHERE id_user = ?";

        try {
            // 🔹 Mise à jour des informations de l'utilisateur dans la table `user`
            PreparedStatement pstmUser = cnx.prepareStatement(queryUser);
            pstmUser.setString(1, passager.getNom());
            pstmUser.setString(2, passager.getPrenom());
            pstmUser.setString(3, passager.getEmail());
            pstmUser.setString(4, passager.getMot_de_passe());
            pstmUser.setInt(5, passager.getTelephonne());
            pstmUser.setString(6, passager.getPhoto_profil());
            pstmUser.setInt(7, passager.getId_user());

            int rowsUpdatedUser = pstmUser.executeUpdate();
            pstmUser.close(); // ✅ Fermeture du statement

            if (rowsUpdatedUser > 0) {
                System.out.println("✅ L'utilisateur avec ID " + passager.getId_user() + " a été mis à jour avec succès.");
            } else {
                System.out.println("⚠️ Aucun utilisateur trouvé avec l'ID " + passager.getId_user());
            }

            // 🔹 Mise à jour des informations spécifiques au passager dans la table `passager`
            PreparedStatement pstmPassager = cnx.prepareStatement(queryPassager);
            pstmPassager.setString(1, passager.getNom());
            pstmPassager.setString(2, passager.getPrenom());
            pstmPassager.setString(3, passager.getEmail());
            pstmPassager.setString(4, passager.getMot_de_passe());
            pstmPassager.setInt(5, passager.getTelephonne());
            pstmPassager.setString(6, passager.getPhoto_profil());
            pstmPassager.setInt(7, passager.getNbTrajetsEffectues());
            pstmPassager.setInt(8, passager.getId_user());

            int rowsUpdatedPassager = pstmPassager.executeUpdate();
            pstmPassager.close(); // ✅ Fermeture du statement

            if (rowsUpdatedPassager > 0) {
                System.out.println("✅ Le passager avec ID utilisateur " + passager.getId_user() + " a été mis à jour avec succès.");
            } else {
                System.out.println("⚠️ Aucun passager trouvé avec l'ID utilisateur " + passager.getId_user());
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour du passager : " + e.getMessage());
        }
    }



    @Override
    public void delete(Passager passager) {
        String queryPassager = "DELETE FROM passager WHERE id_user = ?";
        String queryUser = "DELETE FROM user WHERE id_user = ?";

        try {
            // 🔹 Suppression du passager
            PreparedStatement pstmPassager = cnx.prepareStatement(queryPassager);
            pstmPassager.setInt(1, passager.getId_user());
            pstmPassager.executeUpdate();

            // 🔹 Suppression de l'utilisateur correspondant
            PreparedStatement pstmUser = cnx.prepareStatement(queryUser);
            pstmUser.setInt(1, passager.getId_user());
            int rowsDeleted = pstmUser.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("L'utilisateur avec ID " + passager.getId_user() + " a été supprimé avec succès.");
            } else {
                System.out.println("Aucun utilisateur trouvé avec l'ID " + passager.getId_user());
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }


}
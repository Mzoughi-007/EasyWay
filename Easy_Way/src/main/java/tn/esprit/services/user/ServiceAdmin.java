package tn.esprit.services.user;

import tn.esprit.interfaces.IService;
import tn.esprit.models.user.Admin;
import tn.esprit.util.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceAdmin implements IService<Admin> {
    private Connection cnx;

    public ServiceAdmin() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    public void add(Admin admin) {
        try {
            // 1️⃣ Insérer dans la table 'user' pour ajouter un utilisateur
            String queryUser = "INSERT INTO user (nom, prenom, email, mot_de_passe, telephonne, photo_profil) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmUser = cnx.prepareStatement(queryUser, Statement.RETURN_GENERATED_KEYS);
            pstmUser.setString(1, admin.getNom());
            pstmUser.setString(2, admin.getPrenom());
            pstmUser.setString(3, admin.getEmail());
            pstmUser.setString(4, admin.getMot_de_passe());
            pstmUser.setInt(5, admin.getTelephonne());
            pstmUser.setString(6, admin.getPhoto_profil());

            // Exécution de la requête et récupération de l'ID généré
            int affectedRows = pstmUser.executeUpdate();
            if (affectedRows > 0) {
                // Récupérer l'ID généré de l'utilisateur
                ResultSet generatedKeys = pstmUser.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id_user = generatedKeys.getInt(1);  // ID généré pour l'utilisateur

                    // 2️⃣ Insérer dans la table 'admin' avec l'ID de l'utilisateur
                    String queryAdmin = "INSERT INTO admin (id_user , nom , prenom , email, mot_de_passe , telephonne, photo_profil) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmAdmin = cnx.prepareStatement(queryAdmin);
                    pstmAdmin.setInt(1, id_user);  // ID de l'utilisateur inséré dans la table 'admin'
                    pstmAdmin.setString(2, admin.getNom());
                    pstmAdmin.setString(3, admin.getPrenom());
                    pstmAdmin.setString(4, admin.getEmail());
                    pstmAdmin.setString(5, admin.getMot_de_passe());
                    pstmAdmin.setInt(6, admin.getTelephonne());
                    pstmAdmin.setString(7, admin.getPhoto_profil());

                    int rowsInserted = pstmAdmin.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Admin ajouté avec succès !");
                    } else {
                        System.out.println("Erreur lors de l'ajout de l'admin dans la table admin.");
                    }
                }
            } else {
                System.out.println("Aucune ligne insérée dans la table user.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'admin : " + e.getMessage());
        }
    }


    @Override
    public List<Admin> getAll() {
        List<Admin> admins = new ArrayList<>();
        String qry = "SELECT * FROM `admin`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Admin a = new Admin();
                       // a.setId_admin(rs.getInt("id_admin"));
                        a.setNom(rs.getString("nom"));
                        a.setPrenom(rs.getString("prenom"));
                        a.setMot_de_passe(rs.getString("mot_de_passe"));
                        a.setTelephonne(rs.getInt("telephonne"));
                        a.setPhoto_profil(rs.getString("photo_profil"));

                admins.add(a);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des admins : " + e.getMessage());
        }
        return admins;
    }

    @Override
    public Admin getById(int id_admin) {
        String query = "SELECT a.id_admin, u.id_user, u.nom, u.prenom, u.email, u.mot_de_passe, u.telephonne, u.photo_profil " +
                "FROM admin a " +
                "JOIN user u ON a.id_user = u.id_user " +
                "WHERE a.id_admin = ?";

        Admin admin = null;

        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setInt(1, id_admin);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                admin = new Admin(
                        rs.getInt("id_admin"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getInt("telephonne"),
                        rs.getString("photo_profil")
                );
                System.out.println("✅ Admin trouvé : " + admin.getNom() + " " + admin.getPrenom());
            } else {
                System.out.println("❌ Aucun admin trouvé avec l'ID " + id_admin);
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Erreur SQL : " + e.getMessage());
        }
        return admin;
    }


    @Override
    public void update(Admin admin) {
        String queryUser = "UPDATE user SET nom = ?, prenom = ?, email = ?, mot_de_passe = ?, telephonne = ?, photo_profil = ? WHERE id_user = ?";

        try {
            // Mise à jour des informations de l'utilisateur
            PreparedStatement pstmUser = cnx.prepareStatement(queryUser);
            pstmUser.setString(1, admin.getNom());
            pstmUser.setString(2, admin.getPrenom());
            pstmUser.setString(3, admin.getEmail());
            pstmUser.setString(4, admin.getMot_de_passe());
            pstmUser.setInt(5, admin.getTelephonne());
            pstmUser.setString(6, admin.getPhoto_profil());
            pstmUser.setInt(7, admin.getId_user());

            int rowsUpdatedUser = pstmUser.executeUpdate();
            if (rowsUpdatedUser > 0) {
                System.out.println("L'utilisateur avec ID " + admin.getId_user() + " a été mis à jour avec succès.");
            } else {
                System.out.println("Aucun utilisateur trouvé avec l'ID " + admin.getId_user());
            }

            String queryAdmin = "UPDATE admin SET nom = ?, prenom = ?, email = ?, mot_de_passe = ? , telephonne= ? , photo_profil = ? WHERE id_user = ?";
            PreparedStatement pstmAdmin = cnx.prepareStatement(queryAdmin);

            pstmAdmin.setString(1, admin.getNom());
            pstmAdmin.setString(2, admin.getPrenom());
            pstmAdmin.setString(3, admin.getEmail());
            pstmAdmin.setString(4, admin.getMot_de_passe());
            pstmAdmin.setInt(5, admin.getTelephonne());
            pstmAdmin.setString(6, admin.getPhoto_profil());

            pstmAdmin.setInt(7, admin.getId_user());

            int rowsUpdatedAdmin = pstmAdmin.executeUpdate();
            if (rowsUpdatedAdmin > 0) {
                System.out.println("L'admin avec ID utilisateur " + admin.getId_user() + " a été mis à jour avec succès.");
            } else {
                System.out.println("Aucun admin trouvé avec l'ID utilisateur " + admin.getId_user());
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'admin : " + e.getMessage());
        }
    }



    @Override
    public void delete(Admin admin) {
        String query = "DELETE FROM admin WHERE id_admin = ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setInt(1, admin.getId_admin());
            int rowsDeleted = pstm.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Admin supprimé avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'admin : " + e.getMessage());
        }
    }
}


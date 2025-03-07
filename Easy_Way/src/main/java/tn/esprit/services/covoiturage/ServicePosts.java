package tn.esprit.services.covoiturage;

import tn.esprit.interfaces.IService;
import tn.esprit.models.covoiturage.Posts;
import tn.esprit.util.MyDataBase;
import tn.esprit.util.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePosts implements IService<Posts> {
    private Connection cnx;

    public ServicePosts() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Posts posts) {
        String qry = "INSERT INTO posts (id_user, ville_depart, ville_arrivee, date, message, prix, nombreDePlaces) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);

            int idUserConnecte = SessionManager.getInstance().getId_user();
            if (idUserConnecte == 0) {
                System.out.println("Erreur : Aucun utilisateur connecté.");
                return;
            }

            pstm.setInt(1, idUserConnecte);
            pstm.setString(2, posts.getVilleDepart());
            pstm.setString(3, posts.getVilleArrivee());
            pstm.setDate(4, posts.getDate());
            pstm.setString(5, posts.getMessage());
            pstm.setDouble(6, posts.getPrix());
            pstm.setInt(7, posts.getNombreDePlaces());

            pstm.executeUpdate();

            ResultSet rs = pstm.getGeneratedKeys();
            if (rs.next()) {
                posts.setId_post(rs.getInt(1));
            }

            System.out.println("Post ajouté avec ID : " + posts.getId_post());

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du post : " + e.getMessage());
        }
    }

    @Override
    public List<Posts> getAll() {
        List<Posts> postsList = new ArrayList<>();
        String qry = "SELECT * FROM posts";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Posts p = new Posts();
                p.setId_post(rs.getInt("id_post"));
                p.setId_user(rs.getInt("id_user"));
                p.setVilleDepart(rs.getString("ville_depart"));
                p.setVilleArrivee(rs.getString("ville_arrivee"));
                p.setDate(rs.getDate("date"));
                p.setMessage(rs.getString("message"));
                p.setPrix(rs.getDouble("prix"));
                p.setNombreDePlaces(rs.getInt("nombreDePlaces"));
                postsList.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des posts : " + e.getMessage());
        }
        return postsList;
    }

    @Override
    public Posts getById(int id) {
        return null;
    }

    @Override
    public void update(Posts posts) {
        String checkQuery = "SELECT id_user FROM posts WHERE id_post = ?";
        String updateQuery = "UPDATE posts SET ville_depart = ?, ville_arrivee = ?, date = ?, message = ?, prix = ?, nombreDePlaces = ? WHERE id_post = ? AND id_user = ?";

        try {
            PreparedStatement checkStmt = cnx.prepareStatement(checkQuery);
            checkStmt.setInt(1, posts.getId_post());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int ownerId = rs.getInt("id_user");
                int idUserConnecte = SessionManager.getInstance().getId_user();

                if (ownerId != idUserConnecte) {
                    System.out.println("Erreur : Ce post appartient à un autre utilisateur.");
                    return;
                }

                PreparedStatement pstm = cnx.prepareStatement(updateQuery);
                pstm.setString(1, posts.getVilleDepart());
                pstm.setString(2, posts.getVilleArrivee());
                pstm.setDate(3, posts.getDate());
                pstm.setString(4, posts.getMessage());
                pstm.setDouble(5, posts.getPrix());
                pstm.setInt(6, posts.getNombreDePlaces());
                pstm.setInt(7, posts.getId_post());
                pstm.setInt(8, idUserConnecte);

                int rowsUpdated = pstm.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Mise à jour réussie !");
                } else {
                    System.out.println("Erreur : Impossible de modifier le post.");
                }
            } else {
                System.out.println("Erreur : Post introuvable.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du post : " + e.getMessage());
        }
    }

    @Override
    public void delete(Posts posts) {
        String checkQuery = "SELECT id_user FROM posts WHERE id_post = ?";
        String deleteQuery = "DELETE FROM posts WHERE id_post = ? AND id_user = ?";

        try {
            PreparedStatement checkStmt = cnx.prepareStatement(checkQuery);
            checkStmt.setInt(1, posts.getId_post());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int ownerId = rs.getInt("id_user");
                int idUserConnecte = SessionManager.getInstance().getId_user();

                if (ownerId != idUserConnecte) {
                    System.out.println("Erreur : Vous ne pouvez pas supprimer ce post.");
                    return;
                }

                PreparedStatement pstm = cnx.prepareStatement(deleteQuery);
                pstm.setInt(1, posts.getId_post());
                pstm.setInt(2, idUserConnecte);
                int rowsDeleted = pstm.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Post supprimé avec succès !");
                } else {
                    System.out.println("Erreur : Impossible de supprimer le post.");
                }
            } else {
                System.out.println("Erreur : Post introuvable.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du post : " + e.getMessage());
        }
    }

    public Posts getPostById(int id_post) {
        String qry = "SELECT * FROM posts WHERE id_post = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id_post);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return new Posts(
                        rs.getInt("id_post"),
                        rs.getInt("id_user"),
                        rs.getString("ville_depart"),
                        rs.getString("ville_arrivee"),
                        rs.getDate("date"),
                        rs.getString("message"),
                        rs.getDouble("prix"),
                        rs.getInt("nombreDePlaces")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du post : " + e.getMessage());
        }
        return null;
    }

    public boolean updateNombrePlaces(int id_post, int nouveauNombre) {
        String query = "UPDATE posts SET nombreDePlaces = ? WHERE id_post = ?";
        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, nouveauNombre);
            stmt.setInt(2, id_post);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("✅ Nombre de places mis à jour avec succès !");
                return true;
            } else {
                System.out.println("❌ Erreur : Post introuvable ou mise à jour échouée.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour du nombre de places : " + e.getMessage());
            return false;
        }
    }
}

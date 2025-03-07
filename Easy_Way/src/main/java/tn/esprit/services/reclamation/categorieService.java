package tn.esprit.services.reclamation;

import tn.esprit.interfaces.IService;
import tn.esprit.models.reclamation.categories;
import tn.esprit.util.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class categorieService implements IService<categories> {

    private Connection connection = MyDataBase.getInstance().getCnx();

    @Override
    public void add(categories categorie) {
        String req = "INSERT INTO categorie (type) VALUES (?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, categorie.getType());
            pst.executeUpdate();
            System.out.println("Catégorie ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(categories categorie) {
        String req = "UPDATE categorie SET type=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, categorie.getType());
            pst.setInt(2, categorie.getId());
            pst.executeUpdate();
            System.out.println("Catégorie modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(categories categorie) {
        String req = "DELETE FROM categorie WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, categorie.getId());
            pst.executeUpdate();
            System.out.println("Catégorie supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<categories> getAll() {
        List<categories> categoriesList = new ArrayList<>();
        String req = "SELECT * FROM categorie";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                categoriesList.add(new categories(
                        rs.getInt("id"),
                        rs.getString("type")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return categoriesList;
    }

    @Override
    public categories getById(int id) {
        String req = "SELECT * FROM categorie WHERE id = ?";
        categories categorie = null;
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                categorie = new categories(
                        rs.getInt("id"),
                        rs.getString("type")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return categorie;
    }
}

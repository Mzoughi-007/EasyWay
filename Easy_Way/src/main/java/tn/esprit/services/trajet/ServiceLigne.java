package tn.esprit.services.trajet;

import tn.esprit.interfaces.trajet.IService;
import tn.esprit.models.trajet.Ligne;
import tn.esprit.util.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ServiceLigne implements IService<Ligne> {
    private Connection cnx;
    public ServiceLigne() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Ligne ligne) {
        String query = "INSERT INTO `ligne` (`depart`, `arret`, `type`, `admin_id`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {  // Request generated keys
            pstm.setString(1, ligne.getDep());
            pstm.setString(2, ligne.getArr());
            pstm.setString(3, ligne.getType());
            pstm.setInt(4, ligne.getAdmin_id());

            int affectedRows = pstm.executeUpdate();

            // After executing the update, retrieve the generated key
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ligne.setId(generatedKeys.getInt(1));  // Set the generated ID to the Ligne object
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public List<Ligne> getAll() {
        ArrayList<Ligne> lignes = new ArrayList<>();
        String qry ="SELECT * FROM `ligne`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet  rs =stm.executeQuery(qry);
            while (rs.next()){
                Ligne l = new Ligne();
                l.setId(rs.getInt("id"));
                l.setDep(rs.getString("depart"));
                l.setArr(rs.getString("arret"));
                l.setType(rs.getString("type"));
                l.setAdmin_id(rs.getInt("admin_id"));
                lignes.add(l);
            }
            System.out.println(lignes);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        return lignes;
    }

    @Override
    public void update(Ligne ligne) {
        String query = "UPDATE `ligne` SET `depart` = ?, `arret` = ?, `type` = ?, `admin_id` = ? WHERE `id` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setString(1, ligne.getDep());
            pstm.setString(2, ligne.getArr());
            pstm.setString(3, ligne.getType());
            pstm.setInt(4, ligne.getAdmin_id());
            pstm.setInt(5, ligne.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Ligne ligne) {
        String query = "DELETE FROM `ligne` WHERE `id` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setInt(1, ligne.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public Ligne getById(int id) {
        System.out.println("non implémenté");
        return null;
    }
    public int getIdByLigne(Ligne ligne) {
        int ligneId = -1; // Default value to indicate that the Ligne's ID wasn't found.
        String query = "SELECT `id` FROM `ligne` WHERE `depart` = ? AND `arrivee` = ? AND `type` = ? AND `id_admin` = ?";

        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setString(1, ligne.getDep());
            pstm.setString(2, ligne.getArr());
            pstm.setString(3, ligne.getType());
            pstm.setInt(4, ligne.getAdmin_id());

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                ligneId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ligneId;
    }

}
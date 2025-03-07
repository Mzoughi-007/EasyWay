package tn.esprit.services.trajet;

import tn.esprit.interfaces.trajet.IService;
import tn.esprit.models.trajet.Trajet;
import tn.esprit.util.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTrajet implements IService<Trajet> {
    private Connection cnx;
    public ServiceTrajet() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Trajet trajet) {
        String query = "INSERT INTO `trajet` (`duree`, `distance`, `heure_depart`, `heure_arrive`, `depart`, `arret`, `etat`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setString(1, trajet.getDuree());
            pstm.setInt(2, trajet.getDistance());
            pstm.setString(3, trajet.getHeure_depart());
            pstm.setString(4, trajet.getHeure_arrive());
            pstm.setString(5, trajet.getDepart());
            pstm.setString(6, trajet.getArret());
            pstm.setString(7, trajet.getEtat());
            pstm.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Trajet> getAll() {
        ArrayList<Trajet> trajets = new ArrayList<>();
        String qry ="SELECT * FROM `trajet`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet  rs =stm.executeQuery(qry);
            System.out.println("Connected to database: " + cnx.getCatalog());

            while (rs.next()){
                Trajet s = new Trajet();
                s.setId(rs.getInt("id"));
                s.setDuree(rs.getString("duree"));
                s.setDistance(rs.getInt("distance"));
                s.setHeure_depart(rs.getString("heure_depart"));
                s.setHeure_arrive(rs.getString("heure_arrive"));
                s.setDepart(rs.getString("depart"));
                s.setArret(rs.getString("arret"));
                s.setEtat(rs.getString("etat"));
                trajets.add(s);
            }
            System.out.println(trajets);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        return trajets;
    }



    @Override
    public void update(Trajet trajet) {
        String query = "UPDATE `trajet` SET `duree` = ?, `distance` = ?, `heure_depart` = ?, `heure_arrive` = ? , `depart` = ?, `arret` = ?, `etat` = ? WHERE `id` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setString(1, trajet.getDuree());
            pstm.setInt(2, trajet.getDistance());
            pstm.setString(3, trajet.getHeure_depart());
            pstm.setString(4, trajet.getHeure_arrive());
            pstm.setString(5, trajet.getDepart());
            pstm.setString(6, trajet.getArret());
            pstm.setString(7, trajet.getEtat());
            pstm.setInt(8, trajet.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Trajet trajet) {
        String query = "DELETE FROM `trajet` WHERE `id` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setInt(1, trajet.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Trajet getById(int id) {
        System.out.println("non implémenté");
        return null;
    }

}

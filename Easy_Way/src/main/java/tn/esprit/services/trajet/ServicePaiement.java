package tn.esprit.services.trajet;


import tn.esprit.interfaces.trajet.IService;
import tn.esprit.models.trajet.Paiement;
import tn.esprit.util.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePaiement implements IService<Paiement> {

    private Connection cnx;
    public ServicePaiement() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Paiement paiement) {
        String query = "INSERT INTO `paiement` (`pay_id`,`montant`,`res_id`,`user_id`) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setString(1, paiement.getPay_id());
            pstm.setDouble(2, paiement.getMontant());
            pstm.setInt(3, paiement.getRes_id());
            pstm.setInt(4, paiement.getUser_id());
            pstm.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Paiement> getAll() {
        ArrayList<Paiement> paiements = new ArrayList<>();
        String qry ="SELECT * FROM `paiement`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet  rs =stm.executeQuery(qry);
            while (rs.next()){
                Paiement l = new Paiement();
                l.setId(rs.getInt("id"));
                l.setPay_id(rs.getString("pay_id"));
                l.setMontant(rs.getDouble("montant"));
                l.setRes_id(rs.getInt("res_id"));
                l.setUser_id(rs.getInt("user_id"));
                paiements.add(l);
            }
            System.out.println(paiements);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        return paiements;
    }

    @Override
    public void update(Paiement paiement) {
        String query = "UPDATE `paiement` SET `pay_id` = ?, `montant` = ?, `res_id` = ?, `user_id` = ? WHERE `id` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setString(1, paiement.getPay_id());
            pstm.setDouble(2, paiement.getMontant());
            pstm.setInt(3, paiement.getRes_id());
            pstm.setInt(4, paiement.getUser_id());
            pstm.setInt(5, paiement.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Paiement paiement) {
        String query = "DELETE FROM `paiement` WHERE `id` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setInt(1, paiement.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public Paiement getById(int id) {
        System.out.println("non implémenté");
        return null;
    }
    public String PaymentDetails(Paiement paiement) {
        return "Paiement Details: \n" +
                "ID: " + paiement.getId() + "\n" +
                "Pay ID: " + paiement.getPay_id() + "\n" +
                "Amount: " + paiement.getMontant();
    }
    public List<Integer> getReservationIds() {
        List<Integer> reservationIds = new ArrayList<>();
        String query = "SELECT res_id FROM paiement"; // Query to get reservation IDs
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                reservationIds.add(rs.getInt("res_id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservationIds;
    }


}

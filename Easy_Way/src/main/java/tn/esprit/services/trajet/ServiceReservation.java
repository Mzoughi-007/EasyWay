package tn.esprit.services.trajet;

import tn.esprit.interfaces.trajet.IService;
import tn.esprit.models.trajet.Reservation;
import tn.esprit.util.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ServiceReservation implements IService<Reservation> {
    private Connection cnx;
    public ServiceReservation() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Reservation reservation) {
        String query = "INSERT INTO reservation (`depar`,`arret`, `vehicule`, `nb`, `user_id`) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setString(1, reservation.getDepart());
            pstm.setString(2, reservation.getArret());
            pstm.setString(3, reservation.getVehicule());
            pstm.setInt(4, reservation.getNb());
            pstm.setInt(5, reservation.getUser_id());
            pstm.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Reservation> getAll() {
        ArrayList<Reservation> reservations = new ArrayList<>();
        String qry ="SELECT * FROM `reservation`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet  rs =stm.executeQuery(qry);
            while (rs.next()){
                Reservation l = new Reservation();
                l.setId(rs.getInt("id"));
                l.setDepart(rs.getString("depart"));
                l.setArret(rs.getString("arret"));
                l.setVehicule(rs.getString("vehicule"));
                l.setNb(rs.getInt("nb"));
                l.setNb(rs.getInt("user_id"));
                reservations.add(l);
            }
            System.out.println(reservations);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        return reservations;
    }

    @Override
    public void update(Reservation reservation) {
        String query = "UPDATE `reservation` SET `depart` = ?, `arret` = ?, `vehicule` = ?, `nb` = ?, `nb` = ? WHERE `id` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setString(1, reservation.getDepart());
            pstm.setString(2, reservation.getArret());
            pstm.setString(3, reservation.getVehicule());
            pstm.setInt(4, reservation.getNb());
            pstm.setInt(5, reservation.getUser_id());
            pstm.setInt(6, reservation.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Reservation reservation) {
        String query = "DELETE FROM `reservation` WHERE `id` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setInt(1, reservation.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public Reservation getById(int id) {
        String query = "SELECT * FROM `reservation` WHERE `id` = ?";
        Reservation reservation = null;
        try {
            PreparedStatement pst = cnx.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setDepart(rs.getString("depart"));
                reservation.setArret(rs.getString("arret"));
                reservation.setVehicule(rs.getString("vehicule"));
                reservation.setNb(rs.getInt("nb"));
                reservation.setNb(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservation;
    }

    public int add2(Reservation reservation) {
        String query = "INSERT INTO `reservation` (`depart`, `arret`, `vehicule`, `nb`, `user_id`) VALUES (?, ?, ?, ?, ?)";
        int reservationId = -1;  // Initialize to an invalid ID

        try {
            // Prepare the statement
            PreparedStatement pstm = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, reservation.getDepart());
            pstm.setString(2, reservation.getArret());
            pstm.setString(3, reservation.getVehicule());
            pstm.setInt(4, reservation.getNb());
            pstm.setInt(5, reservation.getUser_id());

            // Execute the update
            pstm.executeUpdate();

            // Get the generated ID (auto-incremented)
            ResultSet rs = pstm.getGeneratedKeys();
            if (rs.next()) {
                reservationId = rs.getInt(1);  // Retrieve the generated ID
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservationId;
    }
    public List<Reservation> getReservationsByIds(List<Integer> reservationIds) {
        List<Reservation> reservations = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT depart, arret, vehicule, nb FROM reservation WHERE id IN (");

        for (int i = 0; i < reservationIds.size(); i++) {
            queryBuilder.append("?");
            if (i < reservationIds.size() - 1) {
                queryBuilder.append(",");
            }
        }

        queryBuilder.append(")");

        String query = queryBuilder.toString();

        try (PreparedStatement pstm = cnx.prepareStatement(query)) {
            for (int i = 0; i < reservationIds.size(); i++) {
                pstm.setInt(i + 1, reservationIds.get(i));
            }

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setDepart(rs.getString("depart"));
                reservation.setArret(rs.getString("arret"));
                reservation.setVehicule(rs.getString("vehicule"));
                reservation.setNb(rs.getInt("nb"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving reservations: " + e.getMessage());
        }
        return reservations;
    }

}

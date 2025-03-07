package tn.esprit.services.trajet;

import tn.esprit.interfaces.trajet.IService;
import tn.esprit.models.trajet.Station;
import tn.esprit.util.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ServiceStation implements IService<Station> {
    private Connection cnx;
    public ServiceStation() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Station station) {
        String query = "INSERT INTO `station` (`nom`, `localisation`, `id_ligne`, `id_admin`) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setString(1, station.getNom());
            pstm.setString(2, station.getLocalisation());
            pstm.setInt(3, station.getId_ligne());
            pstm.setInt(4, station.getId_admin());
            pstm.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Station> getAll() {
        ArrayList<Station> stations = new ArrayList<>();
        String qry ="SELECT * FROM `station`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet  rs =stm.executeQuery(qry);
            while (rs.next()){
                Station s = new Station();
                s.setId(rs.getInt("id"));
                s.setNom(rs.getString("nom"));
                s.setLocalisation(rs.getString("localisation"));
                s.setId_admin(rs.getInt("id_admin"));
                s.setId_ligne(rs.getInt("id_ligne"));
                stations.add(s);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        System.out.println(stations);
        return stations;
    }



    @Override
    public void update(Station station) {
        String query = "UPDATE `station` SET `nom` = ?, `localisation` = ?, `id_ligne` = ?, `id_admin` = ? WHERE `id` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setString(1, station.getNom());
            pstm.setString(2, station.getLocalisation());
            pstm.setInt(3, station.getId_ligne());
            pstm.setInt(4, station.getId_admin());
            pstm.setInt(5, station.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Station station) {
        String query = "DELETE FROM `station` WHERE `id` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setInt(1, station.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public Station getById(int id) {
        System.out.println("non implémenté");
        return null;
    }
    public int getStationIdByIndex(int ligneId, int index) {
        int stationId = -1;  // Default value indicating no station found
        String query = "SELECT `id` FROM `station` WHERE `id_ligne` = ? ORDER BY `id` LIMIT ?, 1";
        try (PreparedStatement pstm = cnx.prepareStatement(query)) {
            pstm.setInt(1, ligneId);
            pstm.setInt(2, index);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    stationId = rs.getInt("id");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while fetching station by index: " + e.getMessage());
        }

        return stationId;
    }



}

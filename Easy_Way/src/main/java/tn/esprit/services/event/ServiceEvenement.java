package tn.esprit.services.event;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import tn.esprit.interfaces.IEvent;
import tn.esprit.models.Events.Evenements;
import tn.esprit.models.Events.StatusEvenement;
import tn.esprit.models.Events.TypeEvenement;
import tn.esprit.util.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement implements IEvent<Evenements> {
    private Connection cnx;

    public ServiceEvenement() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void add(Evenements evenements) {
        String query = "INSERT INTO `evenement`(`type`, `description`, `date_debut`, `date_fin`, `ligne_affectee`, `statut`, `id_createur`) " +
                "VALUES (?,?,?,?,?,?,?)";
        List<Integer> userIds = null;
        List<Integer> phoneNumbers = null;
        try (Connection conn = MyDataBase.getInstance().getCnx();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, evenements.getType_evenement().name());//type event
            ps.setString(2, evenements.getDescription());// description
            ps.setDate(3, new java.sql.Date(evenements.getDate_debut().getTime()));// date debut
            ps.setDate(4, new java.sql.Date(evenements.getDate_fin().getTime()));// date fin
            ps.setInt(5, evenements.getId_ligne_affectee());// id ligne affected
            ps.setString(6, evenements.getStatus_evenement().name()); // status event
            ps.setInt(7, evenements.getId_createur());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating event failed, no rows affected.");
            }

            // api messaging
            userIds = getUsersIdByLine(
                    getLigneInfo(evenements.getId_ligne_affectee()).split(" - ")[0],
                    getLigneInfo(evenements.getId_ligne_affectee()).split(" - ")[1],
                    conn);
            phoneNumbers = getPhoneNumbers(userIds, conn);


            for (Integer phoneNumber : phoneNumbers) {
                VonageClient client = VonageClient.builder().apiKey("c8c34ce3")
                        .apiSecret("FChHt3T5SB9XvulI")
                        .build();
                TextMessage message = new TextMessage("Easy Way",
                        "+216"+phoneNumber,  // Replace with the recipient's number
                        "\uD83D\uDEA8 A new event has been created on your reserved line!\n" +
                        "Type: "+evenements.getType_evenement().toString()+"\n"+
                        "Description: "+evenements.getDescription()+"\n");

                SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);
                for (SmsSubmissionResponseMessage messageResponse : response.getMessages()) {
                    System.out.println("📩 SMS Sent! Status: " + messageResponse.getStatus());
                }
            }


             //twilio api
            // Send SMS notifications
            ServiceTwilio twilioService = new ServiceTwilio();
            for (int phone : phoneNumbers) {
                String message = "⚠️ Un nouvel événement a été créé pour votre ligne ! " +
                        "\nType: " + evenements.getType_evenement() +
                        "\nDescription: " + evenements.getDescription()+"\n";
                twilioService.sendSMS("+216"+phone, message);
            }

            System.out.println("✅ SMS envoyés à tous les utilisateurs concernés.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public List<Evenements> getAll() {
        ArrayList<Evenements> events = new ArrayList<>();
        String query = "SELECT * FROM `evenement`";
        try{
            Statement stm = cnx.createStatement();
            ResultSet rs =stm.executeQuery(query);
            while (rs.next()) {
                Evenements event = new Evenements();
                event.setId_event(rs.getInt("id_evenement"));
                event.setId_ligne_affectee(rs.getInt("ligne_affectee"));
                event.setType_evenement(TypeEvenement.fromString(rs.getString("type")));
                event.setDescription(rs.getString("description"));
                event.setDate_debut(rs.getDate("date_debut"));
                event.setDate_fin(rs.getDate("date_fin"));
                event.setStatus_evenement(StatusEvenement.fromString(rs.getString("statut")));

                events.add(event);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return events;
    }

    @Override
    public Evenements getById(int id_evenement) {
        String query = "SELECT * FROM evenement WHERE id_evenement = ?";
        Evenements event = null;
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setInt(1, id_evenement);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {

                event = new Evenements(rs.getInt("id_evenement"),
                        TypeEvenement.fromString(rs.getString("type")),
                        rs.getInt("ligne_affectee"),
                        rs.getString("description"),
                        rs.getDate("date_debut"),
                        rs.getDate("date_fin"),
                        StatusEvenement.fromString(rs.getString("statut")),
                        rs.getInt("id_createur")
                );
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return event;
    }

    @Override
    public void update(Evenements evenements) {
        String query = "UPDATE `evenement` SET `type`=?,`description`=?,`date_debut`=?,`date_fin`=?,`ligne_affectee`=?,`statut`=? WHERE id_evenement = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setString(1,evenements.getType_evenement().name());//type event
            pstm.setString(2,evenements.getDescription());// description
            pstm.setDate(3,evenements.getDate_debut());// date debut
            pstm.setDate(4,evenements.getDate_fin());// date fin
            pstm.setInt(5,evenements.getId_ligne_affectee());// id ligne affected
            pstm.setString(6,evenements.getStatus_evenement().name()); // status event
            pstm.setInt(7,evenements.getId_event());

            int rowsUpdated = pstm.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("L'evenement " + evenements.getId_event() + " a été mis à jour avec succès.");
            } else {
                System.out.println("Aucun evenement trouvé avec l'ID " + evenements.getId_event());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(int id_evenements) {
        String query = "DELETE FROM `evenement` WHERE id_evenement = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setInt(1, id_evenements); // Suppression basée sur l'ID
            int rowsDeleted = pstm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("L'evenement" + id_evenements + " a été supprimé avec succès.");
            } else {
                System.out.println("Aucune evenement trouvé avec l'ID " + id_evenements);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getLigneInfo(int idLigne) {
        String ligneInfo = "";
        String query = "SELECT depart, arret FROM ligne WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, idLigne);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String depart = rs.getString("depart");
                String arret = rs.getString("arret");
                ligneInfo = depart + " - " + arret;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ligneInfo;
    }

    public List<String> getAllLineInfo(){
        List<String> lines = new ArrayList<>();
        String query = "SELECT depart, arret FROM ligne";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs =stm.executeQuery(query);
            while (rs.next()) {
                lines.add(rs.getString("depart") + " - " + rs.getString("arret"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lines;
    }

    public int getLineIdByDepartArret(String depart, String arret) {
        int lineId = 0;
        String query = "SELECT id FROM ligne WHERE depart = ? AND arret = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setString(1, depart);
            pstm.setString(2, arret);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                lineId = rs.getInt("id");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lineId;
    }

    public List<Integer> getUsersIdByLine(String depart, String arret, Connection conn){
        List<Integer> usersIds = new ArrayList<>();
        String query = "SELECT `user_id` FROM `reservation` WHERE depart = ? AND arret = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, depart);
            ps.setString(2, arret);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                usersIds.add(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return usersIds;
    }

    public List<Integer> getPhoneNumbers(List<Integer> userIds, Connection conn) {
        List<Integer> phoneNumbers = new ArrayList<>();
        String query = "SELECT telephonne FROM user WHERE id_user = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            for (int userId : userIds) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    phoneNumbers.add(rs.getInt("telephonne"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return phoneNumbers;
    }


    public Connection getCnx() {
        return cnx;
    }

    public void setCnx(Connection cnx) {
        this.cnx = cnx;
    }
}

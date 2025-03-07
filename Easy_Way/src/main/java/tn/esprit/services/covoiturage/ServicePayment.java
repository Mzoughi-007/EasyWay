package tn.esprit.services.covoiturage;

import tn.esprit.interfaces.IService;
import tn.esprit.models.covoiturage.Payment;
import tn.esprit.util.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePayment implements IService<Payment> {

    private Connection cnx;

    public ServicePayment() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    public void add(Payment payment) {
        String sql = "INSERT INTO payment (amount, transactionId, email) VALUES (?, ?, ?)";

        try {
            PreparedStatement stmt = cnx.prepareStatement(sql);

            // Vérifier si l'objet payment n'est pas null
            if (payment == null) {
                System.out.println("Erreur : L'objet payment est null.");
                return;
            }

            // Afficher les valeurs pour debug
            System.out.println("Ajout du paiement : " + payment.getAmount() + ", "
                    + payment.getTransactionId() + ", " + payment.getEmail());

            stmt.setDouble(1, payment.getAmount());
            stmt.setString(2, payment.getTransactionId());

            // Vérifier si email est null et attribuer une valeur par défaut
            if (payment.getEmail() == null || payment.getEmail().isEmpty()) {
                stmt.setString(3, "unknown@example.com"); // Valeur par défaut
            } else {
                stmt.setString(3, payment.getEmail());
            }

            stmt.executeUpdate();
            System.out.println("Paiement ajouté avec succès !");

        } catch (SQLException e) {
            System.out.println("Erreur d'insertion : " + e.getMessage());
        }
    }

    @Override
    public List<Payment> getAll() {
        ArrayList<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payment";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("id"));
                payment.setTransactionId(rs.getString("transactionId"));
                payment.setAmount(rs.getDouble("amount"));
                payments.add(payment);
            }
            System.out.println(payments);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return payments;
    }

    @Override
    public void update(Payment payment) {
        String query = "UPDATE payment SET transactionId = ?, amount = ? WHERE paymentId  = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setString(1, payment.getTransactionId());
            pstm.setDouble(2, payment.getAmount());
            pstm.setInt(3, payment.getPaymentId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Payment payment) {
        String query = "DELETE FROM payment WHERE paymentId  = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setInt(1, payment.getPaymentId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public Payment getById(int id) {
        System.out.println("Non implémenté");
        return null;
    }

    public String paymentDetails(Payment payment) {
        return "Payment Details: \n" +
                "ID: " + payment.getPaymentId() + "\n" +
                "Transaction ID: " + payment.getTransactionId() + "\n" +
                "Amount: " + payment.getAmount();
    }
}

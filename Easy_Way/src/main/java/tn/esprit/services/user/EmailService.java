package tn.esprit.services.user;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private final String username = "mejrieya384@gmail.com"; // Remplace par ton email
    private final String password = "oire xyug itgu dgwx"; // Remplace par ton mot de passe

    public void sendEmail(String recipient, String subject, String body) throws MessagingException {
        // Configuration des propriétés SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Création de la session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            // Envoi de l'email
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Afficher l'erreur
            throw e; // Relever l'exception pour la propager au contrôleur
        }
    }
}

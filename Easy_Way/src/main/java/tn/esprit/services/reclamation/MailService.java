package tn.esprit.services.reclamation;



import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class MailService {

    public static void sendMail(String recipient, String subject, String body) {
        final String senderEmail = "tayssirbennejma@gmail.com";  // Remplace par ton email
        final String senderPassword = "aczi hjgn angb tdze";    //  Remplace par ton mot de passe ou utilise un mot de passe d'application si tu utilises Gmail

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("Email envoyé avec succès à " + recipient);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}


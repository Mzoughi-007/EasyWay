    package tn.esprit.services.covoiturage;

    import jakarta.mail.*;
    import jakarta.mail.internet.InternetAddress;
    import jakarta.mail.internet.MimeMessage;

    import java.util.Properties;

    public class MailService {

        private static final String SMTP_HOST = "smtp.gmail.com";
        private static final String SMTP_PORT = "587";
        private static final String EMAIL_USERNAME = "ghofranebenhassen64@gmail.com";
        private static final String EMAIL_PASSWORD = "cnkdngifhvwesphx"; // Remplace par ton mot de passe d'application

        public boolean sendEmail(String recipient, String subject, String messageText) {
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", SMTP_HOST);
            properties.put("mail.smtp.port", SMTP_PORT);
            properties.put("mail.smtp.ssl.trust", SMTP_HOST); // Pour éviter certaines erreurs SSL

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    if (EMAIL_PASSWORD == null || EMAIL_PASSWORD.isEmpty()) {
                        System.err.println("❌ ERREUR : Mot de passe SMTP manquant !");
                        return null;
                    }
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(EMAIL_USERNAME));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject(subject);
                message.setText(messageText);

                // Pour envoyer un email en HTML (optionnel)
                message.setContent("<h2>" + subject + "</h2><p>" + messageText + "</p>", "text/html");

                Transport.send(message);
                System.out.println("✅ Email envoyé avec succès à : " + recipient);
                return true;
            } catch (MessagingException e) {
                e.printStackTrace();
                System.out.println("❌ Échec de l'envoi de l'email : " + e.getMessage());
                return false;
            }
        }
    }

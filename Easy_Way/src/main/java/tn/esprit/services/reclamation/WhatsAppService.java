package tn.esprit.services.reclamation;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class WhatsAppService {
    // Renseigne tes informations Twilio
    private static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    private static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");

    private static final String TWILIO_WHATSAPP_NUMBER = "whatsapp:+14155238886"; // Numéro Twilio

    // Assurez-vous que le numéro WhatsApp du destinataire est correct et différent du numéro Twilio
    public static void sendWhatsAppMessage(String recipientNumber, String messageText) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Formatez le numéro du destinataire pour inclure 'whatsapp:'
        String formattedRecipientNumber = "whatsapp:" + recipientNumber;

        // Assurez-vous que recipientNumber est bien différent du TWILIO_WHATSAPP_NUMBER
        if (!formattedRecipientNumber.equals(TWILIO_WHATSAPP_NUMBER)) {
            // Envoi du message
            Message message = Message.creator(
                    new PhoneNumber(formattedRecipientNumber),  // Numéro du destinataire
                    new PhoneNumber(TWILIO_WHATSAPP_NUMBER),    // Numéro Twilio
                    messageText
            ).create();

            System.out.println("Message WhatsApp envoyé avec succès : " + message.getSid());
        } else {
            System.out.println("Erreur : Le numéro de destinataire est le même que le numéro Twilio.");
        }
    }
}

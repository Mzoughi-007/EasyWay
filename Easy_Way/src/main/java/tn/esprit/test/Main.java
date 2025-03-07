package tn.esprit.test;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import tn.esprit.services.user.ServiceConducteur ;

import tn.esprit.services.trajet.ServicePaiement;
import tn.esprit.services.trajet.ServiceReservation;
import tn.esprit.models.trajet.Reservation;

import tn.esprit.services.event.ServiceEvenement;
import tn.esprit.services.event.ServiceTwilio;
import tn.esprit.models.trajet.Map;

public class Main {
    public static void main(String[] args) {
        VonageClient client = VonageClient.builder().apiKey("c8c34ce3")
                .apiSecret("FChHt3T5SB9XvulI")
                .build();


        TextMessage message = new TextMessage("Easy Way",
                "+21694103115",  // Replace with the recipient's number
                "🚨 just adam ytesti f api SMS!");

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        for (SmsSubmissionResponseMessage messageResponse : response.getMessages()) {
            System.out.println("📩 SMS Sent! Status: " + messageResponse.getStatus());
        }

    }
}
//Tunis, Tunisia
//mourouj
//sfax

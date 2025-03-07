package tn.esprit.services.event;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
public class ServiceTwilio {

    private static final String ACCOUNT_SID = "ACc4e5db72084c04c11d3b9773cdd3b054";
    private static final String AUTH_TOKEN = "7415b615956ba1d15a8538c01834bd4d";
    private static final String TWILIO_PHONE_NUMBER = "+16073665269";

    public void sendSMS(String recipientNumber, String messageSent) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new PhoneNumber(recipientNumber),
                new PhoneNumber(TWILIO_PHONE_NUMBER),
                messageSent
        ).create();

        System.out.println("Message envoyé à : " + recipientNumber);
    }
}

package tn.esprit.services.covoiturage;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class APIpayment {
    private static final String STRIPE_API_KEY = "sk_test_51QxIfzFVpc7jNaMMUDnMCA8yvRNLuVhS4dtF2X6UDNTy6YJMXqHRT8r7j645viHyXVwodE94SpX2P0OIEBBDrpPq00hrXxCoZh";

    static {
        Stripe.apiKey = STRIPE_API_KEY;
    }
    public static PaymentIntent createPayment(double montant, String email) {
        try {
            long amountInCents = (long) (montant * 100);

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("gbp")
                    .setPaymentMethod("pm_card_visa")
                    .addPaymentMethodType("card")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            if (paymentIntent.getStatus().equals("requires_payment_method")) {
                System.out.println("✅ PaymentIntent created. Sending email...");
                MailService mailService = new MailService();
                mailService.sendEmail(email, "Payment Confirmation", "Your payment of " + montant + " GBP has been received.");

            }

            return paymentIntent;
        } catch (StripeException e) {
            e.printStackTrace();
            return null;
        }
    }



//    public static void main(String[] args) {
//        System.out.println("🔄 Starting Stripe Payment Test...");
//        double montant = 5.00;
//
//        PaymentIntent paymentIntent = createPayment(montant);
//
//        if (paymentIntent != null) {
//            System.out.println("✅ PaymentIntent created successfully!");
//            System.out.println("🆔 ID: " + paymentIntent.getId());
//            System.out.println("💰 Amount: " + paymentIntent.getAmount() / 100.0 + " GBP");
//            System.out.println("🔗 Status: " + paymentIntent.getStatus());
//        } else {
//            System.out.println("❌ Failed to create PaymentIntent.");
//        }
//    }
}

package tn.esprit.models.trajet;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class PaiementAPI {
    // Your Stripe API Key
    private static final String STRIPE_API_KEY = "sk_test_51Qv6RFJfspVdInoDmTxfpmACuQYygsgpsNopsq5iRu6TxbHGj9rXL6CW7lXq9ukez8Xt9i8fHeoIw7DIsKZEWWZ300E9wVE1TG";

    static {
        Stripe.apiKey = STRIPE_API_KEY; // Set your Stripe secret key
    }

    // Method to create a PaymentIntent
    public static PaymentIntent createPayment(double montant) {
        try {
            long amountInCents = (long) (montant * 100); // Stripe expects amount in cents

            // Create PaymentIntent with PaymentMethod and other parameters
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents) // Set the amount in cents
                    .setCurrency("gbp") // Set the currency to GBP (or any other currency)
                    .setPaymentMethod("pm_card_visa") // Use the test Visa card payment method ID
                    .addPaymentMethodType("card") // Add "card" as a valid payment method type
                    .build();

            // Create the PaymentIntent with the specified parameters
            return PaymentIntent.create(params); // Return the created PaymentIntent
        } catch (StripeException e) {
            e.printStackTrace(); // Print error if something goes wrong
            return null;
        }
    }
/*
    // Main method to test the payment process
    public static void main(String[] args) {
        // Example amount for the payment
        double montant = 5.00;  // Amount to be charged (in GBP)

        // Create a PaymentIntent
        PaymentIntent paymentIntent = createPayment(montant);

        // Check if the PaymentIntent was created successfully
        if (paymentIntent != null) {
            System.out.println("PaymentIntent created successfully!");
            System.out.println("PaymentIntent ID: " + paymentIntent.getId());
            System.out.println("Amount: " + paymentIntent.getAmount() / 100.0);  // Amount in GBP
            System.out.println("Currency: " + paymentIntent.getCurrency());
        } else {
            System.out.println("Failed to create PaymentIntent.");
        }
    }*/
}

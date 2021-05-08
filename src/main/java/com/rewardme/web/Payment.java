package com.rewardme.web;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("payment")
public class Payment {
    @RequestMapping(value = "/create-payment-intent", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createPaymentIntent(Model model) throws StripeException {
        Stripe.apiKey = "sk_test_lfplhltXHKdcGOGXpvIcsJKR005x5i2xrM";

        PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                .setCurrency("usd")
                .setAmount(2000L)
                .build();

        PaymentIntent intent = PaymentIntent.create(createParams);

        intent.getPaymentMethod();

        return ResponseEntity.ok("{\"clientSecret\" : \"" + intent.getClientSecret() + "\" }");
    }

}

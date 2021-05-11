package com.rewardme.web;

import com.rewardme.data.ContributorRepository;
import com.rewardme.data.PaymentRepository;
import com.rewardme.data.RecipientRepository;
import com.rewardme.data.entity.Contributor;
import com.rewardme.data.entity.PaymentStatus;
import com.rewardme.data.entity.Recipient;
import com.rewardme.data.entity.RecipientType;
import com.rewardme.moneymove.PaymentDistributor;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodListParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("payment")
public class Payment {
    private final PaymentRepository paymentRepository;
    private final PaymentDistributor paymentDistributor;
    private final RecipientRepository recipientRepository;
    private final ContributorRepository contributorRepository;

    @Autowired
    public Payment(PaymentRepository paymentRepository, PaymentDistributor paymentDistributor, RecipientRepository recipientRepository, ContributorRepository contributorRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentDistributor = paymentDistributor;
        this.recipientRepository = recipientRepository;
        this.contributorRepository = contributorRepository;
    }

    @RequestMapping(value = "/create-payment-intent", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createPaymentIntent(Model model, @RequestBody SupportRequest request) throws StripeException {
        Stripe.apiKey = "sk_test_lfplhltXHKdcGOGXpvIcsJKR005x5i2xrM";

        Customer customer = Customer.create(CustomerCreateParams.builder().build());

        long amount = request.getAmount();
        PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                .setCurrency("usd")
                .setAmount(amount)
                .setSetupFutureUsage(PaymentIntentCreateParams.SetupFutureUsage.OFF_SESSION)
                .setCustomer(customer.getId())
                .build();

        PaymentIntent intent = PaymentIntent.create(createParams);

        intent.getPaymentMethod();

        com.rewardme.data.entity.Payment payment = new com.rewardme.data.entity.Payment();
        payment.setAmount(amount);

        List<Recipient> hosts = recipientRepository.findByName(request.getHost());
        Recipient host;
        if (hosts.isEmpty()) {
            host = new Recipient(RecipientType.HOST);
            host.setName(request.getHost());
        } else {
            host = hosts.get(0);
        }
        payment.addRecipient(host);
        List<Recipient> creators = recipientRepository.findByName(request.getCreator());
        Recipient creator;
        if (hosts.isEmpty()) {
            creator = new Recipient(RecipientType.CREATOR);
            creator.setName(request.getCreator());
        } else {
            creator = creators.get(0);
        }
        payment.addRecipient(creator);

        Contributor contributor = new Contributor(UUID.randomUUID().toString());
        contributor.setExternalId(customer.getId());
        payment.setContributor(contributor);

        com.rewardme.data.entity.Payment savedPayment = paymentRepository.save(payment);

        pullStatus(intent, savedPayment);

        return ResponseEntity.ok("{\"clientSecret\" : \"" + intent.getClientSecret() + "\", \"contributor\" : \"" + contributor.getUuid() + "\" }");
    }

    @RequestMapping(value = "/create-recurring-intent", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createRecurringIntent(Model model, @RequestBody SupportRequest request) throws StripeException {
        Stripe.apiKey = "sk_test_lfplhltXHKdcGOGXpvIcsJKR005x5i2xrM";

        List<Contributor> contributors = contributorRepository.findByUuid(request.getContributor());
        if (contributors.isEmpty()) {
            throw new IllegalStateException("Contributor not found: " + request.getContributor());
        }
        Contributor contributor = contributors.get(0);

        PaymentMethodListParams paramsPm =
                PaymentMethodListParams.builder()
                        .setCustomer(contributor.getExternalId())
                        .setType(PaymentMethodListParams.Type.CARD)
                        .build();

        PaymentMethodCollection paymentMethods = PaymentMethod.list(paramsPm);

        if (paymentMethods.getData().isEmpty()) {
            throw new IllegalStateException("No payment methods available");
        }

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setCurrency("usd")
                        .setAmount(request.getAmount())
                        .setPaymentMethod(paymentMethods.getData().get(0).getId())
                        .setCustomer(contributor.getExternalId())
                        .setConfirm(true)
                        .setOffSession(true)
                        .build();

        PaymentIntent.create(params);

        return null;
    }

    private void pullStatus(PaymentIntent intent, com.rewardme.data.entity.Payment savedPayment) {
        new Thread(() -> {
            for (int i = 0; i < 60; i++) {
                try {
                    String status = PaymentIntent.retrieve(intent.getId()).getStatus();
                    if ("succeeded".equals(status)) {
                        savedPayment.setStatus(PaymentStatus.SUCCESSFUL);
                        savedPayment.setBalance(savedPayment.getAmount());
                        paymentDistributor.distribute(paymentRepository.save(savedPayment));
                        return;
                    }
                } catch (StripeException e) {
                    e.printStackTrace();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @GetMapping("/stripe-payment")
    public String stripePayment(Model model, @RequestParam String host, @RequestParam String creator) {
        model.addAttribute("host", host);
        model.addAttribute("creator", creator);
        return "stripe-payment";
    }

    @GetMapping("/stripe-webhook")
    public ResponseEntity<String > stripeWebhook(Model model) {
//        System.out.println(body);
        return ResponseEntity.ok("");
    }

}

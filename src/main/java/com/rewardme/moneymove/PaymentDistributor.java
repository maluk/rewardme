package com.rewardme.moneymove;

import com.rewardme.data.PaymentRepository;
import com.rewardme.data.RecipientRepository;
import com.rewardme.data.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentDistributor {
    private final RecipientRepository recipientRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentDistributor(RecipientRepository recipientRepository, PaymentRepository paymentRepository) {
        this.recipientRepository = recipientRepository;
        this.paymentRepository = paymentRepository;
    }

    public void distribute(Payment payment) {
        if (payment.getBalance() <= 0d) {
            System.out.println("Nothing to distribute");
            return;
        }

        payment.getRecipients().forEach(recipient -> {
            long recipientPortion = (long) (payment.getAmount() * recipient.getRecipientType().getPercentage());
            recipient.setBalance(recipient.getBalance() + recipientPortion);
            recipientRepository.save(recipient);
            payment.setBalance(payment.getBalance() - recipientPortion);
            paymentRepository.save(payment);
        });
    }
}

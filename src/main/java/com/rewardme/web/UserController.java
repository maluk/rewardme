package com.rewardme.web;

import com.rewardme.data.PaymentRepository;
import com.rewardme.data.RecipientRepository;
import com.rewardme.data.entity.Payment;
import com.rewardme.data.entity.Recipient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {
    private final RecipientRepository recipientRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public UserController(RecipientRepository recipientRepository, PaymentRepository paymentRepository) {
        this.recipientRepository = recipientRepository;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/mydata")
    public String myData(Model model, @RequestParam Long id) {
        Recipient recipient = recipientRepository.findById(id).orElseThrow();
        model.addAttribute("recipient", recipient);

        List<Payment> payments = new LinkedList<>();
        paymentRepository.findAll().forEach(payment -> {
            if (payment.getRecipients().contains(recipient)) {
                payments.add(payment);
            }
        });
        model.addAttribute("payments", payments);

        return "user/mydata";
    }
}

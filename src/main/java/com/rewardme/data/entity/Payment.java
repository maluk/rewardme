package com.rewardme.data.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Payment {
    @Id
    @GeneratedValue
    private Long id;

    private Date date;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @ManyToMany(cascade=CascadeType.ALL)
    private List<Recipient> recipients;

    @OneToOne(cascade = CascadeType.ALL)
    private Contributor contributor;

    private long amount;
    private long balance;

    public Payment() {
        this(PaymentStatus.INITIATED);
    }

    public Payment(PaymentStatus status) {
        this.status = status;
        this.date = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
    }

    public void addRecipient(Recipient recipient) {
        if (this.recipients == null) {
            this.recipients = new LinkedList<>();
        }
        this.recipients.add(recipient);
    }

    public Contributor getContributor() {
        return contributor;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

}

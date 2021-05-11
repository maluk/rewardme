package com.rewardme.data.entity;

import javax.persistence.*;

@Entity
public class Recipient {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private RecipientType recipientType;

    private String name;

    private long totalEarned;

    private long balance;

    public Recipient() {

    }

    public Recipient(RecipientType recipientType) {
        this.recipientType = recipientType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RecipientType getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(RecipientType recipientType) {
        this.recipientType = recipientType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalEarned() {
        return totalEarned;
    }

    public void setTotalEarned(long totalEarned) {
        this.totalEarned = totalEarned;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}

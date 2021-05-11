package com.rewardme.data.entity;

public enum RecipientType {
    HOST(0.15d),
    CREATOR(0.75d);

    private final double percentage;

    RecipientType(double percentage) {
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }
}

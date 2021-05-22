package com.zopa.assignment.domain;

import java.math.BigDecimal;

public class Lender {

    private String name;
    private BigDecimal rate;
    private BigDecimal availableAmount;

    public Lender(String name, BigDecimal rate, BigDecimal availableAmount) {
        this.name = name;
        this.rate = rate;
        this.availableAmount = availableAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }
}

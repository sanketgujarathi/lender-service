package com.zopa.assignment.domain;

import java.math.BigDecimal;

public class Lender {

    private String lender;
    private BigDecimal rate;
    private BigDecimal available;


    public Lender() {
    }

    public Lender(String lender, BigDecimal rate, BigDecimal available) {
        this.lender = lender;
        this.rate = rate;
        this.available = available;
    }

    public String getLender() {
        return lender;
    }

    public void setLender(String lender) {
        this.lender = lender;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAvailable() {
        return available;
    }

    public void setAvailable(BigDecimal available) {
        this.available = available;
    }

}

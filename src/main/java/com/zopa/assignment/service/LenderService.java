package com.zopa.assignment.service;

import com.zopa.assignment.domain.Quote;

import java.math.BigDecimal;
import java.util.Optional;

public interface LenderService {

    Optional<Quote> getQuote(BigDecimal requestedAmount);

}

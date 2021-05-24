package com.zopa.assignment.service;

import com.zopa.assignment.domain.Lender;
import com.zopa.assignment.domain.Quote;
import com.zopa.assignment.repository.LenderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

@Service
public class LenderServiceImpl implements LenderService {

    private final int MIN_VAL;
    private final int MAX_VAL;
    private final int DURATION;
    private final int MULTIPLICITY_FACTOR;
    private LenderRepository lenderRepository;
    private static Logger log = LoggerFactory.getLogger(LenderServiceImpl.class);

    public LenderServiceImpl(LenderRepository lenderRepository,
                             @Value("${lender.min_val}")int minValue,
                             @Value("${lender.max_val}")int maxValue,
                             @Value("${lender.duration}")int duration,
                             @Value("${lender.multiplicity_factor}")int multiplicityFactor) {
        this.lenderRepository = lenderRepository;
        MIN_VAL = minValue;
        MAX_VAL = maxValue;
        DURATION = duration;
        MULTIPLICITY_FACTOR = multiplicityFactor;
    }

    @Override
    public Optional<Quote> getQuote(BigDecimal requestedAmount) {
        if(!isValidAmount(requestedAmount)) {
            log.error("Please provide an amount that is between £1000 and £15000 and multiples of 100");
            return Optional.empty();//TODO
        }
        List<Lender> lenders = lenderRepository.getAllLenders();
        lenders.sort(Comparator.comparing(Lender::getRate));
        BigDecimal remainingAmount = requestedAmount;
        BigDecimal rateCalculator = ZERO;
        for (Lender lender : lenders) {
            if (remainingAmount.compareTo(ZERO) > 0) {
                BigDecimal availableAmount = lender.getAvailable();
                BigDecimal borrowedAmount =
                        remainingAmount.compareTo(availableAmount) > 0 ? availableAmount : remainingAmount;
                rateCalculator = rateCalculator.add(borrowedAmount.multiply(lender.getRate()));
                lender.setAvailable(availableAmount.subtract(borrowedAmount));
                remainingAmount = remainingAmount.subtract(borrowedAmount);
            } else {
                break;
            }
        }
        if (remainingAmount.compareTo(ZERO) == 0) {
            BigDecimal rate = rateCalculator.divide(requestedAmount, RoundingMode.HALF_UP);
            log.info("Lending request fulfilled with rate {}", rate);
            return Optional.of(calculateQuote(requestedAmount, rate));
        } else {
            log.info("Not enough offers present to fulfil the request");
            return Optional.empty();
        }
    }

    private Quote calculateQuote(BigDecimal loanAmount, BigDecimal interestRate) {
        BigDecimal apr = calculateAPR(interestRate);
        BigDecimal monthlyInterestRate = apr.divide(valueOf(12), MathContext.DECIMAL128);
        BigDecimal monthlyRepayment = calculateMonthlyRepayment(loanAmount, monthlyInterestRate).setScale(2, RoundingMode.UP);
        return new Quote(loanAmount,
                interestRate,
                monthlyRepayment.setScale(2, RoundingMode.UP),
                monthlyRepayment.multiply(valueOf(DURATION)).setScale(2, RoundingMode.UP));
    }

    // Amortization formula (P * r )/ (1 - 1/(1+r)^n)
    private BigDecimal calculateMonthlyRepayment(BigDecimal loanAmount, BigDecimal monthylyInterestRate) {
        return loanAmount.multiply(monthylyInterestRate).divide(
                ONE.subtract(
                        ONE.divide(
                                ONE.add(monthylyInterestRate)
                                        .pow(DURATION), MathContext.DECIMAL128)), MathContext.DECIMAL128);
    }

    private BigDecimal calculateAPR(BigDecimal interestRate){
        return valueOf((Math.pow(interestRate.add(ONE).doubleValue(), Math.pow(DURATION,-1)) - 1 )* DURATION);
    }

    private boolean isValidAmount(BigDecimal requestedAmount){
        return requestedAmount.remainder(valueOf(MULTIPLICITY_FACTOR)).compareTo(ZERO) == 0 &&
                requestedAmount.compareTo(valueOf(MIN_VAL)) >= 0 &&
                requestedAmount.compareTo(valueOf(MAX_VAL)) <= 0;
    }

}

package com.zopa.assignment.service;

import com.zopa.assignment.domain.Lender;
import com.zopa.assignment.domain.Quote;
import com.zopa.assignment.repository.LenderRepository;
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

    private static final int MIN_VAL = 1000;
    private static final int MAX_VAL = 15000;
    private static final int DURATION = 36;
    private static final int MULTIPLICITY_FACTOR = 100;
    private LenderRepository lenderRepository;

    public LenderServiceImpl(LenderRepository lenderRepository) {
        this.lenderRepository = lenderRepository;
    }

    @Override
    public Optional<Quote> getQuote(BigDecimal requestedAmount) {
        if(!isValidAmount(requestedAmount)) {
            System.out.println("Please provide an amount that is between £1000 and £15000 and multiples of 100");
            throw new RuntimeException("Please provide an amount that is between £1000 and £15000 and multiples of 100");
        }
        List<Lender> lenders = lenderRepository.getAllLenders();
        lenders.sort(Comparator.comparing(Lender::getRate));
        BigDecimal remainingAmount = requestedAmount;
        BigDecimal rateCalculator = ZERO;
        for (Lender lender : lenders) {
            if (remainingAmount.compareTo(ZERO) > 0) {
                BigDecimal availableAmount = lender.getAvailableAmount();
                BigDecimal borrowedAmount =
                        remainingAmount.compareTo(availableAmount) > 0 ? availableAmount : remainingAmount;
                rateCalculator = rateCalculator.add(borrowedAmount.multiply(lender.getRate()));
                lender.setAvailableAmount(availableAmount.subtract(borrowedAmount));
                remainingAmount = remainingAmount.subtract(borrowedAmount);
            } else {
                break;
            }
        }
        if (remainingAmount.compareTo(ZERO) == 0) {
            BigDecimal rate = rateCalculator.divide(requestedAmount, RoundingMode.HALF_UP);
            return Optional.of(calculateQuote(requestedAmount, rate));
        } else {
            return Optional.empty();
        }
    }

    private Quote calculateQuote(BigDecimal loanAmount, BigDecimal interestRate) {
        BigDecimal apr = calculateAPR(interestRate);
        BigDecimal monthlyInterestRate = apr.divide(valueOf(12), MathContext.DECIMAL128);
        BigDecimal monthlyRepayment = calculateMonthlyRepayment(loanAmount, monthlyInterestRate);
        return new Quote(loanAmount, interestRate, monthlyRepayment.setScale(2, RoundingMode.UP), monthlyRepayment.multiply(valueOf(DURATION)).setScale(1, RoundingMode.HALF_UP));
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

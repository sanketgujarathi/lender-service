package com.zopa.assignment;

import com.zopa.assignment.domain.Quote;
import com.zopa.assignment.service.LenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

@SpringBootApplication
public class LenderApplication implements CommandLineRunner {

    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.UK);
    private static final NumberFormat PERCENT_FORMATTER = getPercentFormatterInstance();
    private LenderService lenderService;
    private static Logger log = LoggerFactory.getLogger(LenderApplication.class);

    public LenderApplication(LenderService lenderService) {
        this.lenderService = lenderService;
    }

    public static void main(String[] args) {
        SpringApplication.run(LenderApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            Optional<Quote> quote = lenderService.getQuote(new BigDecimal(args[0]));
            displayQuote(quote);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    private void displayQuote(Optional<Quote> quote){

        if(quote.isPresent()){
            Quote result = quote.get();
            log.info("Requested amount: {}", CURRENCY_FORMATTER.format(result.getRequestedAmount()));
            log.info("Annual Interest Rate: {}", PERCENT_FORMATTER.format(result.getAnnualInterestRate()));
            log.info("Monthly repayment: {}", CURRENCY_FORMATTER.format(result.getMonthlyRepayment()));
            log.info("Total repayment: {}", CURRENCY_FORMATTER.format(result.getTotalRepayment()));
        } else {
            log.info("It is not possible to provide a quote.");
        }
    }

    private static NumberFormat getPercentFormatterInstance() {
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        percentInstance.setMaximumFractionDigits(2);
        return percentInstance;
    }
}

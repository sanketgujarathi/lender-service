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
        Optional<Quote> quote = lenderService.getQuote(new BigDecimal(args[0]));
        displayQuote(quote);
    }

    private void displayQuote(Optional<Quote> quote){

        if(quote.isPresent()){
            Quote quote1 = quote.get();
            log.info("Requested amount: {}", CURRENCY_FORMATTER.format(quote1.getRequestedAmount()));
            log.info("Annual Interest Rate: {}", NumberFormat.getPercentInstance().format(quote1.getAnnualInterestRate()));
            log.info("Monthly repayment: {}", CURRENCY_FORMATTER.format(quote1.getMonthlyRepayment()));
            log.info("Total repayment: {}", CURRENCY_FORMATTER.format(quote1.getTotalRepayment()));
        } else {
            log.info("It is not possible to provide a quote.");
        }
    }
}

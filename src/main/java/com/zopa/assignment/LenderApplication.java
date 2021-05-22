package com.zopa.assignment;

import com.zopa.assignment.domain.Quote;
import com.zopa.assignment.repository.LenderRepositoryImpl;
import com.zopa.assignment.service.LenderService;
import com.zopa.assignment.service.LenderServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootApplication
public class LenderApplication implements CommandLineRunner {

    private LenderService lenderService;

    public LenderApplication(LenderService lenderService) {
        this.lenderService = lenderService;
    }

    public static void main(String[] args) {
        SpringApplication.run(LenderApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Optional<Quote> quote = lenderService.getQuote(BigDecimal.valueOf(1000));
        if(quote.isPresent()){
            displayQuote(quote.get());
        } else {
            System.out.println("It is not possible to provide a quote.");
        }
    }

    private void displayQuote(Quote quote){

        System.out.println("quote.getRequestedAmount() = " + quote.getRequestedAmount());
        System.out.println("quote.getAnnualInterestRate() = " + quote.getAnnualInterestRate());
        System.out.println("quote.getMonthlyRepayment() = " + quote.getMonthlyRepayment());
        System.out.println("quote.getTotalRepayment() = " + quote.getTotalRepayment());
    }
}

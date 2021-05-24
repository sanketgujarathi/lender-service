package com.zopa.assignment.service;


import com.zopa.assignment.domain.Lender;
import com.zopa.assignment.domain.Quote;
import com.zopa.assignment.repository.LenderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(args = {"0"})
public class LenderServiceImplTest {

    @MockBean
    private LenderRepository lenderRepository;

    @Autowired
    private LenderService lenderService;

    @Test
    public void getQuoteSuccessfulWhenLendersAvailable() {
        Mockito.when(lenderRepository.getAllLenders()).thenReturn(getLenders());

        BigDecimal requestedAmount = new BigDecimal(1000);
        Optional<Quote> quote = lenderService.getQuote(requestedAmount);
        assertTrue(quote.isPresent());
        assertEquals(quote.get().getRequestedAmount(), requestedAmount);
        assertEquals(new BigDecimal("0.070"), quote.get().getAnnualInterestRate());
        assertEquals(valueOf(30.78), quote.get().getMonthlyRepayment());
        assertEquals(valueOf(1108.08), quote.get().getTotalRepayment());
    }

    @Test
    public void noQuoteReturnedWhenLendersNotAvailable() {
        Mockito.when(lenderRepository.getAllLenders()).thenReturn(getLenders());

        BigDecimal requestedAmount = new BigDecimal(1700);
        Optional<Quote> quote = lenderService.getQuote(requestedAmount);
        assertFalse(quote.isPresent());
    }

    @Test
    public void noQuoteReturnedWhenNoLendersPresent() {
        Mockito.when(lenderRepository.getAllLenders()).thenReturn(new ArrayList<>());

        BigDecimal requestedAmount = new BigDecimal(1000);
        Optional<Quote> quote = lenderService.getQuote(requestedAmount);
        assertFalse(quote.isPresent());

    }

    @Test
    public void noQuoteReturnedWhenInvalidAmountPassed(){
        assertThrows(IllegalArgumentException.class, () -> lenderService.getQuote(BigDecimal.valueOf(999.99)));
        assertThrows(IllegalArgumentException.class, () -> lenderService.getQuote(BigDecimal.valueOf(1001)));
        assertThrows(IllegalArgumentException.class, () -> lenderService.getQuote(BigDecimal.valueOf(15100)));
    }

    private List<Lender> getLenders() {
        Lender lender1 = new Lender("Jane", valueOf(0.069), new BigDecimal(480));
        Lender lender2 = new Lender("John", valueOf(0.071), new BigDecimal(520));
        List<Lender> lenders = new ArrayList<>();
        lenders.add(lender1);
        lenders.add(lender2);
        return lenders;
    }
}
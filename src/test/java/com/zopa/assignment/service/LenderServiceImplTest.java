package com.zopa.assignment.service;


import com.zopa.assignment.domain.Lender;
import com.zopa.assignment.domain.Quote;
import com.zopa.assignment.repository.LenderRepository;
import com.zopa.assignment.repository.LenderRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class LenderServiceImplTest {


    @MockBean
    private LenderRepository lenderRepository;

    @Autowired
    private LenderService lenderService;

    /*@BeforeEach
    public void init(){
        //lenderRepository = new LenderRepositoryImpl();
        lenderService = new LenderServiceImpl(lenderRepository);
    }*/


    @Test
    public void getQuote() {
        Lender lender1 = new Lender("Jane", valueOf(0.069), new BigDecimal(480));
        Lender lender2 = new Lender("John", valueOf(0.071), new BigDecimal(520));
        List<Lender> lenders = new ArrayList<>();
        lenders.add(lender1);
        lenders.add(lender2);
        Mockito.when(lenderRepository.getAllLenders()).thenReturn(lenders);
        BigDecimal requestedAmount = new BigDecimal(1000);
        Optional<Quote> quote = lenderService.getQuote(requestedAmount);
        assertTrue(quote.isPresent());
        assertEquals(quote.get().getRequestedAmount(), requestedAmount);
        //assertEquals(BigDecimal.valueOf(0.070), quote.get().getAnnualInterestRate());
        assertEquals(valueOf(30.78), quote.get().getMonthlyRepayment());
        assertEquals(valueOf(1108.10), quote.get().getTotalRepayment());


    }
}
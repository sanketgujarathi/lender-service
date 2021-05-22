package com.zopa.assignment.repository;

import com.zopa.assignment.domain.Lender;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LenderRepositoryImpl implements LenderRepository {

    @Override
    public List<Lender> getAllLenders() {
        Lender lender1 = new Lender("Jane", BigDecimal.valueOf(0.069), new BigDecimal(480));
        Lender lender2 = new Lender("John", BigDecimal.valueOf(0.071), new BigDecimal(1000));
        List<Lender> lenders = new ArrayList<>();
        lenders.add(lender2);
        lenders.add(lender1);
        return lenders;
    }

}

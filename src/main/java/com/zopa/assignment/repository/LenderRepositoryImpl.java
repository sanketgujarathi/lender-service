package com.zopa.assignment.repository;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.zopa.assignment.domain.Lender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Repository
public class LenderRepositoryImpl implements LenderRepository {


    private String inputFilePath;
    private static Logger log = LoggerFactory.getLogger(LenderRepositoryImpl.class);

    public LenderRepositoryImpl(@Value("${lender.file_path}")String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    @Override
    public List<Lender> getAllLenders() {

        try (FileReader reader = new FileReader(inputFilePath)) {
            CsvToBean<Lender> csvReader = new CsvToBeanBuilder<Lender>(reader).withType(Lender.class).build();
            return csvReader.parse();

        } catch (IOException e) {
            log.error("Unable to read lender data", e);
        }
        return Collections.emptyList();
    }

    private ColumnPositionMappingStrategy<Lender> getColumnMapping() {
        ColumnPositionMappingStrategy<Lender> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Lender.class);
        String[] columns = {"lender", "rate", "available"};
        strategy.setColumnMapping(columns);
        return strategy;
    }

}

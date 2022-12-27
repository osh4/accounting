package com.osh4.accounting.service.dataimport.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.osh4.accounting.service.dataimport.FileImportService;
import com.osh4.accounting.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Service
@Slf4j
public class CsvFileImportService implements FileImportService {
    @Override
    public void importData(String filename, Map<Integer, Consumer<String>> fieldMapping,
                           Map<Integer, Validator<String>> validatorMapping)
            throws IOException, CsvValidationException {
        try (Reader reader = Files.newBufferedReader(Path.of(filename))) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    System.out.println(Arrays.toString(line));
                }
            }
        }
    }

    @Override
    public boolean isValidFile(String filename, Map<Integer, Consumer<String>> fieldMapping,
                               Map<Integer, Validator<String>> validatorMapping)
            throws IOException, CsvValidationException {
        try (Reader reader = Files.newBufferedReader(Path.of(filename));
             CSVReader csvReader = new CSVReader(reader)) {
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                if (!isValidRow(row, validatorMapping)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isValidRow(String[] line, Map<Integer, Validator<String>> validatorMapping) {
        boolean isValid = isCorrectRowSize(line, validatorMapping);
        return isValid && IntStream.range(0, line.length).allMatch(i -> isValidColumn(i, line[i], validatorMapping));
    }

    private boolean isValidColumn(int colNum, String value, Map<Integer, Validator<String>> validatorMapping) {
        if (!validatorMapping.containsKey(colNum)) {
            log.info("There is no validator for the field, skip it");
            return true;
        }

        if (!validatorMapping.get(colNum).validate(value)) {
            log.error("Invalid value in cell {}", colNum);
            return false;
        }
        return true;
    }

    private boolean isCorrectRowSize(String[] line, Map<Integer, Validator<String>> validatorMapping) {
        int expectedSize = MapUtils.size(validatorMapping);
        int actualSize = ArrayUtils.getLength(line);
        if (expectedSize != actualSize) {
            log.error("Incorrect number of fields in a row, actual: {}, should be: {}", actualSize, expectedSize);
            return false;
        }
        return true;
    }
}

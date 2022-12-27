package com.osh4.accounting.service.dataimport.impl;

import com.osh4.accounting.service.dataimport.FileImportService;
import com.osh4.accounting.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Service
@Slf4j
public class XlsFileImportService implements FileImportService {
    @Override
    public void importData(String filename, Map<Integer, Consumer<String>> fieldMapping,
                           Map<Integer, Validator<String>> validatorMapping) throws Exception {
        var f = new File(filename);
        try (var wb = new ReadableWorkbook(f)) {
            Sheet sheet = wb.getFirstSheet();
            try (Stream<Row> rows = sheet.openStream()) {
                var it = rows.iterator();
                while (it.hasNext()) {
                    processRow(it.next(), fieldMapping, validatorMapping);
                }
            }
        }
    }

    @Override
    public boolean isValidFile(String filename, Map<Integer, Consumer<String>> fieldMapping,
                               Map<Integer, Validator<String>> validatorMapping) throws IOException {
        var f = new File(filename);
        try (var wb = new ReadableWorkbook(f)) {
            Sheet sheet = wb.getFirstSheet();
            try (Stream<Row> rows = sheet.openStream()) {
                var it = rows.iterator();
                while (it.hasNext()) {
                    if (!isValidRow(validatorMapping, it.next())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void processRow(Row row, Map<Integer, Consumer<String>> fieldMapping,
                            Map<Integer, Validator<String>> validatorMapping) {
        log.debug("Processing the row {}", row.getRowNum());
        if (row.getRowNum() == 1) {
            log.info("Table header, skip it");
            return;
        }
        if (!isValidRow(validatorMapping, row)) {
            return;
        }
        for (Cell cell : row) {
            var fieldGetter = fieldMapping.get(cell.getColumnIndex());
            fieldGetter.accept(cell.getText());
            System.out.println(cell);
        }
    }

    private boolean isValidRow(Map<Integer, Validator<String>> validatorMapping, Row row) {
        boolean isValidRow = checkCellsCount(row, validatorMapping.size());
        if (!isValidRow) {
            return false;
        }
        if (row.getRowNum() == 0) {
            log.info("Table header found");
            return true;
        }
        for (Cell cell : row) {
            boolean isValid = validateCell(cell, validatorMapping);
            isValidRow &= isValid;
        }
        return isValidRow;
    }

    private boolean checkCellsCount(Row row, int validCount) {
        int actualCount = row.getCellCount();
        if (actualCount != validCount) {
            log.error("Incorrect number of fields in a row, actual: {}, should be: {}", actualCount, validCount);
            return false;
        }
        return true;
    }

    private boolean validateCell(Cell cell, Map<Integer, Validator<String>> validatorMapping) {
        int columnIndex = cell.getColumnIndex();
        var validator = validatorMapping.get(columnIndex);
        var isValid = validator.validate(cell.getText());
        if (!isValid) {
            log.error("Invalid value in cell {}", columnIndex);
        }
        return isValid;
    }
}

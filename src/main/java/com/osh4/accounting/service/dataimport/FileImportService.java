package com.osh4.accounting.service.dataimport;

import com.opencsv.exceptions.CsvValidationException;
import com.osh4.accounting.validation.Validator;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface FileImportService {
    void importData(String filename, Map<Integer, Consumer<String>> fieldMapping,
                    Map<Integer, Validator<String>> validatorMapping) throws Exception;

    boolean isValidFile(String filename, Map<Integer, Consumer<String>> fieldMapping,
                        Map<Integer, Validator<String>> validatorMapping) throws IOException, CsvValidationException;
}

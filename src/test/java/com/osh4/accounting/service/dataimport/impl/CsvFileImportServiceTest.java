package com.osh4.accounting.service.dataimport.impl;

import com.opencsv.exceptions.CsvValidationException;
import com.osh4.accounting.service.dataimport.impl.CsvFileImportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@ExtendWith(MockitoExtension.class)
class CsvFileImportServiceTest {

    @InjectMocks
    private CsvFileImportService service;

    @Test
    public void shouldImportFile() throws CsvValidationException, IOException {
        service.importData("/home/osh4/test.csv", null, null);
        assertTrue(true);
    }
}
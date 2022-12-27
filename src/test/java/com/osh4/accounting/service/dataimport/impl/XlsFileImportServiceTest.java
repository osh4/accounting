package com.osh4.accounting.service.dataimport.impl;

import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.validation.SettingTypeValidator;
import com.osh4.accounting.validation.StringValidator;
import com.osh4.accounting.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@ExtendWith(MockitoExtension.class)
class XlsFileImportServiceTest {
    private final SettingDto setting = new SettingDto();
    private final Map<Integer, Consumer<String>> fieldMapping = Map.of(
            0, setting::setKey,
            1, setting::setType,
            2, setting::setValue);

    @Mock
    private StringValidator stringValidator;
    @Mock
    private SettingTypeValidator settingTypeValidator;
    @InjectMocks
    private XlsFileImportService service;

    @BeforeEach
    public void setUp() {
        when(stringValidator.validate(anyString())).thenReturn(true);
        when(settingTypeValidator.validate(anyString())).thenReturn(true);
    }

    @Test
    public void shouldImportFile() throws Exception {
        Map<Integer, Validator<String>> validatorMapping = Map.of(
                0, stringValidator,
                1, settingTypeValidator,
                2, stringValidator);
        service.importData("/home/osh4/test.xlsx", fieldMapping, validatorMapping);
        assertTrue(true);
    }
}
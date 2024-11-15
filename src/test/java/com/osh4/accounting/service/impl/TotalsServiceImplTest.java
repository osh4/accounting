package com.osh4.accounting.service.impl;

import com.osh4.accounting.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@ExtendWith(MockitoExtension.class)
class TotalsServiceImplTest {

    private static final LocalDate DAY = LocalDate.of(2000, 01, 01);
    private static final LocalDateTime FROM_DATE_FOR_DAY = LocalDate.from(DAY).atTime(LocalTime.MIDNIGHT);
    private static final LocalDateTime TO_DATE_FOR_DAY = LocalDate.from(DAY).atTime(LocalTime.MAX);
    private static final LocalDate FROM = LocalDate.from(DAY);
    private static final LocalDate TO = LocalDate.from(DAY).plusDays(10);
    private static final LocalDateTime FROM_DATE_TIME = FROM.atTime(LocalTime.MIDNIGHT);
    private static final LocalDateTime TO_DATE_TIME = TO.atTime(LocalTime.MAX);
    private static final BigDecimal AMOUNT = BigDecimal.TEN;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TotalsServiceImpl service;

    @Test
    public void shouldGetTotalsByDay() {
        // given
        when(transactionService.getAmountForDatePeriod(FROM_DATE_FOR_DAY, TO_DATE_FOR_DAY)).thenReturn(Mono.just(AMOUNT));

        // when
        String result = service.calculateTotalsForDay(DAY).block();

        // then
        assertThat(result).isEqualTo("10");
    }

    @Test
    public void shouldGetTotalsByPeriod() {
        // given
        when(transactionService.getAmountForDatePeriod(FROM_DATE_TIME, TO_DATE_TIME)).thenReturn(Mono.just(AMOUNT));

        // when
        String result = service.calculateTotalsForPeriod(FROM, TO).block();

        // then
        assertThat(result).isEqualTo("10");
    }
}
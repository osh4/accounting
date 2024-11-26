package com.osh4.accounting.controller;

import com.osh4.accounting.service.TotalsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/totals")
@Slf4j
@AllArgsConstructor
public class TotalsController extends BaseController {

    private final TotalsService totalsService;

    @GetMapping
    public Mono<ResponseEntity<String>> getByPeriod(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                    LocalDate from,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                    LocalDate to) {
        return totalsService.calculateTotalsForPeriod(from, to)
                .flatMap(this::successResponse);
    }

    @GetMapping("/day")
    public Mono<ResponseEntity<String>> getByDay(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                 LocalDate day) {
        return totalsService.calculateTotalsForDay(day)
                .flatMap(this::successResponse);
    }

}

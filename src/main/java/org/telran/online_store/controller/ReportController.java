package org.telran.online_store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.enums.PeriodType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telran.online_store.dto.ProductReportDto;
import org.telran.online_store.service.ReportService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reports")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class ReportController implements ReportApi {

    private final ReportService reportService;

    @Override
    @GetMapping("/topTenPurchasedProducts")
    public ResponseEntity<List<ProductReportDto>> getTopTenPurchasedProducts() {
        log.info("Get report: Top 10 purchased products");
        return ResponseEntity.status(HttpStatus.OK)
                .body(reportService.getTopOrdered());
    }

    @Override
    @GetMapping("/topTenCancelledProducts")
    public ResponseEntity<List<ProductReportDto>> getTopTenCancelledProducts() {
        log.info("Get report: Top 10 cancelled products");
        return ResponseEntity.status(HttpStatus.OK)
                .body(reportService.getTopCancelled());
    }

    @Override
    @GetMapping("/notPaidProducts/{days}")
    public ResponseEntity<List<ProductReportDto>> getNotPaidProducts(@PathVariable Long days) {
        log.info("Get report: Not paid products by {} days", days);
        return ResponseEntity.status(HttpStatus.OK)
                .body(reportService.getNotPaid(days));
    }

    @Override
    @GetMapping("/profit")
    public ResponseEntity<Map<String, BigDecimal>> getProfitReport(
            @RequestParam PeriodType periodType,
            @RequestParam Long periodAmount) {
        log.info("Get profit report: period = {}, period amount = {}", periodType, periodAmount);
        return ResponseEntity.ok(reportService.getProfitReport(periodType, periodAmount));
    }
}


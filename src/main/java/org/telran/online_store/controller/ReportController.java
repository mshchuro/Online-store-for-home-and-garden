package org.telran.online_store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.enums.PeriodType;
import org.telran.online_store.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telran.online_store.entity.Product;
import org.telran.online_store.service.ReportService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reports")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class ReportController implements ReportApi {

    private final ReportService reportService;

    @Override
    @GetMapping("/topTenPurchasedProducts")
    public ResponseEntity<List<String>> getTopTenPurchasedProducts() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(reportService.getTopOrdered().stream()
                        .map(Product::getName).collect(Collectors.toList()));
    }

    @Override
   // @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/topTenCancelledProducts")
    public ResponseEntity<List<String>> getTopTenCancelledProducts() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(reportService.getTopCancelled().stream()
                        .map(Product::getName).collect(Collectors.toList()));
    }

    @Override
   // @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/notPaidProducts/{days}")
    public ResponseEntity<List<String>> getNotPaidProducts(@PathVariable Long days) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(reportService.getNotPaid(days)
                        .stream()
                        .map(Product::getName)
                        .collect(Collectors.toList()));
    }

    @Operation(
            summary = "Profit Report",
            description = "Returns total profit for a given period grouped by hour/day/week/month"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok")
    })
    @GetMapping("/profit")
    public ResponseEntity<Map<String, BigDecimal>> getProfitReport(
            @RequestParam PeriodType periodType,
            @RequestParam Long periodAmount
    ) {
        return ResponseEntity.ok(reportService.getProfitReport(periodType, periodAmount));
    }
}


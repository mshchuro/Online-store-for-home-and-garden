package org.telran.online_store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.enums.PeriodType;
import org.telran.online_store.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Reports", description = "API endpoints reports")
@RequestMapping("/v1/reports")
public class ReportController {

    private final ProductService productService;

    @Operation(
            summary = "Top ten purchased products",
            description = "Allows to view top ten purchased products"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok")
    })
    @GetMapping("/topTenPurchasedProducts")
    public ResponseEntity<List<String>> getTopTenPurchasedProducts() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getTopTenPurchasedProducts());
    }

    @Operation(
            summary = "Top ten cancelled products",
            description = "Allows to view top ten cancelled products"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok")
    })
    @GetMapping("/topTenCancelledProducts")
    public ResponseEntity<List<String>> getTopTenCancelledProducts() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getTopTenCancelledProducts());
    }

    @Operation(
            summary = "Not paid products",
            description = "Allows viewing products that haven't been paid for within a specified number of days"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok")
    })
    @GetMapping("/notPaidProducts/{days}")
    public ResponseEntity<List<String>> getNotPaidProducts(@PathVariable Long days) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getNotPaidProducts(days));
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
        return ResponseEntity.ok(productService.getProfitReport(periodType, periodAmount));
    }
}

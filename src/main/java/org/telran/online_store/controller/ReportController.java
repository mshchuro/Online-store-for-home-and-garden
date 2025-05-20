package org.telran.online_store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telran.online_store.entity.Product;
import org.telran.online_store.service.ReportService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Reports", description = "API endpoints reports")
@RequestMapping("/v1/reports")
public class ReportController {

    private final ReportService reportService;

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
                .body(reportService.getTopOrdered().stream()
                        .map(Product::getName).collect(Collectors.toList()));
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
                .body(reportService.getTopCancelled().stream()
                        .map(Product::getName).collect(Collectors.toList()));
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
                .body(reportService.getNotPaid(days)
                        .stream()
                        .map(Product::getName)
                        .collect(Collectors.toList()));
    }
}

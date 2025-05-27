package org.telran.online_store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.telran.online_store.dto.ProductReportDto;
import org.telran.online_store.enums.PeriodType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Tag(name = "Reports", description = "API endpoints reports")
@SecurityRequirement(name = "bearerAuth")
public interface ReportApi {

    @Operation(
            summary = "Top ten purchased products",
            description = "Allows to view top ten purchased products"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok")
    })
    ResponseEntity<List<ProductReportDto>> getTopTenPurchasedProducts();

    @Operation(
            summary = "Top ten cancelled products",
            description = "Allows to view top ten cancelled products"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok")
    })
    ResponseEntity<List<ProductReportDto>> getTopTenCancelledProducts();

    @Operation(
            summary = "Not paid products",
            description = "Allows viewing products that haven't been paid for within a specified number of days"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok")
    })
    ResponseEntity<List<ProductReportDto>> getNotPaidProducts(@PathVariable Long days);

    @Operation(
            summary = "Profit Report",
            description = "Returns total profit for a given period grouped by hour/day/week/month"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok")
    })
    @GetMapping("/profit")
    ResponseEntity<Map<String, BigDecimal>> getProfitReport(
            @RequestParam PeriodType periodType,
            @RequestParam Long periodAmount);
}

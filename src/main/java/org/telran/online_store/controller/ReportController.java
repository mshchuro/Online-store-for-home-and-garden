package org.telran.online_store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telran.online_store.service.ProductService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/reports")
public class ReportController {

    private final ProductService productService;

    @GetMapping("/topTenPurchasedProducts")
    public ResponseEntity<List<String>> getTopTenPurchasedProducts() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getTopTenPurchasedProducts());
    }

    @GetMapping("/topTenCancelledProducts")
    public ResponseEntity<List<String>> getTopTenCancelledProducts() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getTopTenCancelledProducts());
    }

    @GetMapping("/notPaidProducts/{days}")
    public ResponseEntity<List<String>> getNotPaidProducts(@PathVariable Long days) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getNotPaidProducts(days));
    }
}

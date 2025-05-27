package org.telran.online_store.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telran.online_store.dto.ProductReportDto;
import org.telran.online_store.enums.PeriodType;
import org.telran.online_store.service.ReportService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetTopTenPurchasedProducts() throws Exception {
        ProductReportDto dto = new ProductReportDto(1L, "Product A");

        Mockito.when(reportService.getTopOrdered()).thenReturn(List.of(dto));

        mockMvc.perform(get("/v1/reports/topTenPurchasedProducts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product A"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetTopTenCancelledProducts() throws Exception {
        ProductReportDto dto = new ProductReportDto(2L, "Product B");

        Mockito.when(reportService.getTopCancelled()).thenReturn(List.of(dto));

        mockMvc.perform(get("/v1/reports/topTenCancelledProducts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Product B"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetNotPaidProducts() throws Exception {
        ProductReportDto dto = new ProductReportDto(3L, "Product C");

        Mockito.when(reportService.getNotPaid(7L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/v1/reports/notPaidProducts/{days}", 7L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("Product C"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetProfitReport() throws Exception {
        Map<String, BigDecimal> profitMap = Map.of(
                "2024-05-01", new BigDecimal("100.00"),
                "2024-05-02", new BigDecimal("250.50")
        );

        Mockito.when(reportService.getProfitReport(PeriodType.DAY, 2L)).thenReturn(profitMap);

        mockMvc.perform(get("/v1/reports/profit")
                        .param("periodType", "DAY")
                        .param("periodAmount", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['2024-05-01']").value(100.00))
                .andExpect(jsonPath("$.['2024-05-02']").value(250.50));
    }
}
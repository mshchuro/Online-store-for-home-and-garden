package org.telran.online_store.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telran.online_store.dto.ProductReportDto;
import org.telran.online_store.service.ReportService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetTopTenPurchasedProducts() throws Exception {
        ProductReportDto productC = new ProductReportDto(3L, "Product C");
        ProductReportDto productA = new ProductReportDto(1L, "Product A");
        ProductReportDto productB = new ProductReportDto(2L, "Product B");

        Mockito.when(reportService.getTopOrdered()).thenReturn(
                List.of(productC, productA, productB));

        mockMvc.perform(get("/v1/reports/topTenPurchasedProducts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product C"))
                .andExpect(jsonPath("$[1].name").value("Product A"))
                .andExpect(jsonPath("$[2].name").value("Product B"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetTopTenCancelledProducts() throws Exception {
        ProductReportDto product = new ProductReportDto(2L, "Product B");

        Mockito.when(reportService.getTopCancelled()).thenReturn(List.of(product));

        mockMvc.perform(get("/v1/reports/topTenCancelledProducts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product B"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetNotPaidProducts() throws Exception {
        ProductReportDto product = new ProductReportDto(3L, "Product C");

        Mockito.when(reportService.getNotPaid(Mockito.anyLong())).thenReturn(List.of(product));

        mockMvc.perform(get("/v1/reports/notPaidProducts/{days}", 7L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product C"));
    }
}
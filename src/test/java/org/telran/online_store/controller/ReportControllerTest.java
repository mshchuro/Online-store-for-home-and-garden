package org.telran.online_store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telran.online_store.entity.Product;
import org.telran.online_store.service.ReportService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReportService reportService;

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetTopTenPurchasedProducts() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product A");

       // Mockito.when(reportService.getTopOrdered()).thenReturn(List.of(product));

        mockMvc.perform(get("/v1/reports/topTenPurchasedProducts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Product A"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetTopTenCancelledProducts() throws Exception {
        Product product = new Product();
        product.setId(2L);
        product.setName("Product B");

        //Mockito.when(reportService.getTopCancelled()).thenReturn(List.of(product));

        mockMvc.perform(get("/v1/reports/topTenCancelledProducts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Product B"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    void testGetNotPaidProducts() throws Exception {
        Product product = new Product();
        product.setId(3L);
        product.setName("Product C");

//        Mockito.when(reportService.getNotPaid(7L)).thenReturn(List.of(product));
//
//        mockMvc.perform(get("/v1/reports/notPaidProducts/{days}", 7L)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0]").value("Product C"));
    }
}
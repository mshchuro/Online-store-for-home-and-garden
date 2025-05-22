package org.telran.online_store.service;

import org.telran.online_store.dto.ProductReportDto;
import org.telran.online_store.entity.Product;

import java.util.List;

public interface ReportService {

    List<ProductReportDto> getTopOrdered();

    List<ProductReportDto> getTopCancelled();

    List<Product> getNotPaid(Long days);
}

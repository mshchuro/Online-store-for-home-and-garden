package org.telran.online_store.service;

import org.telran.online_store.dto.ProductReportDto;
import org.telran.online_store.entity.Product;
import org.telran.online_store.enums.PeriodType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReportService {

    List<ProductReportDto> getTopOrdered();

    List<ProductReportDto> getTopCancelled();

    List<ProductReportDto> getNotPaid(Long days);

    Map<String, BigDecimal> getProfitReport(PeriodType periodType, Long periodAmount);
}

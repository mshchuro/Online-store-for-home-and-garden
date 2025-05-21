package org.telran.online_store.service;

import org.telran.online_store.entity.Product;

import java.util.List;

public interface ReportService {

    List<Product> getTopOrdered();

    List<Product> getTopCancelled();

    List<Product> getNotPaid(Long days);
}

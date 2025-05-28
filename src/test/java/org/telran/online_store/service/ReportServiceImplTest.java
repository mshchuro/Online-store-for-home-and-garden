package org.telran.online_store.service;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.telran.online_store.AbstractServicesTests;
import org.telran.online_store.dto.ProductReportDto;
import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.OrderItem;
import org.telran.online_store.entity.Product;
import org.telran.online_store.enums.DeliveryMethod;
import org.telran.online_store.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ReportServiceImplTest extends AbstractServicesTests {

    @Test
    void testGetTopOrderedProducts() {
        Product product = createProduct("Ordered Product");

        Order order = createOrder(OrderStatus.DELIVERED);

        OrderItem item = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(5)
                .priceAtPurchase(product.getPrice())
                .build();
        orderItemRepo.save(item);

        List<ProductReportDto> topOrdered = reportService.getTopOrdered();
        assertEquals(1, topOrdered.size(), "One ordered product expected");
        assertEquals(product.getId(), topOrdered.get(0).id());
        assertEquals(product.getName(), topOrdered.get(0).name());
    }

    @Test
    void testGetTopCancelledProducts() {
        Product product = createProduct("Cancelled Product");

        Order order = createOrder(OrderStatus.CANCELLED);

        OrderItem item = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(3)
                .priceAtPurchase(product.getPrice())
                .build();
        orderItemRepo.save(item);

        List<ProductReportDto> topCancelled = reportService.getTopCancelled();
        assertEquals(1, topCancelled.size(), "One cancelled product expected");
        assertEquals(product.getId(), topCancelled.get(0).id());
        assertEquals(product.getName(), topCancelled.get(0).name());
    }

    @Disabled("TODO: доделать тест")
    @Test
    void testNotPaid() {
        Product product = createProduct("Unpaid Product");

        Order order = createOrder(OrderStatus.PAYMENT_PENDING);
        order.setUpdatedAt(LocalDateTime.now().minusDays(4));
        order = orderRepo.save(order);

        OrderItem item = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(1)
                .priceAtPurchase(product.getPrice())
                .build();
        orderItemRepo.save(item);

        List<ProductReportDto> result = reportService.getNotPaid(3L);

        assertEquals(1, result.size(), "One product should have been returned");
        ProductReportDto dto = result.get(0);
        assertEquals(product.getId(), dto.id(), "It's not the expected unpaid product");
        assertEquals(product.getName(), dto.name(), "Unexpected product name");
    }

    private Product createProduct(String name) {
        Product product = Product.builder()
                .name(name)
                .description("desc")
                .price(BigDecimal.valueOf(100))
                .imageUrl("img.jpg")
                .build();
        return productRepo.save(product);
    }

    private Order createOrder(OrderStatus status) {
        Order order = Order.builder()
                .deliveryAddress("Street 1")
                .contactPhone("123456789")
                .deliveryMethod(DeliveryMethod.BY_CAR)
                .status(status)
                .user(testUser)
                .build();
        return orderRepo.save(order);
    }
}
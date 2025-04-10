package org.telran.online_store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.telran.online_store.enums.OrderStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    private int userId;

    @CreatedDate
    private LocalDateTime createdAt;

    private String deliveryAddress;

    private String contactPhone;

    private String deliveryMethod;

    private OrderStatus orderStatus;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}

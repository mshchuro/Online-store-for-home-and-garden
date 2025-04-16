package org.telran.online_store.controller;

import jakarta.servlet.ServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telran.online_store.entity.Order;
import org.telran.online_store.entity.User;
import org.telran.online_store.service.OrderService;
import org.telran.online_store.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

}

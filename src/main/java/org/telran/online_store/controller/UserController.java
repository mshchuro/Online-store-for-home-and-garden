package org.telran.online_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telran.online_store.service.UserService;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;
}
//getAll, create, getById, delete
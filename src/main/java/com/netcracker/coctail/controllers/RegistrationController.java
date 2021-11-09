package com.netcracker.coctail.controllers;

import java.util.Collection;

import com.netcracker.coctail.model.Users;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/users")
public class RegistrationController {

    @Resource
    com.netcracker.coctail.dao.PostgresRegistrationDao PostgresRegistrationDao;

    public RegistrationController(com.netcracker.coctail.dao.PostgresRegistrationDao postgresRegistrationDao) {
        this.PostgresRegistrationDao = postgresRegistrationDao;
    }



    @GetMapping(value = "/usersList")
    public Collection<Users> getAll() {
        return PostgresRegistrationDao.getAll();
    }

    @PostMapping(value = "/createUser")
    public void create(@RequestBody Users user) {
        PostgresRegistrationDao.create(user);
    }

}
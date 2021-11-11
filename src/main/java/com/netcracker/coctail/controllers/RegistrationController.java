package com.netcracker.coctail.controllers;

import java.util.Collection;

import com.netcracker.coctail.model.ReadUser;
import com.netcracker.coctail.model.CreateUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RegistrationController {

    @Resource
    com.netcracker.coctail.dao.PostgresRegistrationDao PostgresRegistrationDao;

    public RegistrationController(com.netcracker.coctail.dao.PostgresRegistrationDao postgresRegistrationDao) {
        this.PostgresRegistrationDao = postgresRegistrationDao;
    }



    @GetMapping(value = "/list")
    public Collection<ReadUser> getAll() {
        return PostgresRegistrationDao.getAll();
    }

    @PostMapping(value = "/users")
    public String create(@RequestBody CreateUser user) {
        return PostgresRegistrationDao.create(user);
    }

}
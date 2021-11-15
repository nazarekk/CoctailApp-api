package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.RegistrationDao;
import com.netcracker.coctail.model.CreateUser;
import com.netcracker.coctail.validators.CreateUserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class RegistrationController {

    @Resource
    RegistrationDao registrationDao;


    private final CreateUserValidator createUserValidator;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(createUserValidator);
    }

    @GetMapping("/activation/{code}")
    public String activateUser(@PathVariable String code) {
        registrationDao.activateUser(code);
        return "Account is activated!";
    }

    @PostMapping
    public String create(@RequestBody @Valid CreateUser user) {
        return registrationDao.create(user);
    }

}
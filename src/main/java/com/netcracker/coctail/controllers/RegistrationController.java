package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.RegistrationDao;
import com.netcracker.coctail.model.CreateUser;
import com.netcracker.coctail.validators.CreateUserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "${front_link}")
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
    public ResponseEntity activateUser(@PathVariable String code) {
        return registrationDao.activateUser(code) == 1 ? new ResponseEntity(HttpStatus.OK) :
            new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping
    public String create(@RequestBody @Valid CreateUser user) {
        return registrationDao.create(user);
    }

}
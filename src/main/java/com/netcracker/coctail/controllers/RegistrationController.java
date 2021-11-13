package com.netcracker.coctail.controllers;

import java.util.Collection;
import java.util.WeakHashMap;

import com.netcracker.coctail.dao.RegistrationDao;
import com.netcracker.coctail.model.ActivateUser;
import com.netcracker.coctail.model.ReadUser;
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
    RegistrationDao RegistrationDao;

    private final RegistrationDao registrationDao;
    private final CreateUserValidator createUserValidator;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        dataBinder.setValidator(createUserValidator);
    }

    /*@GetMapping
    public Collection<ReadUser> getAll() {
        return RegistrationDao.getAll();
    }*/

    @GetMapping("/activation/{code}")
    public String activate(@PathVariable String code){
        boolean isActivated = RegistrationDao.activateUser(code);
        return "Molodec";
    }

    @PostMapping
    public String create(@RequestBody @Valid CreateUser user) {
        return RegistrationDao.create(user);
    }

}
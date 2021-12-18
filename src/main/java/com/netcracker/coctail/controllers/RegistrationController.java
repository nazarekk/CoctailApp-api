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
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class RegistrationController {

  private final CreateUserValidator createUserValidator;

  @Resource
  RegistrationDao registrationDao;

  @InitBinder
  public void initBinder(WebDataBinder dataBinder) {
    dataBinder.setValidator(createUserValidator);
  }

  @PostMapping
  public String create(@RequestBody @Valid CreateUser user) {
    return registrationDao.create(user);
  }

}
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
  public ResponseEntity create(@RequestBody CreateUser user) {
    return registrationDao.create(user).isEmpty() ? new ResponseEntity(HttpStatus.NOT_MODIFIED) :
        new ResponseEntity(HttpStatus.OK);
  }

}
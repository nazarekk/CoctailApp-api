package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.RegistrationDao;
import com.netcracker.coctail.model.ActivateUser;
import com.netcracker.coctail.model.CreateUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class RegistrationController {

  @Resource
  RegistrationDao registrationDao;

  @PatchMapping("/activation")
  public ResponseEntity activateUser(@RequestBody ActivateUser user) {
    return registrationDao.activateUser(user) == 1 ? new ResponseEntity(HttpStatus.OK) :
        new ResponseEntity(HttpStatus.NOT_MODIFIED);
  }

  @PostMapping
  public String create(@RequestBody CreateUser user) {
    return registrationDao.create(user);
  }

}
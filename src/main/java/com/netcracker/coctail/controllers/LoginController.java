package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.LoginDao;
import com.netcracker.coctail.model.Login;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class LoginController {

    LoginDao loginDao;

    public LoginController(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    @GetMapping
    Collection<Login> getAll() {
        return loginDao.getAll();
    }

    @CrossOrigin(origins = "https://coctailappfront.herokuapp.com/")
    @PostMapping("/login")
    public String check(@RequestParam(name = "email", required = true, defaultValue = " ") String email,
                        @RequestParam(name = "password", required = true, defaultValue = " ") String password) {
        if (loginDao.checkByEmailAndPassword(email, password)) {
            return "ok";
        } else {
            return "not ok";
        }
    }
}




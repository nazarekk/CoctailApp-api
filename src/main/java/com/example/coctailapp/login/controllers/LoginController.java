package com.example.coctailapp.login.controllers;

import com.example.coctailapp.login.dao.LoginDao;
import com.example.coctailapp.login.model.Login;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class LoginController {

    LoginDao loginDao;

    public LoginController(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    @GetMapping("/login")
    Collection<Login> getAll(){
        return loginDao.getAll();
    }

    @PostMapping("/login")
    public String check(@RequestParam(name = "email", required = true, defaultValue = " ") String email,
                        @RequestParam(name = "password", required = true, defaultValue = " ") String password){
        if(loginDao.getByEmailAndPassword(email,password)){
            return "ok";
        }
        return "not ok";
        }
    }




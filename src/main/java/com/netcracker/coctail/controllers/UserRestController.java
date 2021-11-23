package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.UserDao;
import com.netcracker.coctail.dto.UserDto;
import com.netcracker.coctail.exceptions.DuplicatePasswordException;
import com.netcracker.coctail.exceptions.InvalidPasswordException;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.model.UserInfo;
import com.netcracker.coctail.model.UserPasswords;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;
import com.netcracker.coctail.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/users/")
@CrossOrigin(origins = "*")
@Data
public class UserRestController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping(value = "{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        UserDto result = UserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "info")
    public ResponseEntity<UserInfo> seeMyPersonalData(HttpServletRequest request) {
        String email = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        return new ResponseEntity<>(userDao.myInfo(email), HttpStatus.OK);
    }

    @PatchMapping(value = "settings/edit")
    public ResponseEntity editMyPersonalData(HttpServletRequest request,
                                             @RequestBody @Valid UserInfo user) {
        String email = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        return new ResponseEntity(userDao.editInfo(email, user), HttpStatus.OK);
    }

    @PutMapping(value = "settings")
    public ResponseEntity changePassword(HttpServletRequest request,
                                         @RequestBody @Valid UserPasswords userPasswords) {
        String email = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        User user = userService.getUserByEmail(email);
        if (!passwordEncoder.matches(userPasswords.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        } else if (!userPasswords.getNewPassword().equals(userPasswords.getNewDoublePassword())) {
            throw new DuplicatePasswordException();
        } else {
            userService.changeUserPassword(user, userPasswords.getNewPassword());
            return new ResponseEntity(HttpStatus.OK);
        }
    }
}

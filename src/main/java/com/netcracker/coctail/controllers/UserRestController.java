package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.UserDao;
import com.netcracker.coctail.dto.UserDto;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.model.UserInfo;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;
import com.netcracker.coctail.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users/")
@CrossOrigin(origins = "*")
@Data
public class UserRestController {
  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserDao userDao;

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

  @PatchMapping(value = "edit")
  public ResponseEntity editMyPersonalData(HttpServletRequest request,
                                           @RequestBody @Valid UserInfo user) {
    String email = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    return new ResponseEntity(userDao.editInfo(email, user), HttpStatus.OK);
  }
}

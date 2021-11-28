package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dto.UserDto;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;
import com.netcracker.coctail.service.FriendlistService;
import com.netcracker.coctail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * REST controller user connected requests.
 */

@RestController
@RequestMapping(value = "/api/users/")
@CrossOrigin(origins = "*")
public class UserRestController {

    private final UserService userService;
    private final FriendlistService friendlistService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserRestController(UserService userService, FriendlistService friendlistService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.friendlistService = friendlistService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        UserDto result = UserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping("add/{friendid}")
    public void addFriend(
            @PathVariable(name = "friendid") long friendid,
            HttpServletRequest request) {
        String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        friendlistService.addFriend(ownerEmail, friendid);
    }

    @GetMapping( "find/{nickname}")
    public List<ResponseEntity<UserDto>> getUserByNickname(@PathVariable(name = "nickname") String nickname) {
        List<User> users = friendlistService.getUserByNickname(nickname);
        List<ResponseEntity<UserDto>> list = new ArrayList();
        if (users.isEmpty()) {
            list.add(new ResponseEntity<>(HttpStatus.NO_CONTENT));
            return list;
        }
        for(int i=0; i<users.size(); i++){
            list.add(new ResponseEntity<>(UserDto.fromUser(users.get(i)), HttpStatus.OK));
        }
        return list;
    }

    @PatchMapping("accept/{friendid}")
    public void acceptFriend(
            @PathVariable(name = "friendid") long friendid,
            HttpServletRequest request) {
        String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        friendlistService.acceptFriendRequest(ownerEmail, friendid);
    }

    @PatchMapping("decline/{friendid}")
    public void declineFriend(
            @PathVariable(name = "friendid") long friendid,
            HttpServletRequest request) {
        String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        friendlistService.declineFriendRequest(ownerEmail, friendid);
    }

    @PatchMapping("subscribe/{friendid}")
    public void subcribeTo(
            @PathVariable(name = "friendid") long friendid,
            HttpServletRequest request) {
        String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        friendlistService.subscribeToFriend(ownerEmail, friendid);
    }

    @DeleteMapping("remove/{friendid}")
    public void removeFromFriends(
            @PathVariable(name = "friendid") long friendid,
            HttpServletRequest request) {
        String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        friendlistService.removeFriend(ownerEmail, friendid);
    }
}

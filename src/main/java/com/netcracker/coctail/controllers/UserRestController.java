package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dto.UserDto;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.service.FriendlistService;
import com.netcracker.coctail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller user connected requests.
 */

@RestController
@RequestMapping(value = "/api/users/")
@CrossOrigin(origins = "*")
public class UserRestController {

    private final UserService userService;
    private final FriendlistService friendlistService;

    @Autowired
    public UserRestController(UserService userService, FriendlistService friendlistService) {
        this.userService = userService;
        this.friendlistService = friendlistService;
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


    @PostMapping("{ownerid}/add/{friendid}")
    public void addFriend(
            @PathVariable(name = "ownerid") long ownerid,
            @PathVariable(name = "friendid") long friendid) {
        friendlistService.addFriend(ownerid, friendid);
    }

    @PatchMapping("{ownerid}/accept/{friendid}")
    public void acceptFriend(
            @PathVariable(name = "ownerid") long ownerid,
            @PathVariable(name = "friendid") long friendid) {
        friendlistService.acceptFriendRequest(ownerid, friendid);
    }

    @PatchMapping("{ownerid}/decline/{friendid}")
    public void declineFriend(
            @PathVariable(name = "ownerid") long ownerid,
            @PathVariable(name = "friendid") long friendid) {
        friendlistService.declineFriendRequest(ownerid, friendid);
    }

    @PatchMapping("{ownerid}/subscribe/{friendid}")
    public void subcribeTo(
            @PathVariable(name = "ownerid") long ownerid,
            @PathVariable(name = "friendid") long friendid) {
        friendlistService.subscribeToFriend(ownerid, friendid);
    }

    @DeleteMapping("{ownerid}/remove/{friendid}")
    public void removeFromFriends(
            @PathVariable(name = "ownerid") long ownerid,
            @PathVariable(name = "friendid") long friendid) {
        friendlistService.removeFriend(ownerid, friendid);
    }
}

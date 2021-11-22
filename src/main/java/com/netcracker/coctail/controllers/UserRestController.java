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

/*
 @GetMapping(value = "{ownerid}/{friendid}")
    public ResponseEntity<FriendlistDto> checkFriendById(
            @PathVariable(name = "ownerid") Long ownerid,
            @PathVariable(name = "friendid") Long friendid) {
        User user = userService.getUserById(ownerid);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        User friend = userService.getUserById(friendid);
        if (friend == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        FriendlistDto result = FriendlistDto.fromUser(user,friend);

        return new ResponseEntity<>(result, HttpStatus.OK);
   }
*/

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

    @DeleteMapping("{ownerid}/remove/{friendid}")
    public void removeFromFriends(
            @PathVariable(name = "ownerid") long ownerid,
            @PathVariable(name = "friendid") long friendid) {
            friendlistService.removeFriend(ownerid, friendid);
    }
}

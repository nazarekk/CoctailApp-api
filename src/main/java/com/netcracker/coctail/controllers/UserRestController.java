package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.AdaptiveFriendlistDao;
import com.netcracker.coctail.dao.ModeratorDao;
import com.netcracker.coctail.dto.FriendlistDto;
import com.netcracker.coctail.dto.UserDto;
import com.netcracker.coctail.model.Friendlist;
import com.netcracker.coctail.model.Moderator;
import com.netcracker.coctail.model.ModeratorInformation;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * REST controller user connected requests.
 */

@RestController
@RequestMapping(value = "/api/users/")
@CrossOrigin(origins = "*")
public class UserRestController {

    private final UserService userService;

    @Resource
    AdaptiveFriendlistDao adaptiveFriendlistDao;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
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
    public ResponseEntity<Friendlist> addFriend(
            @PathVariable(name = "ownerid") Long ownerid,
            @PathVariable(name = "friendid") Long friendid) {
        adaptiveFriendlistDao.createFriendlist(ownerid,friendid,2);
        return adaptiveFriendlistDao.createFriendlist(friendid,ownerid,3) == 1 ? new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PatchMapping("{ownerid}/accept/{friendid}")
    public void acceptFriend(
            @PathVariable(name = "ownerid") Long ownerid,
            @PathVariable(name = "friendid") Long friendid) {
            adaptiveFriendlistDao.editFriendlist(ownerid, friendid, 4);
            adaptiveFriendlistDao.editFriendlist(friendid, ownerid, 4);
    }

    @PatchMapping("{ownerid}/decline/{friendid}")
    public void declineFriend(
            @PathVariable(name = "ownerid") Long ownerid,
            @PathVariable(name = "friendid") Long friendid) {
        adaptiveFriendlistDao.editFriendlist(friendid,ownerid,1);
        adaptiveFriendlistDao.editFriendlist(ownerid,friendid,1);
    }

    @DeleteMapping("{ownerid}/revome/{friendid}")
    public void removeFromFriends(
            @PathVariable(name = "ownerid") Long ownerid,
            @PathVariable(name = "friendid") Long friendid) {
        adaptiveFriendlistDao.editFriendlist(ownerid,friendid,1);
        adaptiveFriendlistDao.editFriendlist(ownerid,friendid,1);
    }
}

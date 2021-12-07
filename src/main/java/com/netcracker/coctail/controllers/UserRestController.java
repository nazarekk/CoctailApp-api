package com.netcracker.coctail.controllers;
import com.netcracker.coctail.dao.UserDao;
import com.netcracker.coctail.dto.UserDto;
import com.netcracker.coctail.exceptions.DuplicatePasswordException;
import com.netcracker.coctail.exceptions.InvalidPasswordException;
import com.netcracker.coctail.model.*;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;
import com.netcracker.coctail.service.FriendlistService;
import com.netcracker.coctail.service.RecipeService;
import com.netcracker.coctail.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * REST controller user connected requests.
 */

@RestController
@RequestMapping(value = "/api/users/")
@CrossOrigin(origins = "*")
@Data
public class UserRestController {
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    private UserDao userDao;
    private FriendlistService friendlistService;
    private PasswordEncoder passwordEncoder;
    private RecipeService recipeService;

    @Autowired
    @Lazy
    public UserRestController(UserService userService,
                              JwtTokenProvider jwtTokenProvider,
                              UserDao userDao,
                              FriendlistService friendlistService,
                              PasswordEncoder passwordEncoder,
                              RecipeService recipeService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDao = userDao;
        this.friendlistService = friendlistService;
        this.passwordEncoder = passwordEncoder;
        this.recipeService = recipeService;
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

  @PutMapping(value = "settings")
  public ResponseEntity changePassword(HttpServletRequest request,
                                       @RequestBody @Valid UserPasswords userPasswords) {
    String email = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    System.out.println(userPasswords);
    User user = userService.getUserByEmail(email);
    if (!passwordEncoder.matches(userPasswords.getOldPassword(), user.getPassword())) {
      throw new InvalidPasswordException();
    } else if (!userPasswords.getPassword().equals(userPasswords.getDoubleCheckPass())) {
      throw new DuplicatePasswordException();
    } else {
      userService.changeUserPassword(user, userPasswords.getPassword());
      return new ResponseEntity(HttpStatus.OK);
    }
  }

  @GetMapping(value = "info")
  public ResponseEntity<UserInfo> seeMyPersonalData(HttpServletRequest request) {
    String email = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    return new ResponseEntity<>(userDao.myInfo(email), HttpStatus.OK);
  }

  @PatchMapping(value = "edit")
  public ResponseEntity editMyPersonalData(HttpServletRequest request,
                                           @RequestBody UserInfo user) {
    String email = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    return new ResponseEntity(userDao.editInfo(email, user), HttpStatus.OK);
  }

  @PostMapping("add/{friendid}")
  public ResponseEntity addFriend(
      @PathVariable(name = "friendid") long friendid,
      @RequestHeader("Authorization") String token) {
    String ownerEmail = jwtTokenProvider.getEmail(token.substring(7));
    Boolean ret = friendlistService.addFriend(ownerEmail, friendid);
    return ret == Boolean.TRUE
        ? new ResponseEntity(ret, HttpStatus.OK) :
        new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
  }

  @GetMapping("find")
  public ResponseEntity<List<FriendUser>> getUserByNickname(@RequestParam String nickname,
                                                            HttpServletRequest request) {
    String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    List<FriendUser> users = friendlistService.getUserByNickname(ownerEmail, nickname);
    if (users.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @PatchMapping("accept/{friendid}")
  public ResponseEntity acceptFriend(
      @PathVariable(name = "friendid") long friendid,
      HttpServletRequest request) {
    String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    Boolean ret = friendlistService.acceptFriendRequest(ownerEmail, friendid);
    return ret == Boolean.TRUE
        ? new ResponseEntity(ret, HttpStatus.OK) :
        new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
  }

  @PatchMapping("decline/{friendid}")
  public ResponseEntity declineFriend(
      @PathVariable(name = "friendid") long friendid,
      HttpServletRequest request) {
    String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    Boolean ret = friendlistService.declineFriendRequest(ownerEmail, friendid);
    return ret == Boolean.TRUE
        ? new ResponseEntity(ret, HttpStatus.OK) :
        new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
  }

  @PatchMapping("subscribe/{friendid}")
  public ResponseEntity subcribeTo(
      @PathVariable(name = "friendid") long friendid,
      @RequestHeader("Authorization") String token) {
    String ownerEmail = jwtTokenProvider.getEmail(token.substring(7));
    Boolean ret = friendlistService.subscribeToFriend(ownerEmail, friendid);
    return ret == Boolean.TRUE
        ? new ResponseEntity(ret, HttpStatus.OK) :
        new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
  }

  @DeleteMapping("remove/{friendid}")
  public ResponseEntity removeFromFriends(
      @PathVariable(name = "friendid") long friendid,
      HttpServletRequest request) {
    String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    Boolean ret = friendlistService.removeFriend(ownerEmail, friendid);
    return ret == Boolean.TRUE
        ? new ResponseEntity(ret, HttpStatus.OK) :
        new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
  }
    @GetMapping("recipe/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable(name = "id") int id) {
        Recipe result = recipeService.getRecipeById(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("recipe/list")
    public ResponseEntity<List<DishRecipe>> recipesList() {
        List<DishRecipe> recipes = recipeService.getRecipesByName("");
        if (recipes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @PostMapping(value = "recipe/favourites/{id}")
    public void addToFavourites(@PathVariable(name = "id") int id, HttpServletRequest request) {
        String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        recipeService.addToFavourites(ownerEmail, id);
    }

    @PatchMapping("recipe/{id}")
    public void likeRecipe(
            @PathVariable(name = "id") int id,
            @RequestParam boolean like,
            HttpServletRequest request) {
            String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
            recipeService.likeRecipe(ownerEmail, id, like);
    }

}

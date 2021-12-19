package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.RegistrationDao;
import com.netcracker.coctail.dao.UserDao;
import com.netcracker.coctail.dto.UserDto;
import com.netcracker.coctail.model.ActivateUser;
import com.netcracker.coctail.model.CreateEvent;
import com.netcracker.coctail.model.DishRecipe;
import com.netcracker.coctail.model.Event;
import com.netcracker.coctail.model.EventInfo;
import com.netcracker.coctail.model.FriendUser;
import com.netcracker.coctail.model.Ingredient;
import com.netcracker.coctail.model.StockIngredientInfo;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.model.UserInfo;
import com.netcracker.coctail.model.UserPasswords;
import com.netcracker.coctail.model.UserPersonalInfo;
import com.netcracker.coctail.model.UserPhoto;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.netcracker.coctail.service.FriendlistService;
import com.netcracker.coctail.service.IngredientService;
import com.netcracker.coctail.service.PersonalStockService;
import com.netcracker.coctail.service.RecipeService;
import com.netcracker.coctail.service.UserService;
import com.netcracker.coctail.service.EventService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;


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
    private PersonalStockService personalStockService;
    private IngredientService ingredientService;
    private EventService eventService;
    private RegistrationDao registrationDao;

    @Autowired
    @Lazy
    public UserRestController(UserService userService,
                              JwtTokenProvider jwtTokenProvider,
                              UserDao userDao,
                              FriendlistService friendlistService,
                              PasswordEncoder passwordEncoder,
                              RecipeService recipeService,
                              PersonalStockService personalStockService,
                              EventService eventService,
                              IngredientService ingredientService,
                              RegistrationDao registrationDao) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDao = userDao;
        this.friendlistService = friendlistService;
        this.passwordEncoder = passwordEncoder;
        this.recipeService = recipeService;
        this.personalStockService = personalStockService;
        this.eventService = eventService;
        this.ingredientService = ingredientService;
        this.registrationDao = registrationDao;
    }

    @PatchMapping("/activation")
    public ResponseEntity activateUser(@RequestBody ActivateUser user) {
        return registrationDao.activateUser(user) == 1 ? new ResponseEntity(HttpStatus.OK) :
                new ResponseEntity(HttpStatus.NOT_MODIFIED);
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
    public ResponseEntity addFriend(
            @PathVariable(name = "friendid") long friendid,
            HttpServletRequest request) {
        String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        Boolean ret = friendlistService.addFriend(ownerEmail, friendid);
        return ret == Boolean.TRUE
                ? new ResponseEntity(ret, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("find")
    public ResponseEntity<List<FriendUser>> getUserByNickname(@RequestParam String nickname,
                                                              HttpServletRequest request) {
        String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        List<FriendUser> users = friendlistService.showAllUsers(ownerEmail, nickname);
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("friendlist")
    public ResponseEntity<List<FriendUser>> getFriendList(HttpServletRequest request) {
        String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        List<FriendUser> users = friendlistService.friendList(ownerEmail);
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

    @PatchMapping("unsubscribe/{friendid}")
    public ResponseEntity unsubcribeFrom(
            @PathVariable(name = "friendid") long friendid,
            @RequestHeader("Authorization") String token) {
        String ownerEmail = jwtTokenProvider.getEmail(token.substring(7));
        Boolean ret = friendlistService.unsubcribeFromFriend(ownerEmail, friendid);
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

    @GetMapping("ingredients/list")
    public ResponseEntity<List<Ingredient>> ingredientsList() {
        List<Ingredient> ingredients = ingredientService.getIngredientByName("");
        if (ingredients.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ingredients, HttpStatus.OK);
    }

    @GetMapping("recipe")
    public ResponseEntity<List<DishRecipe>> getRecipeByName(@RequestParam String name,
                                                            HttpServletRequest httpServletRequest) {
        List<DishRecipe> recipes = recipeService.getRecipesByName(name, httpServletRequest);
        if (recipes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping(value = "info")
    public ResponseEntity<UserInfo> seeMyPersonalData(HttpServletRequest request) {
        String email = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        return new ResponseEntity<>(userDao.myInfo(email), HttpStatus.OK);
    }

    @PutMapping(value = "settings")
    public ResponseEntity<?> changePassword(HttpServletRequest request,
                                            @RequestBody @Valid UserPasswords userPasswords) {
        String email = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        User user = userService.getUserByEmail(email);
        userService.checkPassword(user, userPasswords);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "settings/edit")
    public ResponseEntity<?> editMyPersonalData(HttpServletRequest request,
                                                @RequestBody @Valid UserPersonalInfo user) {
        String email = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        userService.changeInfo(email, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("settings/edit")
    public ResponseEntity<?> editUserPhoto(HttpServletRequest request,
                                           @RequestBody UserPhoto user) {
        String email = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        return new ResponseEntity<>(userService.changeUserPhoto(email, user), HttpStatus.OK);
    }

    @GetMapping("settings/edit")
    public ResponseEntity<UserPersonalInfo> getInformationInSettings(HttpServletRequest request) {
        String email = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
        return new ResponseEntity<>(userDao.getInfo(email), HttpStatus.OK);
    }

  @GetMapping("recipe/filter")
  public ResponseEntity<List<DishRecipe>> getRecipesFiltered(
      @RequestParam String sugarless, @RequestParam String alcohol,
      HttpServletRequest httpServletRequest) {
    List<DishRecipe> recipes =
        recipeService.getRecipesFiltered(sugarless, alcohol, httpServletRequest);
    if (recipes.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(recipes, HttpStatus.OK);
  }

    @PostMapping(value = "stock/ingredients")
    public ResponseEntity addIngredient(@RequestHeader("Authorization") String token,
                                        @RequestParam long ingredientId,
                                        @RequestParam long quantity) {
        long userId = personalStockService.getOwnerIdByToken(token);
        boolean result = personalStockService.addIngredientToStock(userId, ingredientId, quantity);
        return result == Boolean.TRUE
                ? new ResponseEntity(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("stock/{ingredientid}")
    public ResponseEntity removeStockIngredient(
            @RequestHeader("Authorization") String token,
            @PathVariable(name = "ingredientid") long ingredientId) {
        long userId = personalStockService.getOwnerIdByToken(token);
        boolean result = personalStockService.removeIngredientFromStock(userId, ingredientId);
        return result == Boolean.TRUE
                ? new ResponseEntity(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = "stock/edit")
    public ResponseEntity editStockIngredient(
            @RequestHeader("Authorization") String token, @RequestParam long ingredientId,
            @RequestParam long quantity) {
        long userId = personalStockService.getOwnerIdByToken(token);
        boolean result = personalStockService.editIngredient(userId, ingredientId, quantity);
        return result == Boolean.TRUE
                ? new ResponseEntity(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "stock/my")
    public ResponseEntity<List<StockIngredientInfo>> getAllUserIngredients(
            @RequestHeader("Authorization") String token) {
        long userId = personalStockService.getOwnerIdByToken(token);
        List<StockIngredientInfo> stockIngredients =
                personalStockService.getStockIngredientsByName(userId, "");
        if (stockIngredients.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(stockIngredients, HttpStatus.OK);
    }

    @GetMapping("stock/search")
    public ResponseEntity<List<StockIngredientInfo>> getStockIngredientsByName(
            @RequestHeader("Authorization") String token,
            @RequestParam String name) {
        long userId = personalStockService.getOwnerIdByToken(token);
        List<StockIngredientInfo> stockIngredients =
                personalStockService.getStockIngredientsByName(userId, name);
        if (stockIngredients.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(stockIngredients, HttpStatus.OK);
    }

    @GetMapping("ingredients/search")
    public ResponseEntity<List<Ingredient>> getIngredientsByName(@RequestParam String name) {
        List<Ingredient> ingredients = ingredientService.getIngredientByName(name);
        if (ingredients.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ingredients, HttpStatus.OK);
    }

    @GetMapping("ingredients/filter")
    public ResponseEntity<List<Ingredient>> getIngredientsFiltered(
            @RequestParam String type,
            @RequestParam String category,
            @RequestParam String active) {
        List<Ingredient> ingredients = ingredientService.getIngredientFiltered(type, category, active);
        if (ingredients.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ingredients, HttpStatus.OK);
    }

    @GetMapping("stock/filter")
    public ResponseEntity<List<StockIngredientInfo>> getStockIngredientsFiltered(
            @RequestHeader("Authorization") String token, @RequestParam String type,
            @RequestParam String category) {
        long userId = personalStockService.getOwnerIdByToken(token);
        List<StockIngredientInfo> stockIngredients =
                personalStockService.getStockIngredientsFiltered(userId, type, category);
        if (stockIngredients.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(stockIngredients, HttpStatus.OK);
    }

    @GetMapping("recipe/list")
    public ResponseEntity<List<DishRecipe>> recipesList(HttpServletRequest request) {
        List<DishRecipe> recipes = recipeService.getRecipesByName("", request);
        if (recipes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

  @GetMapping(value = "recipe/{id}")
  public ResponseEntity<DishRecipe> getRecipeById(@PathVariable(name = "id") int id) {
    DishRecipe result = recipeService.getRecipeById(id);
    if (result == null) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PatchMapping(value = "recipe/favourites/{id}")
  public ResponseEntity addToFavourites(@PathVariable(name = "id") int id,
                                        @RequestParam boolean favourite,
                                        HttpServletRequest request) {
    String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    if (recipeService.addToFavourites(ownerEmail, id, favourite)) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
  }

  @PatchMapping("recipe/{id}")
  public ResponseEntity likeRecipe(
      @PathVariable(name = "id") int id,
      @RequestParam boolean like,
      HttpServletRequest request) {
    String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    if (recipeService.likeRecipe(ownerEmail, id, like)) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
  }

  @GetMapping("recipe/suggestion")
  public ResponseEntity<List<DishRecipe>> getSuggestion(@RequestHeader("Authorization") String header) {
    List<DishRecipe> result = recipeService.getSuggestion(header);
    return result.isEmpty()
        ? new ResponseEntity(HttpStatus.NO_CONTENT) :
        new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping(value = "events/{id}")
  public ResponseEntity<EventInfo> eventInfo(@PathVariable(name = "id") int id) {
    EventInfo result = eventService.eventInfo(id);
    if (result == null) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping(value = "my_events/{id}")
  public ResponseEntity isCreator(@PathVariable(name = "id") int id, HttpServletRequest request) {
     String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
      if (!eventService.isCreator(id, ownerEmail)) {
          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(value = "events/list")
  public ResponseEntity<List<Event>> eventsList() {
    List<Event> events = eventService.getEventsByName("");
    if (events.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(events, HttpStatus.OK);
  }

  @GetMapping(value = "events/filter")
  public ResponseEntity<List<Event>> getEventsFiltered(HttpServletRequest request) {
    String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    List<Event> events = eventService.getEventsFiltered(ownerEmail);
    if (events.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(events, HttpStatus.OK);
  }

  @PostMapping(value = "events")
  public ResponseEntity<Integer> createEvent(@RequestBody CreateEvent event,
                                             HttpServletRequest request) {
    String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    int id = eventService.createEvent(ownerEmail, event);
    if (id > 0) {
      return new ResponseEntity<>(id, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
  }

  @PostMapping(value = "join/{id}")
  public ResponseEntity joinEvent(@PathVariable(name = "id") int id, HttpServletRequest request) {
    String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    if (eventService.joinEvent(ownerEmail, id)) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
  }

  @PostMapping(value = "events/{id}/recipe")
  public ResponseEntity addRecipeToEvent(@PathVariable(name = "id") int id,
                                         @RequestParam String name, HttpServletRequest request) {
    String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    if (eventService.addRecipeToEvent(id, name, ownerEmail)) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
  }

  @DeleteMapping(value = "leave/{id}")
  public ResponseEntity leaveEvent(@PathVariable(name = "id") int id, HttpServletRequest request) {
    String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    if (eventService.leaveEvent(ownerEmail, id)) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
  }

  @DeleteMapping(value = "events/{id}/recipe")
  public ResponseEntity removeRecipeFromEvent(@PathVariable(name = "id") int id,
                                              @RequestParam String name,
                                              HttpServletRequest request) {
    String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    if (eventService.removeRecipeFromEvent(id, name, ownerEmail)) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
  }

  @PatchMapping(value = "events/{id}")
  public ResponseEntity editEvent(@RequestBody CreateEvent event, @PathVariable(name = "id") int id,
                                  HttpServletRequest request) {
    String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    if (eventService.editEvent(ownerEmail, event, id)) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
  }

  @DeleteMapping(value = "events/{id}")
  public ResponseEntity declineEvent(@PathVariable(name = "id") int id,
                                     HttpServletRequest request) {
    String ownerEmail = jwtTokenProvider.getEmail(request.getHeader("Authorization").substring(7));
    if (eventService.declineEvent(ownerEmail, id)) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
  }

}

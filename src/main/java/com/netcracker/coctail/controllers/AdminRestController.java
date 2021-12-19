package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.ModeratorDao;
import com.netcracker.coctail.dto.AdminUserDto;
import com.netcracker.coctail.model.Moderator;
import com.netcracker.coctail.model.ModeratorInformation;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.service.UserService;

import java.util.Collection;
import javax.annotation.Resource;
import javax.validation.Valid;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for ROLE_ADMIN requests.
 */

@RestController
@RequestMapping(value = "/api/admin/")
@Data
@CrossOrigin(origins = "*")
public class AdminRestController {

  private final UserService userService;

  @Resource
  ModeratorDao createModeratorDao;

  @GetMapping(value = "users/{id}")
  public ResponseEntity<AdminUserDto> getUserById(@PathVariable(name = "id") Long id) {
    User user = userService.getUserById(id);

    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    AdminUserDto result = AdminUserDto.fromUser(user);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("moderators")
  public ResponseEntity<Moderator> createModerator(@RequestBody @Valid Moderator user) {
    return createModeratorDao.create(user) == 1 ? new ResponseEntity<>(HttpStatus.OK) :
        new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
  }

  @GetMapping("moderators")
  public Collection<ModeratorInformation> moderatorList() {
    return createModeratorDao.moderatorList();
  }

  @PatchMapping("moderator/edit")
  public ResponseEntity editModerator(@RequestBody @Valid ModeratorInformation user) {
    return createModeratorDao.editModerator(user) == 1 ? new ResponseEntity(HttpStatus.OK):
        new ResponseEntity(HttpStatus.NOT_MODIFIED);

  }

  @DeleteMapping("moderators/remove")
  public ResponseEntity removeModerator(@RequestBody ModeratorInformation user) {
    Boolean result = createModeratorDao.removeModerator(user);
    return result == true
        ? new ResponseEntity(HttpStatus.OK) :
        new ResponseEntity(result, HttpStatus.NOT_MODIFIED);
  }

  @GetMapping("moderators/search")
  public ResponseEntity<ModeratorInformation> searchModerator(@RequestParam String q) {
    return createModeratorDao.searchModerator(q) == null
        ? new ResponseEntity<>(HttpStatus.NO_CONTENT) :
        new ResponseEntity<>(createModeratorDao.searchModerator(q), HttpStatus.OK);
  }

  @GetMapping("moderators/filter")
  public ResponseEntity<ModeratorInformation> filterModerator(@RequestParam Boolean q) {
    ModeratorInformation result = createModeratorDao.filterModerator(q);
    return result == null
        ? new ResponseEntity<>(HttpStatus.NO_CONTENT) :
        new ResponseEntity<>(result, HttpStatus.OK);
  }
}

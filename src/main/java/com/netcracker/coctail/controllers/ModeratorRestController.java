package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.CreateModeratorDao;
import com.netcracker.coctail.model.ActivateModerator;
import com.netcracker.coctail.model.Moderator;
import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/moderators/")
@Data
public class ModeratorRestController {

  private final CreateModeratorDao createModeratorDao;

  @PostMapping("activation")
  public String activateModerator(@RequestBody ActivateModerator moderator) {
    createModeratorDao.activateModerator(moderator);
    return "Account is activated!";
  }
}
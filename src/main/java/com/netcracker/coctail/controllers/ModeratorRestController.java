package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.ModeratorDao;
import com.netcracker.coctail.model.ActivateModerator;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/moderators/")
@Data
public class ModeratorRestController {

  private final ModeratorDao createModeratorDao;

  @CrossOrigin(origins = "${front_link}")
  @PostMapping("activation")
  public String activateModerator(@RequestBody ActivateModerator moderator) {
    createModeratorDao.activateModerator(moderator);
    return "Account is activated!";
  }
}
package com.netcracker.coctail.rest;

import com.netcracker.coctail.dao.CreateModeratorDao;
import com.netcracker.coctail.model.ModeratorConfirmation;
import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/moderators/")
@Data
public class ModeratorRestController {

  CreateModeratorDao createModeratorDao;

  @PostMapping("activation/{code}")
  public String activateModerator(@PathVariable String code, ModeratorConfirmation user) {
    createModeratorDao.activateModerator(code, user);
    return "Account is activated!";
  }
}

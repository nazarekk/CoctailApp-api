package com.netcracker.coctail.rest;

import com.netcracker.coctail.dao.CreateModeratorDao;
import com.netcracker.coctail.dao.RegistrationDao;
import com.netcracker.coctail.dto.AdminUserDto;
import com.netcracker.coctail.model.CreateUser;
import com.netcracker.coctail.model.Moderator;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.service.UserService;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for ROLE_ADMIN requests.
 */

@RestController
@RequestMapping(value = "/api/admin/")
@Data
public class AdminRestController {

    private final UserService userService;

    @Resource
    CreateModeratorDao createModeratorDao;

    @GetMapping(value = "users/{id}")
    public ResponseEntity<AdminUserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        AdminUserDto result = AdminUserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("moderators")
    public String createModerator(@RequestBody @Valid Moderator user) {
        return createModeratorDao.create(user);
    }

    @GetMapping("moderators")
    public String moderatorList() {
        return "";
    }
}

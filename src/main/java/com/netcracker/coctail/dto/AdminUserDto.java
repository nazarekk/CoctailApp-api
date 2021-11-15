package com.netcracker.coctail.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import com.netcracker.coctail.model.User;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminUserDto {
    private Long id;
    private String nickname;
    private String email;
    private Long roleid;
    private boolean isActive;


    public static AdminUserDto fromUser(User user) {
        AdminUserDto adminUserDto = new AdminUserDto();
        adminUserDto.setId(user.getId());
        adminUserDto.setNickname(user.getNickname());
        adminUserDto.setEmail(user.getEmail());
        return adminUserDto;
    }
}

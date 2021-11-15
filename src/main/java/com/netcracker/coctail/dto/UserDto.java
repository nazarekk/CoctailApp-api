package com.netcracker.coctail.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import com.netcracker.coctail.model.User;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private String nickname;
    private String email;


    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setNickname(user.getNickname());
        userDto.setEmail(user.getEmail());

        return userDto;
    }
}

package com.netcracker.coctail.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.netcracker.coctail.model.User;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendlistDto {
    private long ownerid;
    private long friendid;


    public static FriendlistDto fromUser(User user, User friend) {
        FriendlistDto friendlistDto = new FriendlistDto();
        friendlistDto.setOwnerid(user.getId());
        friendlistDto.setFriendid(friend.getId());
        return friendlistDto;
    }
}

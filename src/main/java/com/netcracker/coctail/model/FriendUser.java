package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendUser {
    private long id;
    private String nickname;
    private String email;
    private long statusId;
    private String image;
}

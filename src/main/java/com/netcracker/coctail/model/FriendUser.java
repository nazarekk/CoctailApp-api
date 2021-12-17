package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendUser {
    private final long id;
    private final String nickname;
    private final String email;
    private final long statusId;
    private final String image;
}

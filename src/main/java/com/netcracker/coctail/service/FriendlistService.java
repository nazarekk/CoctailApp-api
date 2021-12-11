package com.netcracker.coctail.service;

import com.netcracker.coctail.model.FriendUser;
import com.netcracker.coctail.model.Friendlist;

import java.util.List;

public interface FriendlistService {
    Friendlist getFriendlist(String ownerEmail, long friendid);
    Boolean addFriend(String ownerEmail, long friendid);
    Boolean acceptFriendRequest(String ownerEmail, long friendid);
    Boolean declineFriendRequest(String ownerEmail, long friendid);
    Boolean removeFriend(String ownerEmail, long friendid);
    Boolean subscribeToFriend(String ownerEmail, long friendid);
    List<FriendUser> getUserByNickname(String ownerEmail, String nickname);
}

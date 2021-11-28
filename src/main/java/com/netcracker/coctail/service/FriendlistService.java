package com.netcracker.coctail.service;

import com.netcracker.coctail.model.Friendlist;
import com.netcracker.coctail.model.User;

import java.util.List;

public interface FriendlistService {
    Friendlist getFriendlist(String ownerEmail, long friendid);
    void addFriend(String ownerEmail, long friendid);
    void acceptFriendRequest(String ownerEmail, long friendid);
    void declineFriendRequest(String ownerEmail, long friendid);
    void removeFriend(String ownerEmail, long friendid);
    void subscribeToFriend(String ownerEmail, long friendid);
    List<User> getUserByNickname(String nickname);
}

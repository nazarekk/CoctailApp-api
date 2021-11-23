package com.netcracker.coctail.service;

import com.netcracker.coctail.model.Friendlist;

public interface FriendlistService {
    Friendlist getFriendlist(long ownerid, long friendid);
    void addFriend(long ownerid, long friendid);
    void acceptFriendRequest(long ownerid, long friendid);
    void declineFriendRequest(long ownerid, long friendid);
    void removeFriend(long ownerid, long friendid);
    void subscribeToFriend(long ownerid, long friendid);

}

package com.netcracker.coctail.dao;


public interface AdaptiveFriendlistDao {

    int createFriendlist(long ownerid, long friendid, long statusid);

    void editFriendlist(long ownerid, long friendid, long statusid);

    void removeFriendlist(long ownerid, long friendid);
}

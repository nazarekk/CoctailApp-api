package com.netcracker.coctail.dao;


import com.netcracker.coctail.model.Friendlist;

import java.util.List;

public interface FriendlistDao {

    List<Friendlist> findFriendlist(long ownerid, long friendid);

    int createFriendlist(long ownerid, long friendid, long statusid);

    void editFriendlist(long ownerid, long friendid, long statusid);

    void removeFriendlist(long ownerid, long friendid);

    long getStatusId(String status);
}

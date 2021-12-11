package com.netcracker.coctail.dao;


import com.netcracker.coctail.model.Friendlist;
import com.netcracker.coctail.model.User;

import java.util.List;

public interface FriendlistDao {

    List<Friendlist> findFriendlist(long ownerid, long friendid);

    void createFriendlist(long ownerid, long friendid, long statusid);

    void editFriendlist(long ownerid, long friendid, long statusid);

    void removeFriendlist(long ownerid, long friendid);

    long getStatusId(String status);

    long getOwnerId(String email);

    List<User> getOwnerByNickname(String nickname);
}

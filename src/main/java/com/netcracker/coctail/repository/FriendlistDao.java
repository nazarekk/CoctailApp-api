package com.netcracker.coctail.repository;

import com.netcracker.coctail.model.Friendlist;
import com.netcracker.coctail.model.User;

import java.util.List;

public interface FriendlistDao {
    List<Friendlist> findFriendlist(long ownerid, long friendid);
}

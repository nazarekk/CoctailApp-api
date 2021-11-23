package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.FriendlistDao;
import com.netcracker.coctail.exceptions.*;
import com.netcracker.coctail.model.Friendlist;
import com.netcracker.coctail.repository.UserDao;
import com.netcracker.coctail.service.FriendlistService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@Data
public class FriendlistServiceImpl implements FriendlistService {

    private final UserDao userDao;
    private final JdbcTemplate jdbcTemplate;
    private final FriendlistDao friendlistDao;

    @Override
    public Friendlist getFriendlist(long ownerid, long friendid) {
        List<Friendlist> result = friendlistDao.findFriendlist(ownerid, friendid);
        return result.get(0);
    }

    @Override
    public void addFriend(long ownerid, long friendid) {
        if (friendlistDao.findFriendlist(ownerid, friendid).isEmpty()) {
            friendlistDao.createFriendlist(ownerid, friendid, friendlistDao.getStatusId("Waiting for response"));
            friendlistDao.createFriendlist(friendid, ownerid, friendlistDao.getStatusId("Awaiting confirmation"));
        } else if (friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() == friendlistDao.getStatusId("None")) {
            friendlistDao.editFriendlist(ownerid, friendid, friendlistDao.getStatusId("Waiting for response"));
            friendlistDao.editFriendlist(friendid, ownerid, friendlistDao.getStatusId("Awaiting confirmation"));
        } else if (friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() == friendlistDao.getStatusId("Awaiting confirmation")) {
            throw new UserAlreadyAwaitsYourResponseException();
        } else if (friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() == friendlistDao.getStatusId("Waiting for response")) {
            throw new AwaitingConfirmationException();
        } else if (friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() == friendlistDao.getStatusId("Friends") ||
                   friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() == friendlistDao.getStatusId("Subscribed to")) {
            throw new AlreadyFriendsException();
        }

    }

    @Override
    public void acceptFriendRequest(long ownerid, long friendid) {
        if (friendlistDao.findFriendlist(ownerid, friendid).isEmpty() || friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() != friendlistDao.getStatusId("Awaiting confirmation")) {
            throw new FriendRequestNotFoundException();
        } else {
            friendlistDao.editFriendlist(ownerid, friendid, friendlistDao.getStatusId("Friends"));
            friendlistDao.editFriendlist(friendid, ownerid, friendlistDao.getStatusId("Friends"));
        }


    }

    @Override
    public void declineFriendRequest(long ownerid, long friendid) {
        if (friendlistDao.findFriendlist(ownerid, friendid).isEmpty() || friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() != friendlistDao.getStatusId("Awaiting confirmation")) {
            throw new FriendRequestNotFoundException();
        } else {
            friendlistDao.editFriendlist(ownerid, friendid, friendlistDao.getStatusId("None"));
            friendlistDao.editFriendlist(friendid, ownerid, friendlistDao.getStatusId("None"));
        }
    }

    @Override
    public void subscribeToFriend(long ownerid, long friendid) {
        if (friendlistDao.findFriendlist(ownerid, friendid).isEmpty() || friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() != friendlistDao.getStatusId("Friends")) {
            throw new NotInFriendlistException();
        } else {
            friendlistDao.editFriendlist(ownerid, friendid, friendlistDao.getStatusId("Subscribed to"));
        }
    }

    @Override
    public void removeFriend(long ownerid, long friendid) {
        if (friendlistDao.findFriendlist(ownerid, friendid).isEmpty()
                || (friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() != friendlistDao.getStatusId("Friends")
                && friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() != friendlistDao.getStatusId("Subscribed to"))) {
            throw new NotInFriendlistException();
        } else {
            friendlistDao.editFriendlist(ownerid, friendid, friendlistDao.getStatusId("None"));
            friendlistDao.editFriendlist(friendid, ownerid, friendlistDao.getStatusId("None"));
        }
    }

}

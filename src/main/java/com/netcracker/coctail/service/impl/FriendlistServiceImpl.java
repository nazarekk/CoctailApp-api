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
            friendlistDao.createFriendlist(ownerid, friendid, 3);
            friendlistDao.createFriendlist(friendid, ownerid, 2);
        } else if (friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() == 1) {
            friendlistDao.editFriendlist(ownerid, friendid, 3);
            friendlistDao.editFriendlist(friendid, ownerid, 2);
        } else if (friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() == 2) {
            throw new UserAlreadyAwaitsYourResponseException();
        } else if (friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() == 3) {
            throw new AwaitingConfirmationException();
        } else if (friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() == 4) {
            throw new AlreadyFriendsException();
        }

    }

    @Override
    public void acceptFriendRequest(long ownerid, long friendid) {
        if (friendlistDao.findFriendlist(ownerid, friendid).isEmpty() || friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() != 2) {
            throw new FriendRequestNotFoundException();
        } else {
            friendlistDao.editFriendlist(ownerid, friendid, 4);
            friendlistDao.editFriendlist(friendid, ownerid, 4);
        }


    }

    @Override
    public void declineFriendRequest(long ownerid, long friendid) {
        if (friendlistDao.findFriendlist(ownerid, friendid).isEmpty() || friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() != 2) {
            throw new FriendRequestNotFoundException();
        } else {
            friendlistDao.editFriendlist(ownerid, friendid, 1);
            friendlistDao.editFriendlist(friendid, ownerid, 1);
        }
    }

    @Override
    public void removeFriend(long ownerid, long friendid) {
        if (friendlistDao.findFriendlist(ownerid, friendid).isEmpty() || friendlistDao.findFriendlist(ownerid, friendid).get(0).getStatusid() != 4) {
            throw new NotInFriendlistException();
        } else {
            friendlistDao.editFriendlist(ownerid, friendid, 1);
            friendlistDao.editFriendlist(friendid, ownerid, 1);
        }
    }

}

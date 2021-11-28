package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.FriendlistDao;
import com.netcracker.coctail.exceptions.*;
import com.netcracker.coctail.model.Friendlist;
import com.netcracker.coctail.model.User;
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

    private boolean check(long ownerId, long friendId, long status) {
        return (friendlistDao.findFriendlist(ownerId, friendId).get(0).getStatusid() == status);
    }

    private long status(String statusName) {
        return friendlistDao.getStatusId(statusName);
    }

    private enum Id {
        NONE("None"),
        CONFIRM("Awaiting confirmation"),
        WAITING("Waiting for response"),
        FRIENDS("Friends"),
        SUBSCRIBED("Subscribed to");

        private final String status;

        Id(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    @Override
    public Friendlist getFriendlist(String ownerEmail, long friendId) {
        long ownerId = friendlistDao.getOwnerId(ownerEmail);
        List<Friendlist> result = friendlistDao.findFriendlist(ownerId, friendId);
        return result.get(0);
    }

    @Override
    public void addFriend(String ownerEmail, long friendId) {

        long notFriends = status(Id.NONE.getStatus());
        long confirm = status(Id.CONFIRM.getStatus());
        long waiting = status(Id.WAITING.getStatus());
        long ownerId = friendlistDao.getOwnerId(ownerEmail);

        if (friendlistDao.findFriendlist(ownerId, friendId).isEmpty()) {
            friendlistDao.createFriendlist(ownerId, friendId, waiting);
            friendlistDao.createFriendlist(friendId, ownerId, confirm);
        } else if (check(ownerId, friendId, notFriends)) {
            friendlistDao.editFriendlist(ownerId, friendId, waiting);
            friendlistDao.editFriendlist(friendId, ownerId, confirm);
        } else if (check(ownerId, friendId, confirm)) {
            throw new UserAlreadyAwaitsYourResponseException();
        } else if (check(ownerId, friendId, waiting)) {
            throw new AwaitingConfirmationException();
        } else {
            throw new AlreadyFriendsException();
        }

    }

    @Override
    public void acceptFriendRequest(String ownerEmail, long friendId) {

        long confirm = status(Id.CONFIRM.getStatus());
        long friends = status(Id.FRIENDS.getStatus());
        long ownerId = friendlistDao.getOwnerId(ownerEmail);

        if (friendlistDao.findFriendlist(ownerId, friendId).isEmpty()
                || !check(ownerId, friendId, confirm)) {
            throw new FriendRequestNotFoundException();
        } else {
            friendlistDao.editFriendlist(ownerId, friendId, friends);
            friendlistDao.editFriendlist(friendId, ownerId, friends);
        }
    }

    @Override
    public void declineFriendRequest(String ownerEmail, long friendId) {

        long confirm = status(Id.CONFIRM.getStatus());
        long notFriends = status(Id.NONE.getStatus());
        long ownerId = friendlistDao.getOwnerId(ownerEmail);

        if (friendlistDao.findFriendlist(ownerId, friendId).isEmpty()
                || !check(ownerId, friendId, confirm)) {
            throw new FriendRequestNotFoundException();
        } else {
            friendlistDao.editFriendlist(ownerId, friendId, notFriends);
            friendlistDao.editFriendlist(friendId, ownerId, notFriends);
        }
    }

    @Override
    public void subscribeToFriend(String ownerEmail, long friendId) {

        long friends = status(Id.FRIENDS.getStatus());
        long subscribed = status(Id.SUBSCRIBED.getStatus());
        long ownerId = friendlistDao.getOwnerId(ownerEmail);

        if (friendlistDao.findFriendlist(ownerId, friendId).isEmpty()
                || !check(ownerId, friendId, friends)) {
            throw new NotInFriendlistException();
        } else {
            friendlistDao.editFriendlist(ownerId, friendId, subscribed);
        }
    }

    @Override
    public void removeFriend(String ownerEmail, long friendId) {

        long notFriends = status(Id.NONE.getStatus());
        long friends = status(Id.FRIENDS.getStatus());
        long subscribed = status(Id.SUBSCRIBED.name());
        long ownerId = friendlistDao.getOwnerId(ownerEmail);

        if (friendlistDao.findFriendlist(ownerId, friendId).isEmpty()
                || (!check(ownerId, friendId, friends) && !check(ownerId, friendId, subscribed))) {
            throw new NotInFriendlistException();
        } else {
            friendlistDao.editFriendlist(ownerId, friendId, notFriends);
            friendlistDao.editFriendlist(friendId, ownerId, notFriends);
        }
    }

    @Override
    public List<User> getUserByNickname(String nickname){
        log.info("calling dao with input " + nickname);
        return friendlistDao.getOwnerByNickname(nickname);
    }

}

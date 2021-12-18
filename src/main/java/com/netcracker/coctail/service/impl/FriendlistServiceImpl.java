package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.FriendlistDao;
import com.netcracker.coctail.exceptions.AlreadyFriendsException;
import com.netcracker.coctail.exceptions.AwaitingConfirmationException;
import com.netcracker.coctail.exceptions.FriendRequestNotFoundException;
import com.netcracker.coctail.exceptions.NotInFriendlistException;
import com.netcracker.coctail.exceptions.UserAlreadyAwaitsYourResponseException;
import com.netcracker.coctail.model.FriendUser;
import com.netcracker.coctail.model.Friendlist;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.dao.UserDao;
import com.netcracker.coctail.service.FriendlistService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Data
public class FriendlistServiceImpl implements FriendlistService {

    private final UserDao userDao;
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
    public List<FriendUser> friendList(String ownerEmail) {
        long ownerId = friendlistDao.getOwnerId(ownerEmail);
        log.info("Looking for friends for {}", ownerId);
        return friendlistDao.friendList(ownerId);
    }

    @Override
    public Boolean addFriend(String ownerEmail, long friendId) {

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
            return Boolean.TRUE;
        } else if (check(ownerId, friendId, confirm)) {
            throw new UserAlreadyAwaitsYourResponseException();
        } else if (check(ownerId, friendId, waiting)) {
            throw new AwaitingConfirmationException();
        } else {
            throw new AlreadyFriendsException();
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean acceptFriendRequest(String ownerEmail, long friendId) {

        long confirm = status(Id.CONFIRM.getStatus());
        long friends = status(Id.FRIENDS.getStatus());
        long ownerId = friendlistDao.getOwnerId(ownerEmail);

        if (friendlistDao.findFriendlist(ownerId, friendId).isEmpty()
                || !check(ownerId, friendId, confirm)) {
            throw new FriendRequestNotFoundException();
        } else {
            friendlistDao.editFriendlist(ownerId, friendId, friends);
            friendlistDao.editFriendlist(friendId, ownerId, friends);
            return Boolean.TRUE;
        }
    }

    @Override
    public Boolean declineFriendRequest(String ownerEmail, long friendId) {

        long confirm = status(Id.CONFIRM.getStatus());
        long notFriends = status(Id.NONE.getStatus());
        long ownerId = friendlistDao.getOwnerId(ownerEmail);

        if (friendlistDao.findFriendlist(ownerId, friendId).isEmpty()
                || !check(ownerId, friendId, confirm)) {
            throw new FriendRequestNotFoundException();
        } else {
            friendlistDao.editFriendlist(ownerId, friendId, notFriends);
            friendlistDao.editFriendlist(friendId, ownerId, notFriends);
            return Boolean.TRUE;
        }
    }

    @Override
    public Boolean subscribeToFriend(String ownerEmail, long friendId) {

        long friends = status(Id.FRIENDS.getStatus());
        long subscribed = status(Id.SUBSCRIBED.getStatus());
        long ownerId = friendlistDao.getOwnerId(ownerEmail);

        if (friendlistDao.findFriendlist(ownerId, friendId).isEmpty()
                || !check(ownerId, friendId, friends)) {
            throw new NotInFriendlistException();
        } else {
            friendlistDao.editFriendlist(ownerId, friendId, subscribed);
            return Boolean.TRUE;
        }
    }

    @Override
    public Boolean unsubcribeFromFriend(String ownerEmail, long friendId) {

        long friends = status(Id.FRIENDS.getStatus());
        long subscribed = status(Id.SUBSCRIBED.getStatus());
        long ownerId = friendlistDao.getOwnerId(ownerEmail);

        if (friendlistDao.findFriendlist(ownerId, friendId).isEmpty()
                || !check(ownerId, friendId, subscribed)) {
            throw new NotInFriendlistException();
        } else {
            friendlistDao.editFriendlist(ownerId, friendId, friends);
            return Boolean.TRUE;
        }

    }

    @Override
    public Boolean removeFriend(String ownerEmail, long friendId) {

        long notFriends = status(Id.NONE.getStatus());
        long subscribed = status(Id.SUBSCRIBED.getStatus());
        long friends = status(Id.FRIENDS.getStatus());
        long ownerId = friendlistDao.getOwnerId(ownerEmail);
        log.info("trying to remove friendid " + friendId + "from ownerId " + ownerId);

        if (friendlistDao.findFriendlist(ownerId, friendId).isEmpty()
                || (!check(ownerId, friendId, friends) && !check(ownerId, friendId, subscribed))) {
            throw new NotInFriendlistException();
        } else {
            friendlistDao.editFriendlist(ownerId, friendId, notFriends);
            friendlistDao.editFriendlist(friendId, ownerId, notFriends);
            return Boolean.TRUE;
        }
    }

    @Override
    public List<FriendUser> getUserByNickname(String ownerEmail, String nickname) {
        nickname = nickname.replaceAll("[^A-Za-z0-9]", "");
        log.info("calling dao with input " + ownerEmail + " and " + nickname);
        List<User> users = friendlistDao.getOwnerByNickname(nickname);
        List<FriendUser> friends = new ArrayList();
        long ownerId = friendlistDao.getOwnerId(ownerEmail);
        List<User> noFriendlist = new ArrayList();
        for (User user : users) {
            long friendId = user.getId();
            if (ownerId != friendId && friendlistDao.findFriendlist(ownerId, friendId).isEmpty()) {
                log.info("users with ids " + friendId + " and " + ownerId + " are not friends");
                noFriendlist.add(user);
            }
        }
        friendlistDao.batchFriendlist(ownerId, noFriendlist);
        for (int i = 0; i < users.size(); i++) {
            long friendId = users.get(i).getId();
            if (ownerId != friendId) {
                friends.add(
                        new FriendUser(
                                users.get(i).getId(),
                                users.get(i).getNickname(),
                                users.get(i).getEmail(),
                                friendlistDao.findFriendlist(
                                        ownerId, friendId).get(0).getStatusid(),
                                users.get(i).getImage()));
                log.info("showing user with id " + friendId);
            }
        }
        return friends;
    }

    @Override
    public List<FriendUser> showAllUsers(String ownerEmail, String nickname) {
        nickname = nickname.replaceAll("[^A-Za-z0-9]", "");
        log.info("calling dao with input " + ownerEmail + " and " + nickname);
        List<User> users = friendlistDao.getOwnerByNickname(nickname);
        List<FriendUser> result = new ArrayList();
        long ownerId = friendlistDao.getOwnerId(ownerEmail);
        List<User> noFriendList = new ArrayList();
        List<Friendlist> inFriendList = friendlistDao.allFriends(ownerId);
        for (User user : users) {
            long friendId = user.getId();
            boolean isFriend = false;
            for (int i = 0; i < inFriendList.size(); i++) {
                if (friendId == inFriendList.get(i).getFriendid()) {
                    isFriend = true;
                }
            }
            if (ownerId != friendId && !isFriend) {
                log.info("users with ids " + ownerId + " and " + friendId + " are not friends");
                noFriendList.add(user);
            }
        }
        friendlistDao.batchFriendlist(ownerId, noFriendList);
        inFriendList = friendlistDao.allFriends(ownerId);
        for (int i = 0; i < users.size(); i++) {
            long friendId = users.get(i).getId();
            int index = 0;
            for (int j = 0; j < inFriendList.size(); j++) {
                if (friendId == inFriendList.get(j).getFriendid()) {
                    index = j;
                }
            }
            if (ownerId != friendId) {
                result.add(
                        new FriendUser(
                                users.get(i).getId(),
                                users.get(i).getNickname(),
                                users.get(i).getEmail(),
                                inFriendList.get(index).getStatusid(),
                                users.get(i).getImage()));
                log.info("showing user with id " + friendId);
            }
        }
        return result;
    }


}

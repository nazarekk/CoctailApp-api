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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
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
  public Boolean removeFriend(String ownerEmail, long friendId) {

    long notFriends = status(Id.NONE.getStatus());
    long friends = status(Id.FRIENDS.getStatus());
    long subscribed = status(Id.SUBSCRIBED.getStatus());
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
    log.info("calling dao with input " + ownerEmail + " and " + nickname);
    long notFriends = status(Id.NONE.getStatus());
    List<User> users = friendlistDao.getOwnerByNickname(nickname);
    List<FriendUser> friends = new ArrayList();
    long ownerId = friendlistDao.getOwnerId(ownerEmail);
    long friendId;
    for (int i = 0; i < users.size(); i++) {
      friendId = users.get(i).getId();
      if (ownerId != friendId) {
        if (friendlistDao.findFriendlist(ownerId, friendId).isEmpty()) {
          friendlistDao.createFriendlist(ownerId, friendId, notFriends);
          friendlistDao.createFriendlist(friendId, ownerId, notFriends);
        }
        friends.add(
            new FriendUser(
                users.get(i).getId(),
                users.get(i).getNickname(),
                users.get(i).getEmail(),
                friendlistDao.findFriendlist(
                    ownerId, friendId).get(0).getStatusid()));
      }
      log.info("added friend wth friendId " + friendId);
    }
    return friends;
  }
}

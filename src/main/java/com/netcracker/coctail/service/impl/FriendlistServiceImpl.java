package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.exceptions.InvalidEmailException;
import com.netcracker.coctail.model.Friendlist;
import com.netcracker.coctail.repository.FriendlistDao;
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

    public Friendlist getFriendlist(long ownerid, long friendid){
        List<Friendlist> result = friendlistDao.findFriendlist(ownerid, friendid);
        if (result.isEmpty()) {
            throw new InvalidEmailException();
        }
        log.info("IN getFriendlist - status: {} found by ownerid: {} and friendid: {}", result.get(0).getStatusid(), result.get(0).getOwnerid(), result.get(0).getFriendid());
        return result.get(0);
    }

}

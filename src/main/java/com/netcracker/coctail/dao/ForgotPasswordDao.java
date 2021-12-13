package com.netcracker.coctail.dao;

import com.netcracker.coctail.dto.AuthenticationRequestDto;
import com.netcracker.coctail.model.User;

public interface ForgotPasswordDao {
    String sendCode(User user);

    AuthenticationRequestDto findByActivationCode(String code);

    int changePassword(User user, String password);
}

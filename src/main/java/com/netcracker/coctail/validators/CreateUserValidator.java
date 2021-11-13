package com.netcracker.coctail.validators;


import com.netcracker.coctail.exceptions.DuplicateEmailException;
import com.netcracker.coctail.exceptions.InvalidEmailException;
import com.netcracker.coctail.exceptions.InvalidPasswordException;
import com.netcracker.coctail.model.CreateUser;
import com.netcracker.coctail.model.ReadUser;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CreateUserValidator implements Validator {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public boolean getByEmail(String email) {
        String SELECT_EMAIL = "SELECT userid, email FROM users WHERE email = '%s'";
        RowMapper<ReadUser> rowMapper = (rs, rownum) ->
                new ReadUser(
                        rs.getInt("userid"),
                        rs.getString("email")
                );
        List<ReadUser> query = jdbcTemplate.query(String.format(SELECT_EMAIL, email), rowMapper);
        if (query.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateUser.class.equals(clazz);
    }

    public static boolean emailCheck(String email) {
        Pattern checkE = Pattern.compile(
                "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z][a-z](?:[a-z]*[a-z])?$"
        );
        return (checkE.matcher(email).matches());
    }

    public static boolean passwordCheck(String password) {
        Pattern checkP = Pattern.compile(
                "^[a-zA-Z0-9_]{5,}$"
        );
        if (password.matches(".*\\d+.*") & (password.matches(".*[a-z]+.*")) & (password.matches(".*[A-Z]+.*"))) {
            return (checkP.matcher(password).matches());
        }
        return false;
    }

    public void validate(Object target, Errors errors) {
        CreateUser user = (CreateUser) target;
        if (!emailCheck(user.getEmail())) {
            throw new InvalidEmailException();
        }
        if (!passwordCheck(user.getPassword())) {
            throw new InvalidPasswordException();
        }
        if (!getByEmail(user.getEmail())) {
            throw new DuplicateEmailException();
        }
    }
}

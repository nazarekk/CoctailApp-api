package com.netcracker.coctail.validators;

import com.netcracker.coctail.exceptions.DuplicateEmailException;
import com.netcracker.coctail.exceptions.InvalidEmailException;
import com.netcracker.coctail.model.Moderator;
import com.netcracker.coctail.model.ReadUser;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
@Data
public class CreateModeratorValidator implements Validator {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    @Value("${emailSelect}")
    private String emailSelection;

    public boolean getByEmail(String email) {
        RowMapper<ReadUser> rowMapper = (rs, rownum) ->
                new ReadUser(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getLong("roleid")
                );
        List<ReadUser> query = jdbcTemplate.query(String.format(emailSelection, email), rowMapper);
        if (query.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Moderator.class.equals(clazz);
    }

    public static boolean emailCheck(String email) {
        Pattern checkE = Pattern.compile(
                "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z][a-z](?:[a-z]*[a-z])?$"
        );
        return (checkE.matcher(email).matches());
    }

    public void validate(Object target, Errors errors) {
        Moderator user = (Moderator) target;
        if (!emailCheck(user.getEmail())) {
            throw new InvalidEmailException();
        }
        if (!getByEmail(user.getEmail())) {
            throw new DuplicateEmailException();
        }
    }
}

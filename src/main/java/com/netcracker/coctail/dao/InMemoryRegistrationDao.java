package com.netcracker.coctail.dao;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.netcracker.coctail.model.Users;

@Deprecated
public class InMemoryRegistrationDao implements PostgresRegistrationDao {

    private Map<String, Users> users;

    public InMemoryRegistrationDao() {
        users = new LinkedHashMap<>();
        test(users);
    }

    private static boolean emailCheck(String email){

        return (checkE.matcher(email).matches())	;
    }

    private static boolean passwordCheck(String password){
        boolean ok = false;
        if(password.matches(".*\\d+.*")){
            if(password.matches(".*[a-z]+.*")){
                if(password.matches(".*[A-Z]+.*")){
                    ok = true;
                }
            }
        };
        return (checkP.matcher(password).matches()&&ok&&password.length()>5);
    }

    private static final Pattern checkE = Pattern.compile(
            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z][a-z](?:[a-z]*[a-z])?$"
    );

    private static final Pattern checkP = Pattern.compile(
            "^[a-zA-Z0-9_]{5,}$"
    );



    private static void test(Map<String, Users> users)	{
        users.put("test@gmail.com", new Users("test@gmail.com","pass123"));
        users.put("test@a.aa", new Users("test@a.aa","pass123"));
    }

    public void create(Users user) {
        if(emailCheck(user.getEmail())){
            if(passwordCheck(user.getPassword())){
                if(users.get(user.getEmail())==null){
                    users.put(user.getEmail(), user);
                }
            }
        }
    }

    @Override
    public Collection<Users> getAll() {
        return users.values();
    }

}
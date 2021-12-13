package com.netcracker.coctail.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * Spring Security wrapper for class {User}.
 */

public class JwtUser implements UserDetails {

    private final Long id;
    private final String nickname;
    private final String password;
    private final String email;
    private final Long roleid;
    private final boolean isactive;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUser(Long id, String nickname, String password, String email,
                   Long roleid, boolean isactive, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.roleid = roleid;
        this.isactive = isactive;
        this.authorities = authorities;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isIsactive() {
        return isactive;
    }
    public String getNickname() {
        return nickname;
    }

    public Long getRoleid() {
        return roleid;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    public Date getLastPasswordResetDate() {
        return getLastPasswordResetDate();
    }

}


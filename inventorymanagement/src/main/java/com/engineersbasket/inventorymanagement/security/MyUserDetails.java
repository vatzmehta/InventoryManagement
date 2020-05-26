package com.engineersbasket.inventorymanagement.security;

import com.engineersbasket.inventorymanagement.db.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

    private String userName;

    private char[] password;

    private boolean isEnabled;

    private List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
       StringBuilder stringBuilder = new StringBuilder();
       for(int i=0; i<password.length; i++){
           stringBuilder.append(password[i]);
       }
       return stringBuilder.toString();
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public MyUserDetails(User user){
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.isEnabled = user.isEnabled();
        this.authorities = Arrays.stream(user.getRole().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}

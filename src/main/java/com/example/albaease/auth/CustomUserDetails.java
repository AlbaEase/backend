package com.example.albaease.auth;

import com.example.albaease.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // roles 리스트에서 권한을 SimpleGrantedAuthority로 변환하여 반환
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())); // "ROLE_" 접두사를 붙여서 권한 생성


}
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLoginId();
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
        return true;
    }

    // 사용자 추가 정보들
    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName();
    }
}

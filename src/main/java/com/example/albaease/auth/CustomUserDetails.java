package com.example.albaease.auth;

import com.example.albaease.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Getter

public class CustomUserDetails implements UserDetails {
    private final User user;
    private Long userId;
    private final String loginId;
//    private final String password;
    private final String fullName;
    private final String role;
    private final String storeName;

    public CustomUserDetails(User user) {
        this.user = user;
        this.loginId = user.getEmail();
//        this.password = user.getPassword();
        this.fullName =   user.getLastName() + user.getFirstName();
        this.role = user.getRole().name();
        this.storeName = user.getStoreName(); // storeName 초기화
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

    public Long getUserId() { // userId 반환하는 메서드 추가
        return user.getUserId();
    }

    @Override
    public String getUsername() {
        return loginId;
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

    public User getUser() {
        return user;
    }
    // 사용자 추가 정보들
    public String getFullName() {

        return user.getLastName() + user.getFirstName();
    }

    // storeName 반환
    public String getStoreName() {
        return storeName;
    }
}

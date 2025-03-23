package org.jpf.model.security;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.jpf.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * UserDetails realization with {@link User} entity.
 * {@link #getAuthorities()} always ROLE_USER.
 * {@link #isAccountNonExpired()} always true.
 * {@link #isAccountNonLocked()} always true.
 * {@link #isCredentialsNonExpired()} always true.
 * {@link #isEnabled()} always true.
 */
@EqualsAndHashCode
@RequiredArgsConstructor
public class AppUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
                new SimpleGrantedAuthority("ROLE_USER")
        );
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return getEmail();
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
}

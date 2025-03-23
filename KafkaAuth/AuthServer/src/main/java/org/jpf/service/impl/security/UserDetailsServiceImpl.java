package org.jpf.service.impl.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpf.model.entity.User;
import org.jpf.model.security.AppUserDetails;
import org.jpf.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Service for working with {@link AppUserDetails}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    /**
     * Service for working with entity {@link User}.
     */
    private final UserService userService;

    /**
     * Load {@link User} by email from {@link UserService}.
     *
     * @param username email searched {@link User}.
     * @return {@link AppUserDetails} with th found user.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userService.findByEmail(username);
        log.info("load user with {} email.", user.getEmail());
        return new AppUserDetails(user);
    }
}

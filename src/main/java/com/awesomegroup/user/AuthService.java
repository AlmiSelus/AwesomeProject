package com.awesomegroup.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Michał on 2017-04-23.
 */
@Service
public class AuthService implements UserDetailsService {

    private final static Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("Searching user {}", s);
        Optional<User> user = userRepository.findUserByEmail(s);
        return user.map(this::mapUserToUserDetails).orElseThrow(() -> new UsernameNotFoundException("User with given email not found!"));
    }

    private UserDetails mapUserToUserDetails(User user) {
        List<GrantedAuthority> authorityList = Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnabled(),
                true, !user.isCredentialsExpired(), !user.isLocked(), authorityList);
    }

}

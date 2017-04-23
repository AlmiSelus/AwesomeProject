package com.awesomegroup.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Michał on 2017-04-23.
 */
@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(s);
        return user.map(this::mapUserToUserDetails).orElseThrow(() -> new UsernameNotFoundException("User with given email not found!"));
    }

    private UserDetails mapUserToUserDetails(User user) {
        List<GrantedAuthority> authorityList = user.getUserRoles().stream().map(role->new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnabled(),
                true, !user.isCredentialsExpired(), !user.isLocked(), authorityList);
    }

}

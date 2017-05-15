package com.awesomegroup.user;

import com.awesomegroup.mail.EmailHTMLSender;
import com.awesomegroup.mail.EmailStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.thymeleaf.context.Context;

import java.util.Optional;

/**
 * Created by Michał on 2017-04-23.
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final String SALT = "2hM$^%#$^64Jpx5*NG#^E6yaRXLq6PhgmC&Yx61rzKgCPJdpZWx(ipq%fk)&HFjz";

    private final UserRepository userRepository;
    private final EmailHTMLSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, EmailHTMLSender mailSender, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> register(User user) {

        Optional<User> userPersisted = Optional.empty();

        if(!userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            log.info("User's [{}] password = {}", user.getEmail(), user.getPassword());
            User registerUserData = User.create(user).enabled(false).locked(true).credentialsExpired(false)
                    .roles().password(passwordEncoder.encode(user.getPassword())).build();
            userRepository.save(registerUserData);
            userPersisted = Optional.ofNullable(registerUserData);
            sendConfirmationEmail(registerUserData);
        }

        return userPersisted;
    }

    public void confirm(String userHash) {
        String userDecodedData = new String(Base64Utils.decodeFromString(userHash)).replace(SALT, "");
        userRepository.findUserByEmail(userDecodedData).ifPresent(user->{
            user.setLocked(false);
            user.setEnabled(true);
            userRepository.save(user);
        });
    }

    private void sendConfirmationEmail(final User user) {
        Context context = new Context();
        context.setVariable("title", "Lorem Ipsum");
        context.setVariable("description", "Lorem Lorem Lorem");
        context.setVariable("link", getConfirmationURL(user));

        mailSender.send(user.getEmail(), "Confirm your account!", "mail/template_register", context);
    }

    private String getConfirmationURL(User user) {
        return "http://localhost:8080/#/confirm?uh=" + Base64Utils.encodeToString((user.getEmail() + SALT).getBytes());
    }

}

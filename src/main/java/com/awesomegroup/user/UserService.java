package com.awesomegroup.user;

import com.awesomegroup.mail.EmailHTMLSender;
import com.awesomegroup.recaptcha.GoogleReCaptcha;
import com.awesomegroup.recaptcha.ReCaptchaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.thymeleaf.context.Context;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Optional;

import static com.awesomegroup.recaptcha.GoogleReCaptcha.RECAPTCHA_SERVICE_URL;

/**
 * Created by Michał on 2017-04-23.
 */
@Service
public class UserService {

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
        GoogleReCaptcha recaptcha = new Retrofit.Builder()
                                        .baseUrl(RECAPTCHA_SERVICE_URL)
                                        .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                                        .build()
                                        .create(GoogleReCaptcha.class);

        recaptcha.checkIfHuman(ReCaptchaRequest.create().secret().remoteIP().build());

        if(!userRepository.findUserByEmail(user.getEmail()).isPresent()) {
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
            userRepository.save(User.create(user).locked(false).enabled(true).build());
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

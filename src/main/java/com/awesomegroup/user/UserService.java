package com.awesomegroup.user;

import com.awesomegroup.fridge.Fridge;
import com.awesomegroup.mail.EmailHTMLSender;
import com.awesomegroup.recaptcha.GoogleReCaptcha;
import com.awesomegroup.recaptcha.ReCaptchaRequest;
import com.awesomegroup.recaptcha.ReCaptchaResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.exceptions.Exceptions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.Context;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.function.Function;

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

    @Value("${google.recaptcha.sitekey}")
    private String siteKey;

    @Value("${google.recaptcha.url}")
    private String serviceUrl;

    @Autowired
    public UserService(UserRepository userRepository, EmailHTMLSender mailSender, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public Maybe<Boolean> checkUserEmail(String mail) {
        return !userRepository.findUserByEmail(mail).isPresent() ? Maybe.just(false) :
                                Maybe.error(new Exception("User with given e-mail already exists."));
    }

    public Maybe<User> register(RegisterJson registerData) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();
        String uri = request.getScheme() + "://" + request.getServerName() +
                (request.getServerPort() != 80 ? ":" + request.getServerPort() : StringUtils.EMPTY);
        ReCaptchaRequest reCaptchaRequest = ReCaptchaRequest.create()
                                                            .secret(siteKey)
                                                            .recaptchaResponse(registerData.getCaptchaResponse())
                                                            .remoteIP(ip)
                                                            .build();
        log.info("Checking captcha!");
        log.info("ReCaptchaRequest = {}", reCaptchaRequest.toString());
        GoogleReCaptcha reCaptcha = new Retrofit.Builder()
                .baseUrl(serviceUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                .build()
                .create(GoogleReCaptcha.class);

        Single<ReCaptchaResponse> observableReCaptcha = reCaptcha.checkIfHuman(reCaptchaRequest.getSecret(), reCaptchaRequest.getResponse());

        return observableReCaptcha.toMaybe()
            .map(reCaptchaResponse -> {
                if(userRepository.findUserByEmail(registerData.getEmail()).isPresent()) {
                    throw Exceptions.propagate(new Exception("User with given email already exists"));
                }
                return reCaptchaResponse;
            })
            .map(reCaptchaResponse -> {
                if(!reCaptchaResponse.isValid()) {
                    throw Exceptions.propagate(new Exception("Captcha is not valid"));
                }
                return reCaptchaResponse;
            })
            .map(reCaptchaResponse->{
                log.info("User {}", Optional.of(registerData).map(this::transformRegisterToUser).orElse(null));
                return User.create(Optional.of(registerData).map(this::transformRegisterToUser).orElse(null))
                        .enabled(false)
                        .locked(true)
                        .credentialsExpired(false)
                        .roles()
                        .password(passwordEncoder.encode(registerData.getPassword()))
                        .fridge(Fridge.create().build())
                        .build();
            })
            .doOnSuccess(user -> {
                userRepository.save(user);
                sendConfirmationEmail(user, uri);
            }).doOnError(Maybe::error);
    }

    public boolean confirm(String userHash) {
        String userDecodedData = new String(Base64Utils.decodeFromString(userHash)).replace(SALT, StringUtils.EMPTY);
        return userRepository.findUserByEmail(userDecodedData).map(user -> {
            userRepository.save(User.create(user).locked(false).enabled(true).build());
            return true;
        }).orElse(false);
    }

    private User transformRegisterToUser(RegisterJson registerJson) {
        return User.create()
                .roles(UserRole.ADMIN_ROLE)
                .email(registerJson.getEmail())
                .name(registerJson.getName())
                .surname(registerJson.getSurname())
                .build();
    }

    private void sendConfirmationEmail(final User user, String baseUrl) {
        Context context = new Context();
        context.setVariable("title", "Lorem Ipsum");
        context.setVariable("description", "Lorem Lorem Lorem");
        context.setVariable("link", getConfirmationURL(user, baseUrl));

        mailSender.send(user.getEmail(), "Confirm your account!", "mail/template_register", context);
    }

    private String getConfirmationURL(User user, String baseUrl) {
        return baseUrl + "/#/confirm?uh=" + Base64Utils.encodeToString((user.getEmail() + SALT).getBytes());
    }
}

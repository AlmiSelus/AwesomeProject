package com.awesomegroup.user;

import com.awesomegroup.mail.EmailHTMLSender;
import com.awesomegroup.recaptcha.GoogleReCaptcha;
import com.awesomegroup.recaptcha.ReCaptchaRequest;
import com.awesomegroup.recaptcha.ReCaptchaResponse;
import com.awesomegroup.recaptcha.ReCaptchaSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Maybe<Boolean> checkUserEmail(String mail) {
        return !userRepository.findUserByEmail(mail).isPresent() ? Maybe.just(false) :
                                Maybe.error(new Exception("User with given e-mail already exists."));
    }

    public Maybe<User> register(RegisterJson registerData) {

        if(userRepository.findUserByEmail(registerData.getEmail()).isPresent()) {
            return Maybe.error(new Exception("User with given email already exists."));
        }

        GoogleReCaptcha recaptcha = new Retrofit.Builder()
                                        .baseUrl(ReCaptchaSettings.RECAPTCHA_SERVICE_URL.toString())
                                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                        .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                                        .build()
                                        .create(GoogleReCaptcha.class);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        String ip = request.getRemoteAddr();

        ReCaptchaRequest reCaptchaRequest = ReCaptchaRequest.create()
                                                            .secret(ReCaptchaSettings.RECAPTCHA_SECRET_KEY.toString())
                                                            .recaptchaResponse(registerData.getCaptchaResponse())
                                                            .remoteIP(ip)
                                                            .build();

        log.info("Checking captcha!");
        log.info("ReCaptchaRequest = {}", reCaptchaRequest.toString());

        Single<ReCaptchaResponse> observableReCaptcha = recaptcha.checkIfHuman(reCaptchaRequest.getSecret(), reCaptchaRequest.getResponse());

        return observableReCaptcha  .filter(reCaptchaResponse -> {
            log.info("Is Valid = {}", reCaptchaResponse.isValid());
            return reCaptchaResponse.isValid();
        })
                                    .filter(r -> !userRepository.findUserByEmail(registerData.getEmail()).isPresent())
                                    .map(reCaptchaResponse -> {
                                        log.info("User {}", Optional.of(registerData).map(this::transformRegisterToUser).orElse(null));
                                        return User.create(Optional.of(registerData).map(this::transformRegisterToUser).orElse(null))
                                                .enabled(false)
                                                .locked(true)
                                                .credentialsExpired(false)
                                                .roles()
                                                .password(passwordEncoder.encode(registerData.getPassword()))
                                                .build();
                                    })
                                    .doOnSuccess(user -> {
                                        userRepository.save(user);
                                        sendConfirmationEmail(user);})
                                    .doOnError(throwable -> log.error(ExceptionUtils.getStackTrace(throwable)));
    }

    public void confirm(String userHash) {
        String userDecodedData = new String(Base64Utils.decodeFromString(userHash)).replace(SALT, "");
        userRepository.findUserByEmail(userDecodedData).ifPresent(user->{
            userRepository.save(User.create(user).locked(false).enabled(true).build());
        });
    }

    private User transformRegisterToUser(RegisterJson registerJson) {
        return User.create()
                .roles(UserRole.ADMIN_ROLE)
                .email(registerJson.getEmail())
                .name(registerJson.getName())
                .surname(registerJson.getSurname())
                .build();
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

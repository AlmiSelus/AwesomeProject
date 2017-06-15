package com.awesomegroup.recaptcha;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Michał on 2017-06-05.
 */
public class ReCaptchaResponseBuilderTest {

    @Test
    public void callCreate_shouldCreateReCaptchaResponse() {
        ReCaptchaResponse reCaptchaResponse = ReCaptchaResponse.create()
                .errorCodes("error1", "error2")
                .hostname("hostname")
                .isValid(true)
                .timestamp(LocalDateTime.of(2017, 6, 16, 1, 50).toString())
                .build();
        assertThat(reCaptchaResponse.getErrorCodes().size(), is(2));
        assertThat(reCaptchaResponse.getErrorCodes().get(0), is("error1"));
        assertThat(reCaptchaResponse.getErrorCodes().get(1), is("error2"));
        assertThat(reCaptchaResponse.getHostname(), is("hostname"));
        assertThat(reCaptchaResponse.isValid(), is(true));
        assertThat(reCaptchaResponse.getTimestamp(), is("2017-06-16T01:50"));

    }
}

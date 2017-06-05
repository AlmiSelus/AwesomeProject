package com.awesomegroup.recaptcha;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.assertThat;

/**
 * Created by Michał on 2017-06-05.
 */
public class ReCaptchaRequestBuilderTests {

    @Test
    public void callCreate_shouldBuildDefaultObject() {
        ReCaptchaRequest request = ReCaptchaRequest.create().build();
        assertThat(request.getSecret(), isEmptyString());
        assertThat(request.getResponse(), isEmptyString());
        assertThat(request.getRemoteip(), isEmptyString());
    }

    @Test
    public void callCreate_withCorrectValues_shouldBuildCorrectRequest() {
        ReCaptchaRequest request = ReCaptchaRequest.create()
                                                    .secret("secret")
                                                    .remoteIP("remote")
                                                    .recaptchaResponse("recaptchaResponse")
                                                    .build();
        assertThat(request.getSecret(), is("secret"));
        assertThat(request.getResponse(), is("recaptchaResponse"));
        assertThat(request.getRemoteip(), is("remote"));
    }

}

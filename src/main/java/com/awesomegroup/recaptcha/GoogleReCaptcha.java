package com.awesomegroup.recaptcha;

import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Michał on 2017-05-25.
 */
public interface GoogleReCaptcha {
    String RECAPTCHA_SERVICE_URL = "https://www.google.com/recaptcha";

    @POST("/api/siteverify")
    String checkIfHuman(@Body ReCaptchaRequest request);
}

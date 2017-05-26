package com.awesomegroup.recaptcha;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Michał on 2017-05-25.
 */
public interface GoogleReCaptcha {
    String RECAPTCHA_SECRET_KEY = "6LcbwCIUAAAAAEBAx0hVwWyXpSkUq51_kmq32pXT";
    String RECAPTCHA_SERVICE_URL = "https://www.google.com/recaptcha/";

    @POST("api/siteverify")
    Observable<ReCaptchaResponse> checkIfHuman(@Body ReCaptchaRequest request);
}

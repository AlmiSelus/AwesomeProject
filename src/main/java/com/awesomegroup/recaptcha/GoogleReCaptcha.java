package com.awesomegroup.recaptcha;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Michał on 2017-05-25.
 */
public interface GoogleReCaptcha {

    @POST("api/siteverify")
    Single<ReCaptchaResponse> checkIfHuman(@Query("secret") String secret, @Query("response") String response);
}

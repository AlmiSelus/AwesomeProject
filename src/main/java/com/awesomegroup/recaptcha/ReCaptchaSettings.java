package com.awesomegroup.recaptcha;

/**
 * Created by Michał on 2017-05-27.
 */
public enum ReCaptchaSettings {
    RECAPTCHA_SECRET_KEY("6LcbwCIUAAAAAEBAx0hVwWyXpSkUq51_kmq32pXT"),
    RECAPTCHA_SERVICE_URL("https://www.google.com/recaptcha/");

    private String setting;

    ReCaptchaSettings(String setting) {
        this.setting = setting;
    }

    @Override
    public String toString() {
        return setting;
    }
}

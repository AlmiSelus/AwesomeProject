package com.awesomegroup.recaptcha;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

/**
 * Created by Michał on 2017-06-05.
 */
public class ReCaptchaResponseBuilderTest {

    @Mock
    private ReCaptchaResponse response;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        response = new ReCaptchaResponse();

        when(response.isValid()).thenReturn(true);
    }

    public void callIsValid_shouldReturnTrue() {

    }
}

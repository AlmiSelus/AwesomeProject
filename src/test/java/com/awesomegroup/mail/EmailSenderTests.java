package com.awesomegroup.mail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Created by c309044 on 2017-05-22.
 */
public class EmailSenderTests {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailSender emailSender;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void callSendMessage_shouldReturnSucessStatus() {
        MimeMessage message = new MimeMessage(Session.getInstance(new Properties()));
        when(javaMailSender.createMimeMessage()).thenReturn(message);

        EmailStatus emailStatus = EmailStatus.create()
                                            .to("test@mail.com")
                                            .subject("Subject")
                                            .body("<h1>body</h1>").build();
        emailStatus = emailSender.sendHtml(emailStatus);
        assertThat(emailStatus.isSuccess(), is(true));
    }

    @Test
    public void callSendMessage_shouldFailWithMessage() {
        EmailStatus status = EmailStatus.create().to("test@test.com").subject("subject").body("").build();
        status = emailSender.sendHtml(status);
        assertThat(status.isSuccess(), is(false));
        assertThat(status.isError(), is(true));
        assertThat(status.getErrorMessage(), containsString("Problem with sending email to: test@test.com"));
    }

}

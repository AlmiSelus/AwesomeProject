package com.awesomegroup.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 * Created by Michał on 2017-04-23.
 */
@Component
public class EmailSender {

    private static final Logger log = LoggerFactory.getLogger(EmailSender.class);

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public EmailStatus sendHtml(EmailStatus mailMessageData) {
        return send(mailMessageData);
    }

    private EmailStatus send(EmailStatus messageData) {
        try {
            MimeMessage mail = getMimeMessage(javaMailSender.createMimeMessage(), messageData);
            javaMailSender.send(mail);
            log.info("Send email '{}' to: {}", messageData.getSubject(), messageData.getTo());
            return new EmailStatus(messageData.getTo(), messageData.getSubject(), messageData.getBody()).success();
        } catch (Exception e) {
            log.error("Problem with sending email to: {}, error message: {}", messageData.getTo(), e.getMessage());
            return new EmailStatus(messageData.getTo(), messageData.getSubject(), messageData.getBody()).error(e.getMessage());
        }
    }

    private MimeMessage getMimeMessage(MimeMessage mail, EmailStatus messageData) throws Exception {
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(messageData.getTo());
        helper.setSubject(messageData.getSubject());
        helper.setText(messageData.getBody(), true);
        return helper.getMimeMessage();
    }
}
package com.awesomegroup.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Created by Michał on 2017-04-23.
 */
@Component
public class EmailHTMLSender {

    private final EmailSender emailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailHTMLSender(EmailSender emailSender, TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    public EmailStatus send(String email, String subject, String templateName, Context context) {
        return emailSender.sendHtml(getEmailMessageStatus(email, subject, templateEngine.process(templateName, context)));
    }

    private EmailStatus getEmailMessageStatus(String email, String subject, String body) {
        return EmailStatus.create().to(email).subject(subject).body(body).build();
    }
}
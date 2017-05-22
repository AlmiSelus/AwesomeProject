package com.awesomegroup.mail;

/**
 * Created by Michał on 2017-04-23.
 */
public class EmailStatus {

    public static final String SUCCESS_STATUS = "SUCCESS";
    public static final String ERROR_STATUS = "ERROR";

    private final String to;
    private final String subject;
    private final String body;

    private String status;
    private String errorMessage;

    public EmailStatus(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public boolean isSuccess() {
        return SUCCESS_STATUS.equals(this.status);
    }

    public boolean isError() {
        return ERROR_STATUS.equals(this.status);
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static EmailStatusBuilder create() {
        return new EmailStatusBuilder();
    }

    public static EmailStatusBuilder create(EmailStatus existingStatus) {
        return new EmailStatusBuilder(existingStatus);
    }

    public static class EmailStatusBuilder {
        private String to;
        private String subject;
        private String body;
        private String status;
        private String msg;

        public EmailStatusBuilder() {
        }

        public EmailStatusBuilder(EmailStatus emailStatus) {
            to = emailStatus.getTo();
            subject = emailStatus.getSubject();
            body = emailStatus.getBody();
        }

        public EmailStatusBuilder to(String to) {
            this.to = to;
            return this;
        }

        public EmailStatusBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public EmailStatusBuilder body(String body) {
            this.body = body;
            return this;
        }

        public EmailStatusBuilder success() {
            status = SUCCESS_STATUS;
            return this;
        }

        public EmailStatusBuilder error(String message) {
            status = ERROR_STATUS;
            this.msg = message;
            return this;
        }

        public EmailStatus build() {
            EmailStatus mailStatus = new EmailStatus(to, subject, body);
            mailStatus.status = status;
            mailStatus.errorMessage = msg;
            return mailStatus;
        }
    }
}

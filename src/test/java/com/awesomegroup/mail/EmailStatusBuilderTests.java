package com.awesomegroup.mail;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by c309044 on 2017-05-22.
 */
public class EmailStatusBuilderTests {

    @Test
    public void createEmailStatus_allRequiredValuesSet() {
        EmailStatus status = EmailStatus.create().to("email").subject("subject").body("body").build();
        assertThat(status.getTo(), is("email"));
        assertThat(status.getSubject(), is("subject"));
        assertThat(status.getBody(), is("body"));
    }

    @Test
    public void createEmailStatus_noEmailSet_emailShouldBeNull() {
        EmailStatus status = EmailStatus.create().subject("subject").body("body").build();
        assertThat(status.getTo(), is(nullValue()));
    }

    @Test
    public void createEmailStatus_noSubjectSet_subjectShouldBeNull() {
        EmailStatus status = EmailStatus.create().to("mail").body("body").build();
        assertThat(status.getSubject(), is(nullValue()));
    }

    @Test
    public void createEmailStatus_noBodySet_bodyShouldBeNull() {
        EmailStatus status = EmailStatus.create().to("mail").subject("subject").build();
        assertThat(status.getBody(), is(nullValue()));
    }

    @Test
    public void createEmailStatus_returnWithStatusSuccess() {
        EmailStatus status = EmailStatus.create().to("mail").subject("subject").body("body").success().build();
        assertThat(status.isSuccess(), is(true));
    }

    @Test
    public void createEmailStatus_returnWithStatusError() {
        EmailStatus status = EmailStatus.create().to("mail").subject("subject").body("body").error("Test msg").build();
        assertThat(status.isError(), is(true));
        assertThat(status.getErrorMessage(), is("Test msg"));
    }
}

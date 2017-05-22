package com.awesomegroup.mail;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by c309044 on 2017-05-22.
 */
public class EmailStatusBuilderTests {

    @Test
    public void createEmailStatus_allRequiredValuesSet() {
        EmailStatus status = EmailStatus.create().to("email").subject("subject").body("body").build();
        Assert.assertEquals("email", status.getTo());
        Assert.assertEquals("subject", status.getSubject());
        Assert.assertEquals("body", status.getBody());
    }

    @Test
    public void createEmailStatus_noEmailSet_emailShouldBeNull() {
        EmailStatus status = EmailStatus.create().subject("subject").body("body").build();
        Assert.assertNull(status.getTo());
    }

    @Test
    public void createEmailStatus_noSubjectSet_subjectShouldBeNull() {
        EmailStatus status = EmailStatus.create().to("mail").body("body").build();
        Assert.assertNull(status.getSubject());
    }

    @Test
    public void createEmailStatus_noBodySet_bodyShouldBeNull() {
        EmailStatus status = EmailStatus.create().to("mail").subject("subject").build();
        Assert.assertNull(status.getBody());
    }
}

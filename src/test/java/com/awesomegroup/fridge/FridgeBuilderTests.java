package com.awesomegroup.fridge;

import com.awesomegroup.user.User;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;


/**
 * Created by Adi on 26.05.2017.
 */
public class FridgeBuilderTests {

    @Test
    public void fridgeBuilder_createDefaultObject() {
        Assert.assertThat(Fridge.create().build(), is(notNullValue()));
    }

    @Test
    public void fridgeBuilder_createSpecificObject() {
        Fridge fridge = Fridge.create().id(4).user(User.create().email("m@m.c").build()).build();
        LoggerFactory.getLogger(getClass()).info(fridge.toString());
        Assert.assertThat(fridge, is(notNullValue()));
        Assert.assertThat(fridge.getFridgeId(), is(4L));
        Assert.assertThat(fridge.getFridgeUser().getEmail(), is("m@m.c"));
    }
}

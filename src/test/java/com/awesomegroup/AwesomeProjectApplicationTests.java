package com.awesomegroup;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.rule.OutputCapture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class AwesomeProjectApplicationTests {

	@Rule
	public OutputCapture output = new OutputCapture();

	@Test
	public void callRunApplication_defaultNoParams_shouldHaveStartedInfoString() {
		AwesomeProjectApplication.main(new String[]{});
		assertThat(output.toString(), containsString("Started AwesomeProjectApplication in"));
	}

}

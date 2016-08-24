package com.projectfive.test;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is an example of a TestNG test class that would contain the service
 * integration tests.
 *
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
@Test
public class ExampleTest {
    @Test
    public void test() throws Exception {
        assertThat("hello").hasSize(5);
    }
}

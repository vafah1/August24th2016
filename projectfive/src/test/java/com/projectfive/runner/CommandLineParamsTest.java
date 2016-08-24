package com.projectfive.runner;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
@Test(groups = "unit")
public class CommandLineParamsTest {

    private CommandLineParams params;

    @Test
    public void testGet() throws Exception {
        params = CommandLineParams.get();
        assertThat(params).isNotNull();
    }

    @Test(dependsOnMethods = "testGet")
    public void testGetEnvironment() throws Exception {
        assertThat(params.getEnvironment()).isEqualTo("test");
    }
}

package com.projectfive.config.options;

import com.beust.jcommander.ParameterException;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
@Test(groups = "unit")
public class KnownEnvironmentsTest {

    @Tested KnownEnvironments.EnvironmentValidator validator;

    final String name = "name";
    final String dev = "dev";
    final String test = "test";

    @Test
    public void testEnum() throws Exception {
        assertThat(KnownEnvironments.values())
                .hasSize(2)
                .contains(KnownEnvironments.DEV, KnownEnvironments.TEST);
        assertThat(KnownEnvironments.valueOf(dev.toUpperCase())).isEqualTo(KnownEnvironments.DEV);
    }

    @Test
    public void testNames() throws Exception {
        assertThat(KnownEnvironments.names())
                .hasSize(2)
                .contains(dev, test);
    }

    @Test
    public void testToString() throws Exception {
        assertThat(KnownEnvironments.TEST.toString()).isEqualTo(test);
    }

    @Test
    public void testValidateNull() throws Exception {
        validator.validate("", null);
    }

    @Test(
            expectedExceptions = ParameterException.class,
            expectedExceptionsMessageRegExp =
                    "Selected environment \\[test\\] is unknown\\. Please use one of \\[name\\]"
    )
    public void testValidateException(@Mocked KnownEnvironments environments) throws Exception {
        new Expectations() {{
            KnownEnvironments.names(); result = new ArrayList<String>(){{ add(name); }};
        }};
        validator.validate("", test);
    }

    @Test
    public void testValidate(@Mocked KnownEnvironments environments) throws Exception {
        new Expectations() {{
            KnownEnvironments.names(); result = new ArrayList<String>(){{ add(name); }};
        }};
        validator.validate("", name);
    }
}

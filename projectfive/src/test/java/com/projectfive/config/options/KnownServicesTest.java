package com.projectfive.config.options;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
@Test(groups = "unit")
public class KnownServicesTest {

    @Test
    public void testEnum() throws Exception {
        assertThat(KnownServices.values())
                .hasSize(2)
                .contains(KnownServices.USER, KnownServices.AUTH);
        assertThat(KnownServices.valueOf("USER")).isEqualTo(KnownServices.USER);
    }
}

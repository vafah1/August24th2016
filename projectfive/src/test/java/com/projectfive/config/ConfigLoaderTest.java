package com.projectfive.config;

import com.projectfive.runner.CommandLineParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import mockit.*;
import org.testng.annotations.Test;

import java.io.IOException;

import static mockit.Deencapsulation.invoke;
import static mockit.Deencapsulation.setField;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
@Test(groups = "unit")
public class ConfigLoaderTest {

    @Tested ConfigLoader tested;
    @Injectable ServiceConfig config;
    @Mocked Config typeConfig;
    @Mocked CommandLineParams params;
    @Mocked ConfigRenderOptions options;
    @Mocked ObjectMapper objectMapper;
    @Mocked ConfigFactory factory;

    final String env = "env";
    final String json = "json";

    @Test
    public void testGetConfigNotCached() throws Exception {
        new Expectations(tested) {{
            setField(ConfigLoader.class, "serviceConfig", null);
            invoke(tested, "buildCache"); result = config;
        }};
        assertThat(ConfigLoader.getConfig()).isEqualTo(config);
    }

    @Test(dependsOnMethods = "testGetConfigNotCached")
    public void testGetConfigCached() throws Exception {
        new Expectations(tested) {{
            setField(ConfigLoader.class, "serviceConfig", config);
        }};
        assertThat(ConfigLoader.getConfig()).isEqualTo(config);
        new Verifications() {{
            invoke(tested, "buildCache"); times = 0;
        }};
    }

    @Test(
            expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "Unable to deserialize configuration file due to error: .*"
    )
    public void testBuildCacheException() throws Exception {
        new Expectations() {{
            CommandLineParams.get().getEnvironment(); result = env;
            typeConfig.getConfig(env); result = typeConfig;
            typeConfig.root().render(options); result = json;
            objectMapper.readValue(json, ServiceConfig.class); result = new IOException("");
        }};
        invoke(ConfigLoader.class, "buildCache");
    }

    @Test
    public void testBuildCache() throws Exception {
        new Expectations() {{
            CommandLineParams.get().getEnvironment(); result = env;
            typeConfig.getConfig(env); result = typeConfig;
            typeConfig.root().render(options); result = json;
            objectMapper.readValue(json, ServiceConfig.class); result = config;
        }};
        final ServiceConfig result = invoke(ConfigLoader.class, "buildCache");
        assertThat(result).isEqualTo(config);
    }
}

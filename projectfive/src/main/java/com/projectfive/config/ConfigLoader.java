package com.projectfive.config;

import com.projectfive.runner.CommandLineParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * This class handles loading and accessing the TypeSafe Config data stored in a .conf file
 *
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
public class ConfigLoader {
    private static final String CONFIG_FILE_NAME = "ServiceConfig.conf";

    // Cached config
    private static ServiceConfig serviceConfig;

    /**
     * Single point of access to the {@link ServiceConfig}. Will build the cache if it's empty.
     *
     * @return cached {@link ServiceConfig}
     */
    @NotNull
    public static ServiceConfig getConfig() {
        if (serviceConfig == null) {
            serviceConfig = buildCache();
        }
        return serviceConfig;
    }

    /**
     * Handles deserializing the TypeSafe Config object into a Java object.
     *
     * @return the {@link ServiceConfig} with all config data populated
     * @throws RuntimeException if the {@link ServiceConfig} object does not match the fields within the
     * resource {@link ConfigLoader#CONFIG_FILE_NAME}
     */
    private static ServiceConfig buildCache() throws RuntimeException {
        // Load the ServiceConfig.conf
        final Config testConfig = ConfigFactory.load(CONFIG_FILE_NAME);

        // Grab the sub config matching the supplied environment
        final Config envConfig = testConfig.getConfig(CommandLineParams.get().getEnvironment());

        // Convert the envConfig into formatted json for deserialization
        final String formattedJson = envConfig.root().render(ConfigRenderOptions.concise());

        // Deserialize the json into a ServiceConfig object
        try {
            return new ObjectMapper().readValue(formattedJson, ServiceConfig.class);
        } catch (IOException ex) {
            throw new RuntimeException(
                    "Unable to deserialize configuration file due to error: " + ex.getMessage(), ex);
        }
    }
}

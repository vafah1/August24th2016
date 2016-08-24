package com.projectfive.config.options;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

/**
 * The environments which can or will be tested. Any environment placed within this enum must have a corresponding
 * configuration block defined in the src/main/resources/{config file name}.conf file.
 *
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
public enum KnownEnvironments {
    DEV ("dev"),
    TEST ("test");

    private final String name;

    KnownEnvironments(@NotNull final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Converts the KnownEnvironments enum into a list of strings
     *
     * @return A list of strings which represent the KnownEnvironments
     */
    @NotNull
    public static List<String> names() {
        return asList(values())
                .stream()
                .map(KnownEnvironments::toString)
                .collect(toList());
    }

    /**
     * This class acts as the validator for the environment parameter.
     * @see {@link com.projectfive.runner.CommandLineParams#environment CommandLineParams.environment}
     */
    public static class EnvironmentValidator implements IParameterValidator {

        /**
         * Verifies the environment parameter provided is a known environment
         *
         * @param name {@inheritDoc}
         * @param value {@inheritDoc}
         * @throws ParameterException {@inheritDoc}
         */
        @Override
        public void validate(String name, @Nullable final String value) throws ParameterException {
            if (value == null) return; // Use the default environment
            final List<String> environments = names();
            if (!environments.contains(value.toLowerCase())) {
                throw new ParameterException(
                        "Selected environment [" + value + "] is unknown. Please use one of " + environments);
            }
        }
    }
}

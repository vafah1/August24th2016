package com.projectfive.runner;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.projectfive.config.options.KnownEnvironments;
import com.projectfive.config.options.KnownEnvironments.EnvironmentValidator;
import org.jetbrains.annotations.NotNull;

import static com.projectfive.config.options.KnownEnvironments.TEST;

/**
 * This class defines the parameters which are able to be passed to the test suite. If the parameter is not
 * required it must contain a default value.
 *
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
@Parameters(separators = "=")
public class CommandLineParams {

    /**
     * Singleton instance of the CommandLineParams. This object can be updated using the
     * {@link com.beust.jcommander.JCommander} constructor.
     *
     * @see {@link TestRunner#main(String[])}
     */
    private static final CommandLineParams params = new CommandLineParams();

    /**
     * The method used to access the singleton instance of the {@link CommandLineParams}
     *
     * @return singleton instance of {@link CommandLineParams}
     */
    @NotNull
    public static CommandLineParams get() {
        return params;
    }

    /**
     * Hide the singletons constructor
     */
    private CommandLineParams() { /* Singleton class */ }

    /**
     * The environment to run the automation against. The default value is 'test' and the validation
     * of the selected option exists in {@link KnownEnvironments}
     */
    @Parameter(
            names = "-env",
            description = "Selects the environment to run against",
            validateWith = EnvironmentValidator.class
    )
    private String environment = TEST.toString();

    @NotNull
    public String getEnvironment() {
        return environment;
    }
}

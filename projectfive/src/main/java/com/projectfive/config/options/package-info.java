/**
 * The options package will contain known "good" parameters that can be used within the applications operation.
 *
 * <p>Example:
 *
 * <p>The {@link com.projectfive.config.options.KnownEnvironments} is an enum containing the environments which are valid to
 * execute the test suite against. It also contains the
 * {@link com.projectfive.config.options.KnownEnvironments.EnvironmentValidator} which is used to validate the environment
 * string passed as a command line parameter.
 *
 * <p>The {@link com.projectfive.config.options.KnownServices} is another enum containing the services which can be tested.
 * This is used as key within the {@link com.projectfive.config.ServiceConfig#services ServiceConfig.services} to
 * allow a user to quickly grab the config object for a particular service.
 *
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
package com.projectfive.config.options;
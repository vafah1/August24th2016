/**
 * The providers package will hold the classes containing the {@link org.testng.annotations.DataProvider}
 * implementation which will be used by the test classes. In most instances these classes will make use of the
 * {@link com.projectfive.config.ConfigLoader#getConfig() ConfigLoader.getConfig()} to generate the data which will be
 * passed into the tests.
 *
 * <p>Example:
 *
 * <p>The following UserProvider.class contains a {@link org.testng.annotations.DataProvider} to supply
 * valid User objects to the test class. The User objects are built with data stored in the config and accessed through
 * {@link com.projectfive.config.ConfigLoader#getConfig() ConfigLoader.getConfig()}.
 *
 * <pre>{@code
 *  public class UserProvider {
 *
 *      {@literal @DataProvider(name = "valid user")}
 *      public static Iterator<Object[]> getValidData() {
 *          final List<String> userIds = getConfig().getValidUserIds();
 *          return userIds.stream()
 *              .map(s -> new Object[]{ new User(s) })
 *              .collect(toList())
 *              .iterator();
 *      }
 *
 *  }
 * }</pre>
 *
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
package com.projectfive.providers;
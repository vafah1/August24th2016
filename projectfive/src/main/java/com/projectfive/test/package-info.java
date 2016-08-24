/**
 * The test directory will contain all the TestNG service tests that will be included and executed from the final test
 * Jar.
 *
 * <b>All test classes MUST end with
 * {@link com.projectfive.runner.TestRunner#TEST_CLASS_ENDING TestRunner.TEST_CLASS_ENDING}</b>
 *
 * <p>Example:
 *
 * <pre>{@code
 *  {@literal @}Test(groups = "service")
 *  public class UserTest {
 *      final UserClient client = UserClient.get();
 *
 *      {@literal @}Test(dataProviderClass = UserProvider.class, dataProvider = "valid user")
 *      public void testPutUser(final User user) {
 *          final String lastName = "Graham";
 *
 *          // Update the users name
 *          user.setLastName(lastName);
 *
 *          // Use the HTTP PUT method to update the users last name
 *          final User updatedUser = client.putUser(user);
 *
 *          // Verify the service updated the user and returned the new last name
 *          assertThat(updatedUser.getLastName()).isEqualTo(lastName);
 *      }
 *  }
 * }</pre>
 *
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
package com.projectfive.test;
/**
 * This package should contain all Singleton HTTP clients and all domain
 * objects that will passed to or returned from the client.
 *
 * <p>Example:
 *
 * <p>We want to test a user REST service with the following endpoints:
 * <ul>
 *     <li>GET  api-url/{version}/user/{userId}</li>
 *     <li>POST api-url/{version}/user</li>
 *     <li>PUT  api-url/{version}/user/{userId}</li>
 * </ul>
 *
 * A user client class would look something like:
 *
 * <pre>{@code
 *      public class UserClient {
 *          // Cache the UserClient so all test classes can access it through UserClient.get()
 *          private static UserClient cache;
 *
 *          // Store the HTTP client to make requests
 *          private final RestClient client;
 *
 *          // UserClient access
 *          public static UserClient get() {
 *              if (cache == null) {
 *                  final Service userService = getConfig().getServices().get(USER);
 *                  // Create a new UserClient handing it a RestClient with the host/version already set
 *                  cache = new UserClient(new RestClient(userService.getHost()).path(userService.getVersion()));
 *              }
 *              return cache;
 *          }
 *
 *          // Takes the RestClient adding the common /user path for each endpoint to build from
 *          private UserClient(@NotNull final RestClient client) {
 *              this.client = client.path("user");
 *          }
 *
 *          public User getUser(@NotNull final String userId) {
 *              // GET host/{version}/user/{userId}
 *          }
 *
 *          public User postUser(@NotNull final User user) {
 *              // POST host/{version}/user
 *          }
 *
 *          public User putUser(@NotNull final User user) {
 *              // PUT host/{version}/user/{userId}
 *          }
 *      }
 * }</pre>
 *
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
package com.projectfive.client;
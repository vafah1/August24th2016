package com.projectfive.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.UriBuilder.fromUri;

/**
 * An immutable, fluent REST client wrapping a {@link Client}
 *
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
public class RestClient {

    private final Map<String, String> headerParams;
    private final List<Cookie> cookies;
    private final UriBuilder uri;
    private final Client client;
    private final MediaType[] request;
    private final MediaType[] accept;

    /**
     * The default entry point into a {@link RestClient}. Using this constructor will generate a new {@link Client}
     * to handle all HTTP requests.
     *
     * @param baseUri the base URL to build the REST request off
     */
    public RestClient(String baseUri) {
        this(baseUri, ClientBuilder.newClient());
    }

    /**
     * This entry point allows the user to configure and provide their own implementation of the {@link Client}.
     *
     * @param baseUri the base URL to build the REST request off
     * @param client the base client to execute the HTTP requests
     */
    public RestClient(String baseUri, Client client) {
        this(fromUri(baseUri), client, new HashMap<>(), new ArrayList<>(), new MediaType[0], new MediaType[0]);
    }

    /**
     * This constructor is used internally to build up the REST request
     *
     * @param uri built using {@link RestClient}
     * @param client to be used during HTTP request execution
     * @param headerParams to be added to the header of the HTTP request
     * @param cookies to be added to the header of the HTTP request
     * @param request to specify the current request type
     * @param accept to specify the expected response type
     */
    private RestClient(UriBuilder uri, Client client, Map<String, String> headerParams,
                       List<Cookie> cookies, MediaType[] request, MediaType[] accept) {
        this.uri = uri;
        this.client = client;
        this.headerParams = headerParams;
        this.cookies = cookies;
        this.request = request;
        this.accept = accept;
    }

    /**
     * Appends the path params to the url.
     *
     * <p>Example: The following would result in "http://www.test.com/one/two/three"
     *
     * <pre>{@code
     *  new RestClient("http://www.test.com/")
     *      .path("one", "two", "three");
     * }</pre>
     *
     * @param path the path[s] to add to the baseUri
     * @return this with an added path
     */
    public RestClient path(String... path) {
        final UriBuilder builder = fromUri(uri.toTemplate());
        asList(path).forEach(builder::path);
        return new RestClient(builder, client, headerParams, cookies, request, accept);
    }

    /**
     * Appends query params to the end of the url.
     *
     * <p>Example: The following would result in "http://www.test.com/one?key1=value1&key2=value2"
     *
     * <pre>{@code
     *  new RestClient("http://www.test.com/")
     *      .path("one")
     *      .query("key1", "value1")
     *      .query("key2", "value2");
     * }</pre>
     *
     * @param key the key of the query
     * @param value the value of the query
     * @return this with an added query
     */
    public RestClient query(String key, String value) {
        final UriBuilder builder = fromUri(uri.toTemplate()).queryParam(key, value);
        return new RestClient(builder, client, headerParams, cookies, request, accept);
    }

    /**
     * Adds header key, value pairs to the request.
     *
     * @param name the name of the header param
     * @param value the value of the header
     * @return this with an added header
     */
    public RestClient header(final String name, final String value) {
        final Map<String, String> newHeaders = new HashMap<String, String>() {{
            putAll(headerParams);
            put(name, value);
        }};
        return new RestClient(uri, client, newHeaders, cookies, request, accept);
    }

    /**
     * Adds a generic cookie header param to the request.
     *
     * @param name the key of the cookie
     * @param value the value of the cookie
     * @return this with a new cookie
     */
    public RestClient cookie(String name, String value) {
        return cookie(new Cookie(name, value));
    }

    /**
     * Allows the user to provide a custom cookie to the request.
     *
     * @param cookie the cookie to add
     * @return this with a new cookie
     */
    public RestClient cookie(final Cookie cookie) {
        final List<Cookie> newCookies = new ArrayList<Cookie>() {{
            addAll(cookies);
            add(cookie);
        }};
        return new RestClient(uri, client, headerParams, newCookies, request, accept);
    }

    /**
     * Allows the user to provide the request type.
     *
     * @param request the type of data to be passed in the request
     * @return this with an added request type
     *
     * @see MediaType#APPLICATION_JSON_TYPE
     * @see MediaType#APPLICATION_XML_TYPE
     * @see MediaType#TEXT_HTML_TYPE
     * @see MediaType#TEXT_PLAIN_TYPE
     */
    public RestClient request(final MediaType... request) {
        return new RestClient(uri, client, headerParams, cookies, request, accept);
    }

    /**
     * Allows the user to provide the accept type of the response.
     *
     * @param accept the type of data to be accepted in the response
     * @return this with an added accept type
     *
     * @see MediaType#APPLICATION_JSON_TYPE
     * @see MediaType#APPLICATION_XML_TYPE
     * @see MediaType#TEXT_HTML_TYPE
     * @see MediaType#TEXT_PLAIN_TYPE
     */
    public RestClient accept(final MediaType... accept) {
        return new RestClient(uri, client, headerParams, cookies, request, accept);
    }

    /**
     * Execute a HTTP GET request against the aggregated endpoint
     *
     * @return the response from the GET request
     */
    public Response get() {
        final WebTarget target = client.target(uri);
        return getBuilder(target).get();
    }

    /**
     * Execute a HTTP GET request against the aggregated endpoint and deserialize the response into
     * the provided type.
     *
     * @param responseType the class type to deserialize into
     * @param <T> the type which will eventually be returned
     * @return the final object with the HTTP response body loaded into it
     */
    public <T> T get(Class<T> responseType) {
        return get().readEntity(responseType);
    }

    /**
     * Execute a HTTP PUT request against the aggregated endpoint
     *
     * @param entity the body of the PUT request
     * @return the response from the PUT request
     *
     * @see Entity#json(Object)
     * @see Entity#form(Form)
     * @see Entity#html(Object)
     * @see Entity#text(Object)
     */
    public Response put(Entity<?> entity) {
        final WebTarget target = client.target(uri);
        return getBuilder(target).put(entity);
    }

    /**
     * Execute a HTTP PUT request against the aggregated endpoint and deserialize the response into
     * the provided type.
     *
     * @param entity the body of the PUT request
     * @param responseType the class type to deserialize into
     * @param <T> the type which will eventually be returned
     * @return the final object with the HTTP response body loaded into it
     *
     * @see Entity#json(Object)
     * @see Entity#form(Form)
     * @see Entity#html(Object)
     * @see Entity#text(Object)
     */
    public <T> T put(Entity<?> entity, Class<T> responseType) {
        return put(entity).readEntity(responseType);
    }

    /**
     * Execute a HTTP POST request against the aggregated endpoint
     *
     * @param entity the body of the POST request
     * @return the response from the POST request
     *
     * @see Entity#json(Object)
     * @see Entity#form(Form)
     * @see Entity#html(Object)
     * @see Entity#text(Object)
     */
    public Response post(Entity<?> entity) {
        final WebTarget target = client.target(uri);
        return getBuilder(target).post(entity);
    }

    /**
     * Execute a HTTP POST request against the aggregated endpoint and deserialize the response into
     * the provided type.
     *
     * @param entity the body of the POST request
     * @param responseType the class type to deserialize into
     * @param <T> the type which will eventually be returned
     * @return the final object with the HTTP response body loaded into it
     *
     * @see Entity#json(Object)
     * @see Entity#form(Form)
     * @see Entity#html(Object)
     * @see Entity#text(Object)
     */
    public <T> T post(Entity<?> entity, Class<T> responseType) {
        return post(entity).readEntity(responseType);
    }

    /**
     * Execute a HTTP DELETE request against the aggregated endpoint
     *
     * @return the response from the DELETE request
     */
    public Response delete() {
        final WebTarget target = client.target(uri);
        return getBuilder(target).delete();
    }

    /**
     * Execute a HTTP DELETE request against the aggregated endpoint and deserialize the response into
     * the provided type.
     *
     * @param responseType the class type to deserialize into
     * @param <T> the type which will eventually be returned
     * @return the final object with the HTTP response body loaded into it
     */
    public <T> T delete(Class<T> responseType) {
        return delete().readEntity(responseType);
    }

    /**
     * Provides the current url in the build process
     *
     * @return the current url
     */
    public String currentPath() {
        return uri.toTemplate();
    }

    /**
     * Adds the request type, accept type, headers, and cookies to the target request
     *
     * @param target the target to build up
     * @return the builder to execute an HTTP request off
     */
    private Invocation.Builder getBuilder(WebTarget target) {
        final Invocation.Builder builder = target.request(request).accept(accept);
        headerParams.forEach(builder::header);
        cookies.forEach(builder::cookie);
        return builder;
    }
}
package com.projectfive.client;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import org.testng.annotations.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static mockit.Deencapsulation.getField;
import static mockit.Deencapsulation.invoke;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
@Test(groups = "unit")
public class RestClientTest {

    @Mocked Entity entity;
    @Mocked WebTarget target;
    @Mocked Response response;
    @Mocked Invocation.Builder builder;
    @Mocked Cookie cookie;
    @Mocked UriBuilder uriBuilder;
    @Mocked Client client;
    @Injectable String baseUri = "baseUri";
    @Tested RestClient tested;

    // Parameters used to test
    private final String path = "path";
    private final String key = "key";
    private final String value = "value";

    // Field/Method names used for verification
    private final String headerField = "headerParams";
    private final String cookiesField = "cookies";
    private final String requestField = "request";
    private final String acceptField = "accept";
    private final String getBuilderMethod = "getBuilder";

    @Test
    public void testPathSingle() throws Exception {
        final RestClient result = tested.path(path);
        assertThat(result).isNotSameAs(tested);
        new Verifications() {{
            uriBuilder.path(path); times = 1;
        }};
    }

    @Test
    public void testPathMultiple() throws Exception {
        final RestClient result = tested.path(path, path, path);
        assertThat(result).isNotSameAs(tested);
        new Verifications() {{
            uriBuilder.path(path); times = 3;
        }};
    }

    @Test
    public void testQuery() throws Exception {
        final RestClient result = tested.query(key, value);
        assertThat(result).isNotSameAs(tested);
        new Verifications() {{
            uriBuilder.queryParam(key, value);
        }};
    }

    @Test
    public void testHeaderEmpty() throws Exception {
        final RestClient result = tested.header(key, value);
        assertThat(result).isNotSameAs(tested);

        final Map<String, String> headers = getField(result, headerField);
        assertThat(headers).containsOnly(entry(key, value));
    }

    @Test
    public void testHeaderNotEmpty() throws Exception {
        final RestClient result = tested.header(key, value).header(key, value);
        assertThat(result).isNotSameAs(tested);

        final Map<String, String> headers = getField(result, headerField);
        assertThat(headers).containsOnly(entry(key, value), entry(key, value));
    }

    @Test
    public void testCookieStrings() throws Exception {
        new Expectations(tested) {{
            tested.cookie((Cookie) any); result = tested;
        }};
        final RestClient result = tested.cookie(key, value);
        assertThat(result).isSameAs(tested);
        new Verifications() {{
            new Cookie(key, value);
        }};
    }

    @Test
    public void testCookieEmpty() throws Exception {
        final RestClient result = tested.cookie(cookie);
        assertThat(result).isNotSameAs(tested);

        final List<Cookie> cookies = getField(result, cookiesField);
        assertThat(cookies).containsOnly(cookie);
    }

    @Test
    public void testCookieNotEmpty() throws Exception {
        final RestClient result = tested.cookie(cookie).cookie(cookie);
        assertThat(result).isNotSameAs(tested);

        final List<Cookie> cookies = getField(result, cookiesField);
        assertThat(cookies).containsOnly(cookie, cookie);
    }

    @Test
    public void testRequestSingle() throws Exception {
        final RestClient result = tested.request(APPLICATION_JSON_TYPE);
        assertThat(result).isNotSameAs(tested);

        final MediaType[] request = getField(result, requestField);
        assertThat(request).containsExactly(APPLICATION_JSON_TYPE);
    }

    @Test
    public void testRequestMultiple() throws Exception {
        final RestClient result = tested.request(APPLICATION_JSON_TYPE, TEXT_PLAIN_TYPE);
        assertThat(result).isNotSameAs(tested);

        final MediaType[] request = getField(result, requestField);
        assertThat(request).containsExactly(APPLICATION_JSON_TYPE, TEXT_PLAIN_TYPE);
    }

    @Test
    public void testAcceptSingle() throws Exception {
        final RestClient result = tested.accept(APPLICATION_JSON_TYPE);
        assertThat(result).isNotSameAs(tested);

        final MediaType[] accept = getField(result, acceptField);
        assertThat(accept).containsExactly(APPLICATION_JSON_TYPE);
    }

    @Test
    public void testAcceptMultiple() throws Exception {
        final RestClient result = tested.accept(APPLICATION_JSON_TYPE, TEXT_PLAIN_TYPE);
        assertThat(result).isNotSameAs(tested);

        final MediaType[] accept = getField(result, acceptField);
        assertThat(accept).containsExactly(APPLICATION_JSON_TYPE, TEXT_PLAIN_TYPE);
    }

    @Test
    public void testGetResponse() throws Exception {
        new Expectations(tested) {{
            invoke(tested, getBuilderMethod, new Class<?>[]{WebTarget.class}, (WebTarget) any); result = builder;
            builder.get(); result = response;
        }};
        assertThat(tested.get()).isSameAs(response);
    }

    @Test
    public void testGetObject() throws Exception {
        new Expectations(tested) {{
            invoke(tested, "get"); result = response;
            response.readEntity(String.class); result = value;
        }};
        assertThat(tested.get(String.class)).isSameAs(value);
    }

    @Test
    public void testPutResponse() throws Exception {
        new Expectations(tested) {{
            invoke(tested, getBuilderMethod, new Class<?>[]{WebTarget.class}, (WebTarget) any); result = builder;
            builder.put(entity); result = response;
        }};
        assertThat(tested.put(entity)).isSameAs(response);
    }

    @Test
    public void testPutObject() throws Exception {
        new Expectations(tested) {{
            invoke(tested, "put", entity); result = response;
            response.readEntity(String.class); result = value;
        }};
        assertThat(tested.put(entity, String.class)).isSameAs(value);
    }

    @Test
    public void testPostResponse() throws Exception {
        new Expectations(tested) {{
            invoke(tested, getBuilderMethod, new Class<?>[]{WebTarget.class}, (WebTarget) any); result = builder;
            builder.post(entity); result = response;
        }};
        assertThat(tested.post(entity)).isSameAs(response);
    }

    @Test
    public void testPostObject() throws Exception {
        new Expectations(tested) {{
            invoke(tested, "post", entity); result = response;
            response.readEntity(String.class); result = value;
        }};
        assertThat(tested.post(entity, String.class)).isSameAs(value);
    }

    @Test
    public void testDeleteResponse() throws Exception {
        new Expectations(tested) {{
            invoke(tested, getBuilderMethod, new Class<?>[]{WebTarget.class}, (WebTarget) any); result = builder;
            builder.delete(); result = response;
        }};
        assertThat(tested.delete()).isSameAs(response);
    }

    @Test
    public void testDeleteObject() throws Exception {
        new Expectations(tested) {{
            invoke(tested, "delete"); result = response;
            response.readEntity(String.class); result = value;
        }};
        assertThat(tested.delete(String.class)).isSameAs(value);
    }

    @Test
    public void testCurrentPath() throws Exception {
        tested.currentPath();
        new Verifications() {{
            uriBuilder.toTemplate();
        }};
    }

    @Test
    public void testGetBuilder() throws Exception {
        new Expectations() {{
            target.request((MediaType[]) any).accept((MediaType[]) any); result = builder;
        }};
        final Invocation.Builder result = invoke(tested, "getBuilder", target);
        assertThat(result).isSameAs(builder);
    }
}
package integration;

import org.junit.*;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithServer;

import play.test.*;


import java.util.concurrent.CompletionStage;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static play.test.Helpers.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;


/**
 * Integration testing that involves starting up an application or a server.
 * <p>
 * https://www.playframework.com/documentation/2.5.x/JavaFunctionalTest
 */
public class IntegrationTest extends WithServer {

    @Test
    public void testHomePage() throws Exception {
        // Tests using a scoped WSClient to talk to the server through a port.
        try (WSClient ws = WS.newClient(this.testServer.port())) {
            CompletionStage<WSResponse> stage = ws.url("/ns").get();
            WSResponse response = stage.toCompletableFuture().get();
            String body = response.getBody();
            assertThat(body, containsString("Notification Specification"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * same as testHomePage() but the approach is different.
     * @throws Exception
     */
    @Test
    public void testInServerThroughApp() throws Exception {
        // Tests using the internal application available in the server.
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/ns");

        Result result = route(app, request);
        final String body = contentAsString(result);
        assertThat(body, containsString("Notification Specification"));
    }

    /**
     * we need to find out how to insert sample data and so we can uncomment the following tests.
     */
    @Test
    public void testUI() {
        running(testServer(9002, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:9002");

            assertThat(browser.$("header h1").first().getText(), equalTo("Play application — Notification Specification"));
            assertThat(browser.$("section h1").first().getText(), equalTo("No notification specifications found"));
            //assertThat(browser.$("section h1").first().getText(), equalTo("7 notification specifications found"));

            //assertThat(browser.$("#pagination li.current").first().getText(), equalTo("Displaying 1 to 7 of 7"));
            //browser.$("#pagination li.next a").click();

            //assertThat(browser.$("#pagination li.current").first().getText(), equalTo("Displaying 11 to 20 of 574"));
            //browser.$("#searchbox").text("amd");
            //browser.$("#searchsubmit").click();

            //assertThat(browser.$("section h1").first().getText(), equalTo("One notification specification found"));
            //browser.$("a", withText("amd")).click();

            //assertThat(browser.$("section h1").first().getText(), equalTo("Edit Notification Specification"));

            //browser.$("#nValue").text("amd123");
            //browser.$("input.primary").click();

            //assertThat(browser.$("section h1").first().getText(), equalTo("7 notification specifications found"));
            //assertThat(browser.$(".alert-message").first().getText(), equalTo("Done! Notification Specificationmost_likely_company has been updated"));

            //browser.$("#searchbox").text("amd123");
            //browser.$("#searchsubmit").click();

            //browser.$("a", withText("Apple II")).click();
            //browser.$("input.danger").click();

            //assertThat(browser.$("section h1").first().getText(), equalTo("6 notification specifications found"));
            //assertThat(browser.$(".alert-message").first().getText(), equalTo("Done! Notification Specification has been deleted"));

            //browser.$("#searchbox").text("amd");
            //browser.$("#searchsubmit").click();

            //assertThat(browser.$("section h1").first().getText(), equalTo("No notification specifications found"));
        });
    }


}

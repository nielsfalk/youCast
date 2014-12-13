package de.nielsfalk.podcast;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class FeedResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(FeedResource.class);
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void FeedShouldBeCreated() {
        String responseMsg = target().path("feed").request().get(String.class);
        assertThat(responseMsg, containsString("<itunes:subtitle>Der nagelneue Podcast zum Lorem Ypsum</itunes:subtitle>"));
    }
}

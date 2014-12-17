package com.github.axet.vget;

import org.junit.Test;

import static com.github.axet.vget.VGetBridge.realUrl;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class VGetBridgeTest {


    @Test
    public void shouldFindYoutubeUrl() {
        String uri = realUrl("https://www.youtube.com/watch?v=Nn-ZShqZ6mM&amp;feature=youtube_gdata").toString();

        assertThat(uri, containsString("dur=389.166"));
    }
}
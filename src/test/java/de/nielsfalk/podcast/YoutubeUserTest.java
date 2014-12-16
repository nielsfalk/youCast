package de.nielsfalk.podcast;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class YoutubeUserTest {
    @Test
    public void chanelShouldBeParsed() {
        YoutubeUser tomBoSphere = new YoutubeUser("TomBoSphere");

        Rss.Channel channel = tomBoSphere.getChannel();

        assertThat(channel.image.href, is("https://yt3.ggpht.com/--YNSN0GFz6E/AAAAAAAAAAI/AAAAAAAAAAA/Vl9bz_AhAHc/s88-c-k-no/photo.jpg"));
        assertThat(channel.title, is("TomBoSphere"));
        assertThat(channel.link, is("https://www.youtube.com/user/TomBoSphere"));

    }
}
package de.nielsfalk.youCast;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
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

    @Test
    public void feedShouldBeCreated() {
        Rss niles = new YoutubeUser("niles781").getFeed("https://youCast.org/");

        assertThat(niles.channel.image.href, is("https://yt3.ggpht.com/-bmxPx2cdAb8/AAAAAAAAAAI/AAAAAAAAAAA/Z94xCf16BDU/s88-c-k-no/photo.jpg"));
        assertThat(niles.channel.title, is("niles781"));
        assertThat(niles.channel.link, is("https://www.youtube.com/user/niles781"));
        Rss.Item item = niles.channel.items.get(0);
        assertThat(item.title, is("25"));
        assertThat(item.description, is("This video is about stairs"));
        assertThat(item.duration, is(75));
        assertThat(item.enclosure.url, is("https://youCast.org/https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DBSXXITYa6nI%26feature%3Dyoutube_gdata/25.mp4"));

    }
}
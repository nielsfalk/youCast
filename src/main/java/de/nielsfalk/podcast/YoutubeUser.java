package de.nielsfalk.podcast;

import de.nielsfalk.podcast.Rss.Channel;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

/**
 * @author Niels Falk
 */
public class YoutubeUser {
    private final String user;

    public YoutubeUser(String user) {
        this.user = user;
    }

    public Channel getChannel() {
        try {
            String xml = IOUtils.toString(new URL("https://gdata.youtube.com/feeds/api/users/"+user), "UTF8");
            Document document = Jsoup.parse(xml);

            Channel result = new Channel();


            return result
                    .image(document.getElementsByTag("media:thumbnail").attr("url"))
                    .title(document.getElementsByTag("title").text())
                    .link("https://www.youtube.com/user/"+user)
                    .description(document.getElementsByTag("content").text())
                    .author(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

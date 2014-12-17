package de.nielsfalk.youCast;

import de.nielsfalk.youCast.Rss.Channel;
import de.nielsfalk.youCast.Rss.Item;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.bind.DatatypeConverter.parseDateTime;

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
            String xml = IOUtils.toString(new URL("https://gdata.youtube.com/feeds/api/users/" + user), "UTF8");
            Document document = Jsoup.parse(xml);


            return new Channel()
                    .image(document.getElementsByTag("media:thumbnail").attr("url"))
                    .title(document.getElementsByTag("title").text())
                    .link("https://www.youtube.com/user/" + user)
                    .description(document.getElementsByTag("content").text())
                    .author(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Item> getUploads(String requestURL) {
        try {
            String xml = IOUtils.toString(new URL("https://gdata.youtube.com/feeds/api/users/" + user + "/uploads"), "UTF8");
            List<Item> result = new ArrayList<>();
            Document document = Jsoup.parse(xml);
            for (Element upload : document.getElementsByTag("entry")) {
                String title = upload.getElementsByTag("title").text();
                int duration = Integer.parseInt(upload.getElementsByAttribute("duration").attr("duration"));
                String link = upload.getElementsByAttributeValue("rel", "alternate").attr("href");

                result.add(
                        new Item(link, requestURL, title)
                                .author(upload.getElementsByTag("author").get(0).getElementsByTag("name").text())
                                .description(upload.getElementsByTag("content").text())
                                .pubDate(parseDateTime(upload.getElementsByTag("published").text()).getTime())
                                .duration(duration)
                );
            }


            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Rss getFeed(String requestURL) {
        Channel channel = getChannel();
        channel.setItems(getUploads(requestURL));
        return new Rss(channel);
    }
}

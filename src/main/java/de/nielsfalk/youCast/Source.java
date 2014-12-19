package de.nielsfalk.youCast;

import com.github.axet.vget.VGetBridge;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static de.nielsfalk.youCast.PodcastRss.title;
import static javax.xml.bind.DatatypeConverter.parseDateTime;

/**
 * @author Niels Falk
 */
public enum Source {
    youtube {
        @Override
        public PodcastRss getFeed(String user, String requestUrl) {
            return new YoutubeUser(user).getFeed(requestUrl);
        }

        @Override
        public URI realUrl(String playback) {
            return VGetBridge.realUrl(playback);
        }
    }, json {
        @Override
        public PodcastRss getFeed(String parameter, String requestUrl) {
            return null;
        }

        @Override
        public URI realUrl(String playback) {
            return null;
        }
    };

    public abstract PodcastRss getFeed(String parameter, String requestUrl);

    public abstract URI realUrl(String playback);

    private static class YoutubeUser {
        private final String user;

        private YoutubeUser(String user) {
            this.user = user;
        }

        private PodcastRss.Channel getChannel() {
            try {
                String xml = IOUtils.toString(new URL("https://gdata.youtube.com/feeds/api/users/" + user), "UTF8");
                Document document = Jsoup.parse(xml);

                return title(document.getElementsByTag("title").text())
                        .image(document.getElementsByTag("media:thumbnail").attr("url"))
                        .title(document.getElementsByTag("title").text())
                        .link("https://www.youtube.com/user/" + user)
                        .description(document.getElementsByTag("content").text())
                        .author(user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private List<PodcastRss.Item> getUploads(String requestURL) {
            try {
                String xml = IOUtils.toString(new URL("https://gdata.youtube.com/feeds/api/users/" + user + "/uploads"), "UTF8");
                List<PodcastRss.Item> result = new ArrayList<>();
                Document document = Jsoup.parse(xml);
                for (Element upload : document.getElementsByTag("entry")) {
                    String title = upload.getElementsByTag("title").text();
                    int duration = Integer.parseInt(upload.getElementsByAttribute("duration").attr("duration"));
                    String link = upload.getElementsByAttributeValue("rel", "alternate").attr("href");

                    result.add(
                            new PodcastRss.Item(link, requestURL, title)
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

        private PodcastRss getFeed(String requestURL) {
            return getChannel().items(getUploads(requestURL)).rss();
        }
    }
}

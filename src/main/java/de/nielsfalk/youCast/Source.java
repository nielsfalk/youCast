package de.nielsfalk.youCast;

import de.nielsfalk.youCast.PodcastRss.Adapters.DurationAdapter;
import de.nielsfalk.youCast.PodcastRss.Adapters.PubDateAdapter;
import de.nielsfalk.youCast.PodcastRss.Item;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
        public PodcastRss getFeed(String jsonUrl, String requestUrl) {
            return new PodcastJson(jsonUrl, requestUrl).getFeed();
        }

        @Override
        public URI realUrl(String playback) {
            try {
                return new URI(playback);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }
        }
    };

    private static class PodcastJson {

        private final JSONObject jsonObject;
        private final String jsonUrl;
        private final String requestUrl;
        private String urlSuffix = "";
        private String urlPrefix = "";

        public PodcastJson(String jsonUrl, String requestUrl) {
            this.jsonUrl = jsonUrl;
            this.requestUrl = requestUrl;
            try {
                String json = IOUtils.toString(new URL(jsonUrl), "UTF8");
                jsonObject = new JSONObject(json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            urlPrefix = jsonUrl;
            for (String separator : new String[]{"/?", "?"}) {
                if (jsonUrl.contains(separator)) {
                    int separatorIndex = jsonUrl.lastIndexOf(separator);
                    urlSuffix = jsonUrl.substring(separatorIndex);
                    urlPrefix = urlPrefix.substring(0, separatorIndex);
                    break;
                }
            }
            urlPrefix = urlPrefix.substring(0, urlPrefix.lastIndexOf("/")+1);

        }

        public PodcastRss getFeed() {
            return title(jsonObject.getString("title"))
                    .image(relativeUrl(jsonObject.getString("image")))
                    .link(relativeUrl(jsonObject.getString("link")))
                    .description(jsonObject.getString("description"))
                    .author(jsonObject.getString("author"), jsonObject.getString("email"))
                    .items(readItems())
                    .rss();
        }

        private List<Item> readItems() {
            JSONArray items = jsonObject.getJSONArray("items");
            ArrayList<Item> result = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                JSONObject jsonItem = items.getJSONObject(i);
                try {
                    result.add(new Item(relativeUrl(jsonItem.getString("file")), requestUrl, jsonItem.getString("title"))
                            .author(jsonItem.has("author") ? jsonItem.getString("author") : null)////todo: niels 22.12.2014 :if empty use channel
                            .description(jsonItem.getString("description"))
                            .pubDate(new PubDateAdapter().unmarshal(jsonItem.getString("pubDate")))
                            .duration(new DurationAdapter().unmarshal(jsonItem.getString("duration"))));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return result;
        }

        private String relativeUrl(String href) {
            if (href.startsWith("http")){
                return href;
            }
            return urlPrefix + href + urlSuffix;
        }
    }

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
                        .link("https://www.youtube.com/user/" + user)
                        .description(document.getElementsByTag("content").text())
                        .author(user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private List<Item> getUploads(String requestURL) {
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

        private PodcastRss getFeed(String requestURL) {
            return getChannel().items(getUploads(requestURL)).rss();
        }
    }
}

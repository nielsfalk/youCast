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
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static de.nielsfalk.youCast.PodcastRss.title;
import static javax.xml.bind.DatatypeConverter.parseDateTime;
import static org.apache.commons.lang3.StringUtils.removeEnd;

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
    }, vimeo {
        @Override
        public PodcastRss getFeed(String user, String requestUrl) {
            return new VimeoUser(user).getFeed(requestUrl);
        }

        @Override
        public URI realUrl(String playback) {
            return VGetBridge.realUrl(playback);
        }
    };

    public abstract PodcastRss getFeed(String parameter, String requestUrl);

    public URI realUrl(String playback) {
        try {
            return new URI(playback);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static class VimeoUser {
        private final String user;

        public VimeoUser(String user) {
            this.user = user;
        }

        public PodcastRss getFeed(String requestUrl) {
            try {
                URL url = new URL("https://vimeo.com/" + user + "/videos/rss");
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                try (InputStream is = url.openStream()) {
                    org.w3c.dom.Document document = builder.parse(is);

                    XPath xPath = XPathFactory.newInstance().newXPath();

                    return title(xPath.compile("/rss/channel/title").evaluate(document))
                            .image(xPath.compile("/rss/channel/image/url").evaluate(document))
                            .link(xPath.compile("/rss/channel/link").evaluate(document))
                            .description("/rss/channel/description")
                            .author(user).items(getItems(requestUrl, document, xPath)).rss();
                }
            } catch (IOException | ParserConfigurationException | XPathExpressionException | SAXException e) {
                throw new RuntimeException(e);
            }

        }

        private List<Item> getItems(String requestUrl, org.w3c.dom.Document document, XPath xPath) throws XPathExpressionException {
            ArrayList<Item> result = new ArrayList<>();
            for (int i = 1; ; i++) {
                String pathPrefix = "/rss/channel/item[" + i + "]";
                String link = xPath.compile(pathPrefix + "/link").evaluate(document);
                try {
                    String time = xPath.compile(pathPrefix + "/pubDate").evaluate(document);
                    Date pubDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").parse(time);

                    result.add(
                            new Item(link, requestUrl, xPath.compile(pathPrefix + "/title").evaluate(document))
                                    .author(xPath.compile(pathPrefix + "/dc:creator").evaluate(document))
                                    .description(xPath.compile(pathPrefix + "/description").evaluate(document))
                                    .pubDate(pubDate));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                if (link.equals(xPath.compile("/rss/channel/item[last()]/link").evaluate(document))) {
                    break;
                }
            }
            return result;
        }
    }

    private static class PodcastJson {

        private final JSONObject jsonObject;
        private final String requestUrl;
        private String urlSuffix = "";
        private String urlPrefix = "";

        public PodcastJson(String jsonUrl, String requestUrl) {
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
            urlPrefix = urlPrefix.substring(0, urlPrefix.lastIndexOf("/") + 1);

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
            if (href.startsWith("http")) {
                return href;
            }
            return urlPrefix + href + urlSuffix;
        }
    }

    private static class YoutubeUser {
        private final String user;
        private Document rss;
        private Document html;

        private YoutubeUser(String user) {
            this.user = user;
        }

        private PodcastRss.Channel getChannel() {
                return title(removeEnd(getHtml().getElementsByTag("title").text(), "\n - YouTube"))
                        .image(getHtml().getElementsByClass("channel-header-profile-image").attr("src"))
                        .link("https://www.youtube.com/user/" + user)
                        .author(user);
        }

        private List<Item> getUploads(String requestURL) {
            List<Item> result = new ArrayList<>();
            Iterator<Element> pageIterator = getHtml().getElementsByClass("feed-item-container").iterator();
            Iterator<Element> rssIterator = getRss().getElementsByTag("entry").iterator();
            while (pageIterator.hasNext() && rssIterator.hasNext()){
                Element pageElement = pageIterator.next();
                Element rssElement = rssIterator.next();
                Element a = pageElement.getElementsByTag("a").last();
                String title = a.text();
                int duration = new DurationAdapter().unmarshal(pageElement.getElementsByClass("video-time").text());
                String link = "https://www.youtube.com" +pageElement.getElementsByTag("a").last().attr("href");
                result.add(
                        new Item(link, requestURL, title)
                                .author(user)
                                .description(pageElement.getElementsByClass("yt-lockup-description").text())
                                .pubDate(parseDateTime(rssElement.getElementsByTag("published").text()).getTime())
                                .duration(duration)
                );
            }
            return result;
        }

        private PodcastRss getFeed(String requestURL) {
            return getChannel().items(getUploads(requestURL)).rss();
        }

        private Document getHtml() {
            if (html == null) {
                try {
                    html = Jsoup.parse(IOUtils.toString(new URL("https://www.youtube.com/user/" + user + "/videos?flow=list"), "UTF8"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return html;
        }

        private Document getRss() {
            if (rss == null) {
                String rssUrl = getHtml().getElementsByAttributeValue("title", "RSS").attr("href");
                try {
                    rss = Jsoup.parse(IOUtils.toString(new URL(rssUrl), "UTF8"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return rss;
        }
    }
}

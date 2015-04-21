package de.nielsfalk.youCast;

import de.nielsfalk.youCast.PodcastRss.Adapters.DurationAdapter;
import de.nielsfalk.youCast.PodcastRss.Adapters.PubDateAdapter;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Niels Falk
 */
@SuppressWarnings("UnusedDeclaration")
@XmlRootElement(name = "rss")
public class PodcastRss {

    @XmlAttribute(name = "xmlns:itunes")
    private final String iTunes = "http://www.itunes.com/dtds/podcast-1.0.dtd";

    @XmlAttribute
    private final String version = "2.0";

    @XmlAttribute(name = "xmlns:atom")
    private final String atom = "http://www.w3.org/2005/Atom";

    @XmlElement
    Channel channel;

    public PodcastRss() {
    }

    private PodcastRss(Channel channel) {
        this();
        this.channel = channel;
    }

    static Channel title(String title) {
        return new Channel()
                .title(title);
    }

    private static String encode(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Channel {
        @XmlElement
        String title;

        @XmlElement
        String link;

        @XmlElement
        private final String language = "de";

        @XmlElement(name = "itunes:subtitle")
        String subTitle;

        @XmlElement(name = "itunes:author")
        private String author;

        @XmlElement
        String description;

        @XmlElement(name = "itunes:summary")
        private String summary;

        @XmlElement(name = "itunes:explicit")
        private final String explicit = "no";

        @XmlElement(name = "itunes:owner")
        private Owner owner;

        @XmlElement(name = "itunes:image")
        Image image;

        @XmlElement(name = "item")
        List<Item> items;

        public Channel image(String href) {
            image = new Image(href);
            return this;
        }

        public Channel title(String title) {
            this.title = title;
            return this;
        }

        public Channel link(String link) {
            this.link = link;
            return this;
        }

        public Channel description(String description) {
            this.description = description;
            subTitle = description;
            summary = description;
            return this;
        }

        public Channel author(String user, String email) {
            author = user;
            owner = new Owner(user, email);
            return this;
        }

        public Channel author(String user) {
            return author(user, user + '@' + user + ".buzz");
        }

        public Channel items(List<Item> items) {
            this.items = items;
            return this;
        }

        public PodcastRss rss() {
            return new PodcastRss(this);
        }
    }

    public static class Item {
        @XmlElement
        String title;

        @XmlElement(name = "itunes:author")
        private String author;

        @XmlElement(name = "itunes:subtitle")
        private String subTitle;

        @XmlElement(name = "itunes:summary")
        private String summary;

        @XmlElement
        String description;

        @XmlElement
        final Enclosure enclosure;

        @XmlElement
        private final String link;

        @XmlElement
        @XmlJavaTypeAdapter(value = PubDateAdapter.class)
        Date pubDate;

        @XmlElement(name = "itunes:duration")
        @XmlJavaTypeAdapter(value = DurationAdapter.class)
        Integer duration;

        @XmlElement(name = "itunes:explicit")
        private final String explicit = "no";

        public Item(String link, String requestURL, String title) {
            this.link = link;
            this.title = title;
            subTitle = title;
            summary = title;

            StringBuilder url = new StringBuilder().append(requestURL);
            if (!url.toString().endsWith("/")) {
                url.append('/');
            }

            url.append(encode(link));

            url.append('/');
            url.append(createFileName(title));
            enclosure = new Enclosure(url.toString());

        }

        private String createFileName(String title) {
            if (StringUtils.isEmpty(title)) {
                return "video.mp4";
            }
            return encode(title) + ".mp4";
        }

        public Enclosure getEnclosure() {
            return enclosure;
        }

        public Item title(String title) {
            this.title = title;
            subTitle = title;
            summary = title;
            return this;
        }

        public Item author(String author) {
            this.author = author;
            return this;
        }

        public Item description(String description) {
            this.description = description;
            return this;
        }

        public Item pubDate(Date pubDate) {
            this.pubDate = pubDate;
            return this;
        }

        public Item duration(int duration) {
            this.duration = duration;
            return this;
        }
    }

    public static class Enclosure {
        @XmlAttribute
        final String url;

        @XmlAttribute
        private final String type = "video/mpeg";

        public Enclosure(String url) {
            this.url = url;
        }
    }

    public static class Image {
        @XmlAttribute
        final String href;

        public Image(String href) {
            this.href = href;
        }
    }

    public static class Owner {

        @XmlElement(name = "itunes:name")
        private final String name;

        @XmlElement(name = "itunes:email")
        private final String email;

        public Owner(String name, String email) {
            this.name = name;

            this.email = email;
        }
    }

    public static class Adapters {
        public static class PubDateAdapter extends XmlAdapter<String, Date> {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            @Override
            public String marshal(Date date) throws Exception {
                return dateFormat.format(date);
            }

            @Override
            public Date unmarshal(String string) throws Exception {
                return dateFormat.parse(string);
            }
        }

        public static class DurationAdapter extends XmlAdapter<String, Integer> {

            @Override
            public Integer unmarshal(String string){
                int i = 0;
                for (String part : StringUtils.split(string, ':')) {
                    i *= 60;
                    i += Integer.parseInt(part);
                }
                return i;
            }

            @Override
            public String marshal(Integer integer) throws Exception {
                Integer minute = integer / 60;
                Integer second = integer % 60;

                return minute.toString() + ':' + StringUtils.leftPad(second.toString(), 2, '0');
            }
        }
    }
}

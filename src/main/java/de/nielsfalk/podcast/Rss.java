package de.nielsfalk.podcast;

import org.jsoup.select.Elements;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * @author Niels Falk
 */
@SuppressWarnings("UnusedDeclaration")
@XmlRootElement(name = "rss")
public class Rss {

    @XmlAttribute(name = "xmlns:itunes")
    private final String iTunes = "http://www.itunes.com/dtds/podcast-1.0.dtd";

    @XmlAttribute
    private final String version = "2.0";

    @XmlAttribute(name = "xmlns:atom")
    private final String atom = "http://www.w3.org/2005/Atom";

    @XmlElement
    private Channel channel;

    public Rss(HttpServletRequest request) {
        this();
        channel = new Channel();
        channel.setItems(Arrays.asList(new Item(channel, request)));
    }

    public Rss() {

    }


    public static class Channel {
        @XmlElement
        String title = "Der Spezialist";

        @XmlElement
        String link = "http://www.nielsfalk.de/";

        @XmlElement
        private final String language = "de";

        @XmlElement(name = "itunes:subtitle")
        String subTitle = "Der nagelneue Podcast zum Lorem Ypsum";

        @XmlElement(name = "itunes:author")
        private String author = "Niels Falk";

        @XmlElement
        String description = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

        @XmlElement(name = "itunes:summary")
        private String summary = subTitle;

        @XmlElement(name = "itunes:explicit")
        private final String explicit = "no";

        @XmlElement(name = "itunes:owner")
        private Owner owner = new Owner("Niels", "Podcast@Nielsfalk.de");

        @XmlElement(name = "itunes:image")
        Image image;

        @XmlElement(name = "item")
        List<Item> items;

        public void setItems(List<Item> items) {
            this.items = items;
        }


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

        public Channel author(String user) {
            author = user;
            owner = new Owner(user, user + '@' + user + ".buzz");
            return this;
        }
    }

    public static class Item{
        @XmlElement
        private final String title = "Niels Falk 42010";

        @XmlElement(name = "itunes:author")
        private final String author;

        @XmlElement(name = "itunes:subtitle")
        private final String subTitle = "Niels Falk 42010";

        @XmlElement(name = "itunes:summary")
        private final String summary = subTitle;

        @XmlElement
        private final String description = "Niels Falk unicycling in Hamburg (Marco Polo Terrassen, Eimsbusch Skatepark, Acker Pool Co) and Berlin (Velodrom, Volkspark Friedrichshain, Schmetterlingsghetto). With Dori Lehmann, Uli Malende, Niko Wilbert and Nadine Wegner as guestriders. The Music is from Fuo (Carca, Zrk)";

        @XmlElement
        private final Enclosure enclosure;

        @XmlElement
        private final String link = "https://www.youtube.com/watch?v=NswSP0Hrh9Q";

        @XmlElement
        private final String pubDate = "Mon, 25 Aug 2014 00:00:00 GMT";//todo :use Date and converter

        @XmlElement(name = "itunes:duration")
        private final String duration = "1:18:00";

        @XmlElement(name = "itunes:explicit")
        private final String explicit = "no";


        public Item(Channel channel, HttpServletRequest request) {
            author = channel.author;
            StringBuffer url = request.getRequestURL();
            if (!url.toString().endsWith("/")) {
                url.append('/');
            }

            String videoPlayback = "https://www.youtube.com/watch?v=NswSP0Hrh9Q";
            try {
                url.append(URLEncoder.encode(videoPlayback, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            url.append("/video.mp4");
            enclosure = new Enclosure(url.toString(), 209515617);

        }

        public Enclosure getEnclosure() {
            return enclosure;
        }
    }

    private static class Enclosure {
        @XmlAttribute
        private final String url;
        //@XmlAttribute
        private final int length;

        @XmlAttribute
        private final String type = "video/mpeg";

        public Enclosure(String url, int length) {
            this.url = url;
            this.length = length;
        }
    }

    public static class Image {
        @XmlAttribute
        final String href;

        public Image(String href) {
            this.href = href;
        }
    }

    public static class Owner{

        @XmlElement(name = "itunes:name")
        private final String name;

        @XmlElement(name = "itunes:email")
        private final String email;

        public Owner(String name, String email) {
            this.name = name;

            this.email = email;
        }
    }

    public static class AtomLink {

    }
}

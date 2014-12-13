package de.nielsfalk.podcast;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
    private final Channel channel = new Channel();


    public static class Channel {
        @XmlElement
        private final String title = "Der Spezialist";

        @XmlElement
        private final String link = "http://www.nielsfalk.de/";

        @XmlElement
        private final String language = "de";

        @XmlElement(name = "itunes:subtitle")
        private final String subTitle = "Der nagelneue Podcast zum Lorem Ypsum";

        @XmlElement(name = "itunes:author")
        private final String author = "Niels Falk";

        @XmlElement
        private final String description = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

        @XmlElement(name = "itunes:summary")
        private final String summary = subTitle;

        @XmlElement(name = "itunes:explicit")
        private final String explicit = "no";

        @XmlElement(name = "itunes:owner")
        private final Owner owner = new Owner("Niels", "Podcast@Nielsfalk.de");

        @XmlElement(name = "itunes:image")
        private final Image image = new Image("http://nielsfalk.de/_nielsFalk.png");

        @XmlElement(name = "item")
        List<Item> items = Arrays.asList(new Item(this));
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
        private final Enclosure enclosure = new Enclosure("https://r14---sn-5hn7ym7s.googlevideo.com/videoplayback?key=yt5&ip=79.201.239.17&sparams=dur%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Cmime%2Cmm%2Cms%2Cmv%2Cnh%2Cratebypass%2Crequiressl%2Csource%2Cupn%2Cexpire&signature=89B087E1044ED171EE331B03CB6BF1E91BC75514.2776A419BB941117FAF922F7627596ECD292DCF5&id=o-ADsTTeISLeymssE_n8-6dekHTimffW1u2tjz7gaooyZV&ms=au&mt=1418500031&mv=m&dur=0.000&mime=video%2Fwebm&sver=3&source=youtube&initcwndbps=657500&requiressl=yes&nh=EAE&ratebypass=yes&ipbits=0&expire=1418521721&mm=31&fexp=900234%2C900718%2C905639%2C908213%2C917000%2C924638%2C927622%2C932404%2C939973%2C9405576%2C941004%2C943917%2C947209%2C947218%2C948124%2C952302%2C952605%2C952901%2C953912%2C957103%2C957105%2C957201&upn=W99ifZWLOnw&itag=43&signature=89B087E1044ED171EE331B03CB6BF1E91BC75514.2776A419BB941117FAF922F7627596ECD292DCF5", 209515617);

        @XmlElement
        private final String link = "https://www.youtube.com/watch?v=FhLnyNwStiE";

        @XmlElement
        private final String pubDate = "Mon, 25 Aug 2014 00:00:00 GMT";//todo :use Date and converter

        @XmlElement(name = "itunes:duration")
        private final String duration = "1:18:00";

        @XmlElement(name = "itunes:explicit")
        private final String explicit = "no";


        public Item(Channel channel) {
            author = channel.author;
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
        private final String href;

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

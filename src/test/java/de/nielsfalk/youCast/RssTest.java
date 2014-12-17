package de.nielsfalk.youCast;

import de.nielsfalk.youCast.Rss.Channel;
import de.nielsfalk.youCast.Rss.Item;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Date;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class RssTest {


    @Test
    public void xmlShouldBeWritten() {
        assertThat(toXml(createTestFeed()), is(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<rss xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\" version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
                        "    <channel>\n" +
                        "        <title>the Title</title>\n" +
                        "        <link>https://someone.org</link>\n" +
                        "        <language>de</language>\n" +
                        "        <itunes:subtitle>someones podcast</itunes:subtitle>\n" +
                        "        <itunes:author>someone</itunes:author>\n" +
                        "        <description>someones podcast</description>\n" +
                        "        <itunes:summary>someones podcast</itunes:summary>\n" +
                        "        <itunes:explicit>no</itunes:explicit>\n" +
                        "        <itunes:owner>\n" +
                        "            <itunes:name>someone</itunes:name>\n" +
                        "            <itunes:email>someone@someone.buzz</itunes:email>\n" +
                        "        </itunes:owner>\n" +
                        "        <itunes:image href=\"https://youCast.org/someOne/img.png\"/>\n" +
                        "        <item>\n" +
                        "            <title>episode3</title>\n" +
                        "            <itunes:author>someone</itunes:author>\n" +
                        "            <itunes:subtitle>episode3</itunes:subtitle>\n" +
                        "            <itunes:summary>episode3</itunes:summary>\n" +
                        "            <description>here is my third episode.</description>\n" +
                        "            <enclosure url=\"https://youcast.com/someone/https%3A%2F%2Fsomeone.org%2Fepisode3/episode3.mp4\" type=\"video/mpeg\"/>\n" +
                        "            <link>https://someone.org/episode3</link>\n" +
                        "            <pubDate>2014-12-17 01:28:13</pubDate>\n" +
                        "            <itunes:duration>58 1</itunes:duration>\n" +
                        "            <itunes:explicit>no</itunes:explicit>\n" +
                        "        </item>\n" +
                        "    </channel>\n" +
                        "</rss>\n"));
    }

    private Rss createTestFeed() {
        Channel channel = new Channel()
                .image("https://youCast.org/someOne/img.png")
                .title("the Title")
                .link("https://someone.org")
                .description("someones podcast")
                .author("someone");
        channel.setItems(asList(
                new Item("https://someone.org/episode3", "https://youcast.com/someone", "episode3")
                        .author("someone")
                        .description("here is my third episode.")
                        .pubDate(new Date(1418776093046l))
                        .duration(1)));
        return new Rss(channel);
    }

    public String toXml(Object entity) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(entity.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter stringWriter = new StringWriter();
            jaxbMarshaller.marshal(entity, stringWriter);

            return stringWriter.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }
}
package de.nielsfalk.youCast

import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

import static de.nielsfalk.youCast.Source.json
import static de.nielsfalk.youCast.Source.vimeo
import static de.nielsfalk.youCast.Source.youtube
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static de.nielsfalk.youCast.PodcastRss.Adapters.PubDateAdapter

/**
 * @author Niels Falk
 */
class FeedResourceSpec extends Specification {
    private FeedResource resource

    def "videoUrlPrefix"() {
        request("https://youCast.org/youtube/TomBoSphere")

        expect:
        resource.videoUrlPrefix(youtube) == "https://youCast.org/youtube"
    }

    def "youtube"() {
        def rss = podCastRequest("https://youCast.org/", youtube, "niles781")
        def firstItem = rss.channel.items.get(0)

        expect:
        rss.channel.image.href == "https://yt3.ggpht.com/-bmxPx2cdAb8/AAAAAAAAAAI/AAAAAAAAAAA/Z94xCf16BDU/s88-c-k-no/photo.jpg"
        rss.channel.title == "niles781"
        rss.channel.link == "https://www.youtube.com/user/niles781"
        firstItem.title == "25"
        firstItem.description == "This video is about stairs"
        firstItem.duration == 75
        firstItem.enclosure.url == "https://youCast.org/youtube/https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DBSXXITYa6nI%26feature%3Dyoutube_gdata/25.mp4"
    }

    def "json"() {
        def rss = podCastRequest("https://youCast.org/", json, "https://content.wuala.com/contents/nielsfalk/myCast/nielsFalk.json/?dl=1")
        def firstItem = rss.channel.items.get(0)

        expect:
        rss.channel.image.href == "https://content.wuala.com/contents/nielsfalk/myCast/darth.jpeg/?dl=1"
        rss.channel.title == "Niels cannal"
        rss.channel.link == "http://elbtrial.com"
        firstItem.title == "Detmold"
        firstItem.author == "Sophia"
        firstItem.description == "Detmold"
        firstItem.pubDate == new PubDateAdapter().unmarshal("2014-09-24 00:03:57")
        firstItem.duration == 280
    }

    def "vimeo"(){
        def rss = podCastRequest("https://youCast.org/", vimeo, "sinco")
        def firstItem = rss.channel.items.get(0)

        expect:
        rss.channel.image.href == "https://i.vimeocdn.com/portrait/3189371_100x100.jpg"
        rss.channel.title == "Vimeo / sinco's videos"
        rss.channel.link == "http://vimeo.com/sinco/videos"
        firstItem.title == "this fire"
        firstItem.duration == null
        firstItem.enclosure.url == "https://youCast.org/vimeo/http%3A%2F%2Fvimeo.com%2F89392551/this+fire.mp4"


    }

    def PodcastRss podCastRequest(String baseUrl, Source source, String parameter) {
        request(baseUrl + source.name() + parameter)
        def podcast = resource.podcast(parameter, source)
        podcast.getEntity()
    }

    def request(String url) {
        resource = new FeedResource()
        resource.request = mock(HttpServletRequest.class)
        when(resource.request.getRequestURL()).thenReturn(new StringBuffer(url))
    }
}

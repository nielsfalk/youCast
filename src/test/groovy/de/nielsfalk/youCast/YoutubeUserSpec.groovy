package de.nielsfalk.youCast

import spock.lang.Specification

import static de.nielsfalk.youCast.Source.youtube

/**
 * @author Niels Falk
 */
class YoutubeUserSpec extends Specification {
    def "createFeed"() {
        def niles = youtube.getFeed("niles781", "https://youCast.org/")
        def firstItem = niles.channel.items.get(0)

        expect:
        niles.channel.image.href == "https://yt3.ggpht.com/-bmxPx2cdAb8/AAAAAAAAAAI/AAAAAAAAAAA/Z94xCf16BDU/s88-c-k-no/photo.jpg"
        niles.channel.title == "niles781"
        niles.channel.link == "https://www.youtube.com/user/niles781"
        firstItem.title == "25"
        firstItem.description == "This video is about stairs"
        firstItem.duration == 75
        firstItem.enclosure.url == "https://youCast.org/https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DBSXXITYa6nI%26feature%3Dyoutube_gdata/25.mp4"
    }
}

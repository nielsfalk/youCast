package de.nielsfalk.youCast

import spock.lang.Specification

import static de.nielsfalk.youCast.VGetBridge.realUrl


/**
 * @author Niels Falk
 */
class VGetBridgetSpec extends Specification {
    def "read video url"() {
        def url = realUrl("https://www.youtube.com/watch?v=Nn-ZShqZ6mM&amp;feature=youtube_gdata").toString();

        expect:
        url.contains("dur=389.166")
    }
}

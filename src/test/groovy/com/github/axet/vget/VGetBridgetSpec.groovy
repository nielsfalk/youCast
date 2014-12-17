package com.github.axet.vget

import spock.lang.Specification

import static com.github.axet.vget.VGetBridge.realUrl


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

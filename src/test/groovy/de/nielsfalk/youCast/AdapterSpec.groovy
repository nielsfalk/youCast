package de.nielsfalk.youCast

import de.nielsfalk.youCast.PodcastRss.Adapters.DurationAdapter
import de.nielsfalk.youCast.PodcastRss.Adapters.PubDateAdapter
import spock.lang.Specification

/**
 * @author Niels Falk
 */
class AdapterSpec extends Specification {
    def "marshal duration"() {
        expect:
        new DurationAdapter().marshal(givenSeconds) == expectedXml

        where:
        givenSeconds | expectedXml
        3            | "0:03"
        75           | "1:15"
        60 * 60 * 24 | "1440:00"
    }

    def "unmarshal duration"() {
        expect:
        new DurationAdapter().unmarshal(givenXml) == expectedSeconds

        where:
        givenXml  | expectedSeconds
        "0:03"    | 3
        "1:15"    | 75
        "1440:00" | 60 * 60 * 24
    }

    def "marshal pubDate"() {
        expect:
        def givenDate = new Date(givenTime)
        new PubDateAdapter().marshal(givenDate) == expectedXml

        where:
        givenTime     | expectedXml
        1418776093046 | "2014-12-17 01:28:13"
        0             | "1970-01-01 01:00:00"
    }

    def "unmarshal pubDate"() {
        expect:
        def expectedDate = new Date(expectedTime)
        new PubDateAdapter().unmarshal(givenXml) == expectedDate

        where:
        givenXml              | expectedTime
        "2014-12-17 01:28:13" | 1418776093000
        "1970-01-01 01:00:00" | 0
    }
}
package de.nielsfalk.youCast

import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

import static de.nielsfalk.youCast.Source.youtube
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

/**
 * @author Niels Falk
 */
class FeedResourceTest extends Specification {
    def "videoUrlPrefix"() {
        def resource = new FeedResource()
        resource.request = mock(HttpServletRequest.class)
        when(resource.request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/youtube/TomBoSphere"))

        expect:
        resource.videoUrlPrefix(youtube) == "http://localhost:8080/youtube"
    }
}

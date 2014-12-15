package de.nielsfalk.podcast;

import com.github.axet.vget.VGetBridge;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

import static javax.ws.rs.core.Response.Status.MOVED_PERMANENTLY;

@Path("feed")
@Produces({"application/rss+xml;charset=utf-8"})
public class FeedResource {

    @Context
    private HttpServletRequest request;

    @GET
    public Response feed() {
        return Response.ok(new Rss(request)).build();
    }

    @Path("{playback}/video.mp4")
    @GET
    public Response getVideo(@PathParam("playback") String playback) throws URISyntaxException {
        URI location = VGetBridge.realUrl(playback).toURI();
        return Response.status(MOVED_PERMANENTLY).location(location).build();
    }

}

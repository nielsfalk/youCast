package de.nielsfalk.youCast;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

import static javax.ws.rs.core.Response.Status.MOVED_PERMANENTLY;

@Path("/")
public class FeedResource {

    @Context
    HttpServletRequest request;

    @Path("{source}/{parameter}")
    @Produces({"application/rss+xml;charset=utf-8"})
    @GET
    public Response youtube(@PathParam("parameter") String parameter, @PathParam("source") Source source) {
        return Response.ok(source.getFeed(parameter, videoUrlPrefix(source))).build();
    }

    @Path("{source}/{playback}/{fileName}.mp4")
    @GET
    public Response getVideo(@PathParam("playback") String playback, @PathParam("source") Source source) throws URISyntaxException {
        return Response
                .status(MOVED_PERMANENTLY)
                .location(source.realUrl(playback)).build();
    }

    String videoUrlPrefix(Source source) {
        String request = this.request.getRequestURL().toString();
        return request.substring(0, request.indexOf(source.name())+source.name().length());
    }
}

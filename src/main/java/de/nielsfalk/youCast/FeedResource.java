package de.nielsfalk.youCast;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

import static com.github.axet.vget.VGetBridge.realUrl;
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

    @Path("{user}")
    @GET
    public Response youtube(@PathParam("user") String user) {
        return Response.ok(new YoutubeUser(user).getFeed(getRequestURL())).build();
    }

    @Path("{user}/{playback}/{fileName}.mp4")
    @GET
    public Response getVideo(@PathParam("playback") String playback, @PathParam("user") String user, @PathParam("fileName") String ignored) throws URISyntaxException {
        return Response
                .status(MOVED_PERMANENTLY)
                .location(realUrl(playback)).build();
    }

    private String getRequestURL() {
        return request.getRequestURL().toString();
    }

}

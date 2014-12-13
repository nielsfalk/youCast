package de.nielsfalk.podcast;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("feed")
@Produces({"application/rss+xml;charset=utf-8"})
public class FeedResource {

    @GET
    public Response getIt() {
        return Response.ok(new Rss()).build();
    }

}

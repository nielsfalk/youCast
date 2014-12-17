package de.nielsfalk.youCast;

import de.nielsfalk.youCast.Rss.Adapters.DurationAdapter;
import de.nielsfalk.youCast.Rss.Adapters.PubDateAdapter;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class AdaptersTest {

    @Test
    public void marshalDuration() throws Exception {
        assertThat(new DurationAdapter().marshal(3), is("0:03"));
        assertThat(new DurationAdapter().marshal(75), is("1:15"));
        assertThat(new DurationAdapter().marshal(60 * 60 * 24), is("1440:00"));
    }

    @Test
    public void unmarshalDuration() throws Exception {
        assertThat(new DurationAdapter().unmarshal("0:03"), is(3));
        assertThat(new DurationAdapter().unmarshal("1:15"), is(75));
        assertThat(new DurationAdapter().unmarshal("1440:00"), is(60 * 60 * 24));
    }

    @Test
    public void marshalPubdate() throws Exception {
        assertThat(new PubDateAdapter().marshal(new Date(1418776093046l)), is("2014-12-17 01:28:13"));
        assertThat(new PubDateAdapter().marshal(new Date(0)), is("1970-01-01 01:00:00"));
    }

    @Test
    public void unmarshalPubdate() throws Exception {
        assertThat(new PubDateAdapter().unmarshal("2014-12-17 01:28:13").getTime(), is(1418776093000l));
        assertThat(new PubDateAdapter().unmarshal("1970-01-01 01:00:00").getTime(), is((long) 0));
    }

}
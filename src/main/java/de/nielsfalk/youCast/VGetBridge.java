package de.nielsfalk.youCast;

import com.github.axet.vget.VGet;
import com.github.axet.vget.info.VideoInfo;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

class VGetBridge {
    public static URI realUrl(String url) {
        try {
            URL videoPage = new URL(url);
            VideoInfo info = VGet.parser(null, videoPage).info(videoPage);
            VGet vGet = new VGet(info, null);
            vGet.extract();
            return info.getInfo().getSource().toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


}

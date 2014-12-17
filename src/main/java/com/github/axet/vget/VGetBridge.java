package com.github.axet.vget;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class VGetBridge {
    public static URI realUrl(String url) {
        try {
            VGet vGet = new VGet(new URL(url), new File("."));
            vGet.extract();
            return vGet.info.getInfo().getSource().toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


}

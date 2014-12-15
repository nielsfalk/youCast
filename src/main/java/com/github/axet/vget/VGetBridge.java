package com.github.axet.vget;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class VGetBridge {
    public static URL realUrl(String url) {
        try {
            VGet vGet = new VGet(new URL(url), new File("."));
            vGet.extract();
            return vGet.info.getInfo().getSource();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


}

package com.parthparekh.algorithms;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

class WordListUtil {

    static String path() {
        try {
            URL res = WordListUtil.class.getClassLoader().getResource("wordlist/wordlist.txt");
            if (res == null) {
                throw new IllegalStateException("Resource not found");
            }
            File file = new File(res.toURI());
            return file.getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }
}

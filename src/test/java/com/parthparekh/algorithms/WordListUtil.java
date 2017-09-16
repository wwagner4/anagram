package com.parthparekh.algorithms;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

class WordListUtil {

    static File file() {
        try {
            URL res = WordListUtil.class.getClassLoader().getResource("wordlist/wordlist.txt");
            if (res == null) {
                throw new IllegalStateException("Resource not found");
            }
            return new File(res.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }
}

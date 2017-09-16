package com.parthparekh.algorithms;

import java.io.IOException;

class AnagramRunner {

    public static void run(String... arg) throws IOException {
        String[] args = {
                "/Users/wwagner4/prj/AnagramSolver/wordlist/wordlist.txt",
                "1",
                "wolfgang wagner"
        };
        AnagramSolver.run(args);
    }

}

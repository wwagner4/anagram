package com.parthparekh.algorithms;

import java.io.IOException;

public class AnagramRunner {

    public static void main(String... arg) throws IOException {
        String[] args = {
                "/Users/wwagner4/prj/AnagramSolver/wordlist/wordlist.txt",
                "1",
                "wolfgang wagner"
        };
        AnagramSolver.main(args);
    }

}

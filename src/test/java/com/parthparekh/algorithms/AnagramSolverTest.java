package com.parthparekh.algorithms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;


/**
 * Unit test for AnagramSolver class
 *
 * @author : Parth Parekh
 */
public class AnagramSolverTest {

    private AnagramSolver anagramSolver;

    @Before
    public void setUp() throws IOException {
        File f = WordListUtil.file();
        anagramSolver = new AnagramSolver(3, f);
    }

    @Test
    public void findAnagramsTest() throws IOException {
        Set<Set<String>> anagrams = anagramSolver.findAllAnagrams("silent");
        Assert.assertNotNull(anagrams);
        Assert.assertEquals(10, anagrams.size());
    }
}

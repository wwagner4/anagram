package com.parthparekh.algorithms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


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
        Iterator<Set<String>> anagrams = anagramSolver.findAllAnagrams("silent");
        List<Set<String>> sentances = new ArrayList<>();
        while(anagrams.hasNext()) {
            sentances.add(anagrams.next());
        }
        Assert.assertNotNull(anagrams);
        Assert.assertEquals(11, sentances.size());
    }
}

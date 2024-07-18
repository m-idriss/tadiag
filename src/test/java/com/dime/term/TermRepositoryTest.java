package com.dime.term;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.TestTransaction;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TermRepositoryTest {

    @Inject
    TermRepository termRepository;

    @Test
    @TestTransaction
    void testFindByWord() {
        Term term = new Term();
        term.setWord("example");
        termRepository.persist(term);

        Term found = termRepository.findByWord("example");

        assertNotNull(found);
        assertEquals("example", found.getWord());
    }

    @Test
    @TestTransaction
    void testDeleteByWord() {
        Term term = new Term();
        term.setWord("example");
        termRepository.persist(term);

        long deletedCount = termRepository.deleteByWord("example");

        assertEquals(1, deletedCount);

        Term found = termRepository.findByWord("example");
        assertNull(found);
    }

    @Test
    @TestTransaction
    void testListAll() {
        Term term1 = new Term();
        term1.setWord("example1");
        Term term2 = new Term();
        term2.setWord("example2");

        termRepository.persist(term1);
        termRepository.persist(term2);

        List<Term> terms = termRepository.listAll();

        assertEquals(2, terms.size());
        assertTrue(terms.stream().anyMatch(t -> "example1".equals(t.getWord())));
        assertTrue(terms.stream().anyMatch(t -> "example2".equals(t.getWord())));
    }
}

package com.dime.term;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class TermMapperTest {

    @Inject
    private TermMapper termMapper;

    @Test
    void testToRecord() {
        Term term = new Term();
        term.setId(1L);
        term.setWord("word");
        term.setSynonyms(List.of("synonym1", "synonym2"));

        TermRecord termRecord = termMapper.toRecord(term);

        assertEquals(term.getId(), termRecord.getId());
        assertEquals(term.getWord(), termRecord.getWord());
        assertEquals(term.getSynonyms(), termRecord.getSynonyms());
    }

    @Test
    void testToEntity() {
        TermRecord termRecord = new TermRecord();
        termRecord.setId(1L);
        termRecord.setWord("word");
        termRecord.setSynonyms(List.of("synonym1", "synonym2"));

        Term term = termMapper.toEntity(termRecord);

        assertEquals(termRecord.getId(), term.getId());
        assertEquals(termRecord.getWord(), term.getWord());
        assertEquals(termRecord.getSynonyms(), term.getSynonyms());
    }

}

package com.dime.term;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dime.exceptions.GenericError;
import com.dime.exceptions.GenericException;
import com.dime.extensions.wordsapi.WordsApiService;

import jakarta.inject.Inject;

@ExtendWith(MockitoExtension.class)
class TermServiceTest {

    @RestClient
    @Inject
    @Mock
    private WordsApiService wordsApiService;

    @Mock
    private TermRepository termRepository;

    @InjectMocks
    private TermService termService;

    private Term term;

    @BeforeEach
    public void setup() {
        term = new Term();
        term.setWord("example");
        List<String> synonyms = List.of("synonym1", "synonym2");
        term.setSynonyms(synonyms);
    }

    @Test
    void testListAll() {
        List<Term> terms = List.of(term);
        when(termRepository.listAll()).thenReturn(terms);

        List<Term> result = termService.listAll();

        assertEquals(terms, result);
        verify(termRepository, times(1)).listAll();
    }

    @Test
    void testDeleteByWord() {
        when(termRepository.deleteByWord("example")).thenReturn(1L);

        Optional<Boolean> result = termService.deleteByWord("example");

        assertTrue(result.isPresent());
        assertTrue(result.get());
        verify(termRepository, times(1)).deleteByWord("example");
    }

    @Test
    void testDeleteByWord_TermNotInDatabase() {
        when(termRepository.deleteByWord("example")).thenReturn(0L);

        Optional<Boolean> result = termService.deleteByWord("example");

        assertTrue(result.isPresent());
        assertFalse(result.get());
        verify(termRepository, times(1)).deleteByWord("example");
    }

    @Test
    void testGetTerm_TermInDatabase() {
        when(termRepository.findByWord("example")).thenReturn(term);

        Optional<Term> result = termService.getTerm("example");

        assertTrue(result.isPresent());
        assertEquals(term, result.get());
        verify(termRepository, times(1)).findByWord("example");
        verify(termRepository, times(0)).persist(term);
        verifyNoInteractions(wordsApiService);
    }

    @Test
    void testGetTerm_TermNotInDatabaseButInWordsApiService() {
        // The first two calls to findByWord return null, the third one returns the term
        when(termRepository.findByWord("example"))
                .thenReturn(null) // null in the database with exact word
                .thenReturn(null) // null in the database with word returned by the API (ie singular)
                .thenReturn(term);
        when(wordsApiService.findByWord("example")).thenReturn(term);

        Optional<Term> result = termService.getTerm("example");

        assertTrue(result.isPresent());
        assertEquals(term, result.get());
        verify(termRepository, times(3)).findByWord("example");
        verify(wordsApiService, times(1)).findByWord("example");
        verify(termRepository, times(1)).persist(term);
    }

    @Test
    void testGetTerm_TermNotInDatabaseButinDatabaseWithTheWordsApiReturnWord() {
        // The first two calls to findByWord return null, the third one returns the term
        when(termRepository.findByWord("example"))
                .thenReturn(null) // null in the database with exact word
                .thenReturn(term) // present in the database with word returned by the API (ie singular)
                .thenReturn(term);
        when(wordsApiService.findByWord("example")).thenReturn(term);

        Optional<Term> result = termService.getTerm("example");

        assertTrue(result.isPresent());
        assertEquals(term, result.get());
        verify(termRepository, times(3)).findByWord("example");
        verify(wordsApiService, times(1)).findByWord("example");
        verify(termRepository, times(0)).persist(term);
    }

    @Test
    void testGetTerm_TermNotFoundAnywhere() {
        when(termRepository.findByWord("example")).thenReturn(null);
        when(wordsApiService.findByWord("example")).thenReturn(null);

        Optional<Term> result = termService.getTerm("example");

        assertFalse(result.isPresent());
        verify(termRepository, times(1)).findByWord("example");
        verify(wordsApiService, times(1)).findByWord("example");
        verify(termRepository, times(0)).persist(term);
    }

    @Test
    void testGetTerm_WordsApiServiceException() {
        when(termRepository.findByWord("example")).thenReturn(null);
        when(wordsApiService.findByWord("example"))
                .thenThrow(GenericError.FAILED_DEPENDENCY.exWithArguments(Map.of("code", 500)));
        try {
            termService.getTerm("example");
        } catch (Exception e) {
            assertEquals("Failed during using dependency, [code : 500]", e.getMessage());
            assertTrue(e instanceof GenericException);
        }

        verify(termRepository, times(1)).findByWord("example");
        verify(wordsApiService, times(1)).findByWord("example");
        verify(termRepository, times(0)).persist(term);
    }
}

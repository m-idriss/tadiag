package com.dime.term;

import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.dime.extensions.wordsapi.WordsApiService;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TermService {

    @Inject
    @RestClient
    private WordsApiService wordsApiService;

    @Inject
    private TermRepository termRepository;

    /*
     * This method returns a list of all terms persisted in the database
     */
    public List<Term> listAll() {
        return termRepository.listAll();
    }

    /*
     * This method deletes a term from the database by its word
     */
    public Optional<Boolean> deleteByWord(String word) {
        return Optional.of(termRepository.deleteByWord(word) > 0);
    }

    /*
     * This method returns a term by its id
     */

    public Optional<Term> findById(Long id) {
        return Optional.ofNullable(termRepository.findById(id));
    }

    /*
     * This method is annotated with @Transactional to ensure that the transaction
     * is open during the entire method execution. This is necessary because the
     * method calls two different repositories. The method first checks if the term
     * is already persisted in the database. If it is not, it calls the
     * extensionsService to get the term and then persists it in the database.
     * Finally, it returns the term.
     */

    @Transactional
    public Optional<Term> getTerm(String word) {
        Term term = null;
        term = termRepository.findByWord(word);
        if (term != null) {
            return Optional.of(term);
        }
        term = wordsApiService.findByWord(word);
        if (term == null) {
            Log.warn("Error Term not found in wordsApiService : [" + word + "]");
            return Optional.ofNullable(term);
        }
        if (termRepository.findByWord(term.getWord()) == null) {
            Log.warn(
                    "Error Term not found in database, persisting term : [" + term.getWord() + "] for word : [" + word
                            + "]");
            termRepository.persist(term);
        }
        return Optional.of(termRepository.findByWord(term.getWord()));
    }

}

package com.dime.term;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TermRepository implements PanacheRepository<Term> {

    /*
     * This method returns a term persisted in the database by its word.
     */
    public Term findByWord(String word) {
        return find("word", word).firstResult();
    }

    /*
     * This method deletes a term persisted in the database by its word.
     */
    public long deleteByWord(String word) {
        return delete("word", word);
    }
}
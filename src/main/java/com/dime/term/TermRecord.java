package com.dime.term;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TermRecord {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("word")
    private String word;

    @JsonProperty("synonyms")
    private List<String> synonyms;

    public String getWord() {
        return word;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    @Override
    public String toString() {
        return "TermRecord [id=" + id + ", word=" + word + ", synonyms=" + synonyms + "]";
    }
}
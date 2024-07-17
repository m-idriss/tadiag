package com.dime.extensions.wordsapi;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.dime.term.Term;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/words")
@RegisterRestClient(configKey = "wordsapi")
public interface WordsApiService {

    @GET
    @Path("{word}/synonyms")
    @Produces(MediaType.APPLICATION_JSON)
    Term findByWord(@PathParam("word") String word);

    // implement health check for api @Path("words/health")
    @GET
    @Path("/health/synonyms")
    @Produces(MediaType.APPLICATION_JSON)
    String healthApi();

}
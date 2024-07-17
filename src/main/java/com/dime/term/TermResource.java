package com.dime.term;

import java.util.Map;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.dime.exceptions.GenericError;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/terms")
@Produces("application/json")
@Tag(name = "Terms", description = "Manage Terms")
public class TermResource {

    @Inject
    private TermService termService;

    @Inject
    private TermMapper termMapper;

    /*
     * curl -X GET http://localhost:8080/api/v1/terms/word
     */
    @GET
    @Path("/{word}")
    @Operation(description = "Get term by word")
    @Transactional
    public Response getTerm(String word) {
        String wordLower = word.toLowerCase();
        return termService.getTerm(wordLower)
                .map(termMapper::toRecord)
                .map(Response::ok)
                .orElseThrow(() -> GenericError.WORD_NOT_FOUND.exWithArguments(Map.of("word", wordLower)))
                .build();
    }

    /*
     * curl -X GET http://localhost:8080/api/v1/terms
     */
    @GET
    @Operation(description = "List all terms")
    public Response getTerms() {
        return Response.ok(termService.listAll().stream()
                .map(termMapper::toRecord)
                .toList())
                .build();
    }

    /*
     * curl -X DELETE http://localhost:8080/api/v1/terms/word
     */
    @DELETE
    @Path("/{word}")
    @Transactional
    @Operation(description = "Delete term by word")
    public Response deleteTerm(String word) {
        String wordLower = word.toLowerCase();
        return termService.deleteByWord(wordLower)
                .map(deleted -> Response.ok().build())
                .orElseThrow(() -> GenericError.WORD_NOT_FOUND.exWithArguments(Map.of("word", wordLower)));
    }

}

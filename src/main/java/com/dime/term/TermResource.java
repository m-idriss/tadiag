package com.dime.term;

import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import com.dime.exceptions.GenericError;

import io.quarkus.hal.HalCollectionWrapper;
import io.quarkus.hal.HalEntityWrapper;
import io.quarkus.hal.HalService;
import io.quarkus.resteasy.reactive.links.InjectRestLinks;
import io.quarkus.resteasy.reactive.links.RestLink;
import io.quarkus.resteasy.reactive.links.RestLinkType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/terms")
@Tag(name = "Terms", description = "Manage Terms")
public class TermResource {

    @Inject
    private TermService termService;

    @Inject
    private TermMapper termMapper;

    @Inject
    HalService halService;

    /*
     * curl -X GET http://localhost:8080/api/v1/terms/word
     */
    @GET
    @Path("{word: [a-zA-Z]+}")
    @RestLink(rel = "self-by-word")
    @Produces({ MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON })
    @InjectRestLinks(RestLinkType.INSTANCE)
    @Operation(summary = "Get term by word")
    public HalEntityWrapper<TermRecord> getTermByWord(@PathParam("word") String word) {
        String wordLower = word.toLowerCase();
        Term entity = termService.getTerm(wordLower).get();
        HalEntityWrapper<TermRecord> halEntity = halService.toHalWrapper(termMapper.toRecord(entity));
        return halEntity;
    }

    @GET
    @Path("/{id: \\d+}")
    @RestLink(rel = "self")
    @Produces({ MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON })
    @InjectRestLinks(RestLinkType.INSTANCE)
    @Operation(summary = "Get term by id")
    public HalEntityWrapper<TermRecord> getTermById(@PathParam("id") int id) {
        Term entity = termService.findById(Long.valueOf(id))
                .orElseThrow(() -> GenericError.TERM_NOT_FOUND.exWithArguments(Map.of("id", id)));
        HalEntityWrapper<TermRecord> halEntity = halService.toHalWrapper(termMapper.toRecord(entity));
        return halEntity;
    }

    /*
     * curl -X GET http://localhost:8080/api/v1/terms
     */
    @GET
    @RestLink(rel = "list")
    @Produces({ MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON })
    @Operation(summary = "List all terms")
    public HalCollectionWrapper<TermRecord> listAllTerms() {
        List<Term> terms = termService.listAll();
        List<TermRecord> termRecords = terms.stream().map(termMapper::toRecord).toList();
        return halService.toHalCollectionWrapper(termRecords, "terms", TermRecord.class);
    }

    /*
     * curl -X DELETE http://localhost:8080/api/v1/terms/word
     */
    @DELETE
    @Path("/{word}")
    @Transactional
    @Operation(summary = "Delete term by word")
    public Response deleteTerm(String word) {
        String wordLower = word.toLowerCase();
        return termService.deleteByWord(wordLower)
                .map(deleted -> Response.noContent().build()).get();
    }

}

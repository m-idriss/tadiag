package com.dime.term;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class TermResourceTest {
    @Test
    void testGetbyWordEndpoint() {
        given()
                .when().get("api/v1/terms/health")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("word", equalTo("health"))
                .body("synonyms", hasSize(1))
                .body("synonyms", hasItem("wellness"))
                .body("_links", allOf(
                        hasKey("self-by-word"),
                        hasKey("self"),
                        hasKey("list")))
                .body("_links.self-by-word.href", matchesPattern("http://[^/]+/api/v1/terms/health"))
                .body("_links.self.href", matchesPattern("http://[^/]+/api/v1/terms/1"))
                .body("_links.list.href", matchesPattern("http://[^/]+/api/v1/terms"));

    }

    @Test
    void testEmptyListAllEndpoint() {
        given()
                .when().get("api/v1/terms")
                .then()
                .statusCode(200)
                .body("_embedded.terms", empty())
                .body("_links.list.href", matchesPattern("http://[^/]+/api/v1/terms"));

    }

}
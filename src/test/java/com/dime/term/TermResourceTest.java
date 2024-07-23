package com.dime.term;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TermResourceTest {

    @Test
    public void testGetTermByWord() {
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
    public void testListAllTermsWithTwoTerms() {
        given()
                .when().get("api/v1/terms/health").then().statusCode(200);
        given()
                .when().get("api/v1/terms/TRY").then().statusCode(200);

        given()
                .when().get("api/v1/terms/Test").then().statusCode(200);

        given()
                .when().get("api/v1/terms")
                .then()
                .statusCode(200)
                .body("_embedded", hasKey("terms"))
                .body("_embedded.terms", hasSize(3))
                .body("_embedded.terms[0].id", equalTo(1))
                .body("_embedded.terms[0].word", equalTo("health"))
                .body("_embedded.terms[0].synonyms", hasSize(1))
                .body("_embedded.terms[0].synonyms", hasItem("wellness"))
                .body("_embedded.terms[1].id", equalTo(2))
                .body("_embedded.terms[1].word", equalTo("try"))
                .body("_embedded.terms[1].synonyms", hasSize(20))
                .body("_embedded.terms[1].synonyms", hasItem("test"))
                .body("_embedded.terms[2].id", equalTo(3))
                .body("_embedded.terms[2].word", equalTo("test"))
                .body("_embedded.terms[2].synonyms", hasSize(16))
                .body("_embedded.terms[2].synonyms", hasItem("run"))
                .body("_links.list.href", matchesPattern("http://[^/]+/api/v1/terms"));
    }
}

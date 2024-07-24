package com.dime.term;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class TermResourceTest {

  public static final String TERMS_ENDPOINT = "api/v1/terms";
  public static final String DOMAIN = "http://[^/]+/";

  @Test
  public void testGetTermById() {

    // call with word
    int healthId = given().when().get(TERMS_ENDPOINT + "/health").then().statusCode(200).extract().path("id");

    // repeat call with word
    int healthIdStateless = given().when().get(TERMS_ENDPOINT + "/health").then().statusCode(200).extract().path("id");

    assertEquals(healthId, healthIdStateless);

    // call by id and check response
    given()
        .when().get(TERMS_ENDPOINT + "/" + healthId)
        .then()
        .statusCode(200)
        .body("id", equalTo(healthId))
        .body("word", equalTo("health"))
        .body("synonyms", hasSize(1))
        .body("synonyms", hasItem("wellness"))
        .body("_links", allOf(
            hasKey("self-by-word"),
            hasKey("self"),
            hasKey("list")))
        .body("_links.self-by-word.href", matchesPattern(DOMAIN + TERMS_ENDPOINT + "/health"))
        .body("_links.self.href", matchesPattern(DOMAIN + TERMS_ENDPOINT + "/" + healthId))
        .body("_links.list.href", matchesPattern(DOMAIN + TERMS_ENDPOINT));
  }

  @Test
  public void testGetTermByWord() {

    int healthId = given()
        .when().get(TERMS_ENDPOINT + "/health")
        .then()
        .statusCode(200).extract().path("id");

    given()
        .when().get(TERMS_ENDPOINT + "/health")
        .then()
        .statusCode(200)
        .body("id", equalTo(healthId))
        .body("word", equalTo("health"))
        .body("synonyms", hasSize(1))
        .body("synonyms", hasItem("wellness"))
        .body("_links", allOf(
            hasKey("self-by-word"),
            hasKey("self"),
            hasKey("list")))
        .body("_links.self-by-word.href", matchesPattern(DOMAIN + TERMS_ENDPOINT + "/health"))
        .body("_links.self.href", matchesPattern(DOMAIN + TERMS_ENDPOINT + "/" + healthId))
        .body("_links.list.href", matchesPattern(DOMAIN + TERMS_ENDPOINT));
  }

  @Test
  public void testListAllTerms() {

    int healthId = given().when().get(TERMS_ENDPOINT + "/health").then().statusCode(200).extract().path("id");
    int tryId = given().when().get(TERMS_ENDPOINT + "/TRY").then().statusCode(200).extract().path("id");
    int testId = given().when().get(TERMS_ENDPOINT + "/Test").then().statusCode(200).extract().path("id");

    given()
        .when().get(TERMS_ENDPOINT)
        .then()
        .statusCode(200)
        .body("_embedded", hasKey("terms"))
        .body("_embedded.terms", hasSize(3))
        .body("_embedded.terms[0].id", equalTo(healthId))
        .body("_embedded.terms[0].word", equalTo("health"))
        .body("_embedded.terms[0].synonyms", hasSize(1))
        .body("_embedded.terms[0].synonyms", hasItem("wellness"))
        .body("_embedded.terms[0]._links.self-by-word.href", matchesPattern(DOMAIN + TERMS_ENDPOINT + "/health"))
        .body("_embedded.terms[0]._links.self.href", matchesPattern(DOMAIN + TERMS_ENDPOINT + "/" + healthId))
        .body("_embedded.terms[0]._links.list.href", matchesPattern(DOMAIN + TERMS_ENDPOINT))
        .body("_embedded.terms[1].id", equalTo(tryId))
        .body("_embedded.terms[1].word", equalTo("try"))
        .body("_embedded.terms[1].synonyms", hasSize(20))
        .body("_embedded.terms[1].synonyms", hasItem("test"))
        .body("_embedded.terms[1]._links.self-by-word.href", matchesPattern(DOMAIN + TERMS_ENDPOINT + "/try"))
        .body("_embedded.terms[1]._links.self.href", matchesPattern(DOMAIN + TERMS_ENDPOINT + "/" + tryId))
        .body("_embedded.terms[1]._links.list.href", matchesPattern(DOMAIN + TERMS_ENDPOINT))
        .body("_embedded.terms[2].id", equalTo(testId))
        .body("_embedded.terms[2].word", equalTo("test"))
        .body("_embedded.terms[2].synonyms", hasSize(16))
        .body("_embedded.terms[2].synonyms", hasItem("run"))
        .body("_embedded.terms[2]._links.self-by-word.href", matchesPattern(DOMAIN + TERMS_ENDPOINT + "/test"))
        .body("_embedded.terms[2]._links.self.href", matchesPattern(DOMAIN + TERMS_ENDPOINT + "/" + testId))
        .body("_embedded.terms[2]._links.list.href", matchesPattern(DOMAIN + TERMS_ENDPOINT))
        .body("_links.list.href", matchesPattern(DOMAIN + TERMS_ENDPOINT));
  }
}

package com.dime.term;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class TermResourceTest {
    @Test
    @Order(1)
    void testHelloEndpoint() {
        given()
                .when().get("api/v1/terms/health")
                .then()
                .statusCode(200)
                .body(containsString("\"id\":"))
                .body(containsString("\"word\":\"health\",\"synonyms\":[\"wellness\"]"));

    }

    @Test
    @Order(2)
    void testListAllEndpoint() {
        given()
                .when().get("api/v1/terms")
                .then()
                .statusCode(200)
                .body(containsString("\"id\":"))
                .body(containsString("\"word\":\"health\",\"synonyms\":[\"wellness\"]"));

    }

}
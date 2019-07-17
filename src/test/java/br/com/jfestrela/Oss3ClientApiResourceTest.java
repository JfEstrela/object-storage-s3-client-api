package br.com.jfestrela;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class Oss3ClientApiResourceTest {

    @Test
    public void Oss3ClientApiEndpointTest() {
        given()
          .when().get("/oss3-client-api/buckets")
          .then()
             .statusCode(200);
    }

}
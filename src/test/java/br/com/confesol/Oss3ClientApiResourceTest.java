package br.com.jfestrela;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class Oss3ClientApiResourceTest {

    @Test
    public void Oss3ClientApiEndpointTest() {
        given()
          .when().get("/oss3-client-api")
          .then()
             .statusCode(200)
             .body(is("Bem vindo ao Quarkus!"));
    }

}
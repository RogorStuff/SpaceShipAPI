package com.spaceshipapi.spaceshipapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import java.util.Objects;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = SpaceshipapiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpaceshipapiIntegrationTests {

    @LocalServerPort
    private int port;


    @BeforeEach
    void setUp() {
        String propertyPort = System.getProperty("server.port");
        RestAssured.port = Integer.parseInt(Objects.requireNonNullElseGet(propertyPort, () -> String.valueOf(port)));

        String baseHost = System.getProperty("server.host");
        if(baseHost==null){
            baseHost = "http://localhost";
        }
        RestAssured.baseURI = baseHost;

    }

    @Order(1)
    @Test
    void createShip() throws JSONException {

        JSONObject ship = new JSONObject();
        ship.put("id", "1");
        ship.put("name", "The classic");
        ship.put("firstAppearance", "Casablanca");
        ship.put("dateFirstAppearance", "38 BC");

        // GIVEN
        given()
                .contentType(ContentType.JSON)
                .body(ship.toString())

                // WHEN
                .when()
                .post("/api/spacecrafts")

                // THEN
                .then()
                .log().all()
                .body("ships[0].name", equalTo("The classic"))
                .statusCode(201)
                .log().all();

    }

    @Order(2)
    @ParameterizedTest(name="Id {0} can be found: {1}")
    @CsvSource({
            "1, true",
            "2, false"
    })
    void searchShip(int id, boolean canBeFound) throws JSONException {

        JSONObject shipToSearch = new JSONObject();
        shipToSearch.put("id", id);
        shipToSearch.put("name", "The classic");
        shipToSearch.put("firstAppearance", "Casablanca");
        shipToSearch.put("dateFirstAppearance", "38 BC");

        if (canBeFound){
            // GIVEN
            given()
                    .contentType(ContentType.JSON)
                    .body(shipToSearch.toString())

                    // WHEN
                    .when()
                    .get("/api/spacecrafts/"+id)

                    // THEN
                    .then()
                    .statusCode(200)
                    .body("ships[0].name", equalTo("The classic"))
                    .log().all();
        } else {
            // GIVEN
            given()
                    .contentType(ContentType.JSON)
                    .body(shipToSearch.toString())

                    // WHEN
                    .when()
                    .get("/api/spacecrafts/"+id)

                    // THEN
                    .then()
                    .statusCode(404)
                    .log().all();
        }
    }

    @Order(3)
    @ParameterizedTest(name="Id {0} can be updated: {1}")
    @CsvSource({
            "1, true",
            "2, false"
    })
    void UpdateShip(int id, boolean canBeFound) throws JSONException {

        JSONObject shipToSearch = new JSONObject();
        shipToSearch.put("id", id);
        shipToSearch.put("name", "The classic");
        shipToSearch.put("firstAppearance", "Casablanca 2");
        shipToSearch.put("dateFirstAppearance", "38 BC");

        if (canBeFound){
            // GIVEN
            given()
                    .contentType(ContentType.JSON)
                    .body(shipToSearch.toString())

                    // WHEN
                    .when()
                    .put("/api/spacecrafts")

                    // THEN
                    .then()
                    .statusCode(200)
                    .body("responseText", equalTo("Ship with Id 1 updated"))
                    .log().all();
        } else {
            // GIVEN
            given()
                    .contentType(ContentType.JSON)
                    .body(shipToSearch.toString())

                    // WHEN
                    .when()
                    .put("/api/spacecrafts")

                    // THEN
                    .then()
                    .statusCode(404)
                    .log().all();
        }

    }

    @Order(4)
    @ParameterizedTest(name="Id {0} can be deleted: {1}")
    @CsvSource({
            "1, true",
            "2, false"
    })
    void DeleteShip(int id, boolean canBeFound) throws JSONException {

        if (canBeFound){
            // GIVEN
            given()
                    .contentType(ContentType.JSON)

                    // WHEN
                    .when()
                    .delete("/api/spacecrafts/"+id)

                    // THEN
                    .then()
                    .statusCode(204)
                    .log().all();
        } else {
            // GIVEN
            given()
                    .contentType(ContentType.JSON)

                    // WHEN
                    .when()
                    .delete("/api/spacecrafts/"+id)

                    // THEN
                    .then()
                    .statusCode(404)
                    .log().all();
        }
    }

}

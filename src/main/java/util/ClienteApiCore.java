package util;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Cliente HTTP mínimo hacia /api/core/*, usado por la capa de Lógica de Negocio
 * para preparar datos de prueba (registro, eventos, reservas) y para contrastar
 * lo mostrado en pantalla contra la respuesta real de la API (consistencia
 * Front/Back, RSK-17 — ver TC-WEB-01/02 en R3).
 *
 * Librería Base (CTAL-TAE v2.0): solo transporte HTTP, sin lógica de negocio ni
 * aserciones — esas viven en questions/.
 */
public class ClienteApiCore {

    private static final String BASE_URL = "https://testathon.testingperu.com/api/core";

    private ClienteApiCore() { }

    public static Response get(String path) {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .accept("application/json")
                .when()
                .get(path)
                .then()
                .extract().response();
    }

    public static Response post(String path, Object body, String tokenUsuario) {
        RequestSpecification request = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .accept("application/json");
        if (tokenUsuario != null && !tokenUsuario.isBlank()) {
            request = request.header("Authorization", "Bearer " + tokenUsuario);
        }
        return request.body(body)
                .when()
                .post(path)
                .then()
                .extract().response();
    }

    public static Response disponibilidad(String eventoId) {
        return get("/eventos/" + eventoId + "/disponibilidad");
    }
}

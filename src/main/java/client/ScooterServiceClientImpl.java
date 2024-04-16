package client;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.given;

public class ScooterServiceClientImpl implements ScooterServiceClient {

    private static final String CREATE_USER_ENDPOINT = "/api/v1/courier";
    private static final String LOGIN_ENDPOINT = "/api/v1/courier/login";
    private static final String DELETE_COURIER_ENDPOINT = "/api/v1/courier/:id";
    private static final String GET_ORDERS_ENDPOINT = "/api/v1/orders";
    public final RequestSpecification requestSpecification;
    public final ResponseSpecification responseSpecification;

    public ScooterServiceClientImpl(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        this.requestSpecification = requestSpecification;
        this.responseSpecification = responseSpecification;
    }

    @Override
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .spec(requestSpecification)
                .body(courier)
                .post(CREATE_USER_ENDPOINT)
                .then()
                .spec(responseSpecification);

    }

    @Override
    public ValidatableResponse login(Credentials credentials) {

        return given()
                .spec(requestSpecification)
                .body(credentials)
                .post(LOGIN_ENDPOINT)
                .then()
                .spec(responseSpecification);
    }
    @Override
    public ValidatableResponse deleteCourierById(String id) {
        return given()
                .spec(requestSpecification)
                .delete(DELETE_COURIER_ENDPOINT + id)
                .then()
                .spec(responseSpecification);
    }

    @Override
    public ValidatableResponse createOrder(OrderData orderData) {
        return given()
                .spec(requestSpecification)
                .body(orderData)
                .post("/api/v1/orders")
                .then()
                .spec(responseSpecification);
    }
    @Override
    public Response getListOfOrders() {
        return RestAssured.given()
                .spec(requestSpecification)
                .get("/api/v1/orders")
                .then()
                .spec(responseSpecification)
                .extract()
                .response();
    }
}
package client;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public interface ScooterServiceClient {
    ValidatableResponse createCourier(Courier courier);
    ValidatableResponse login(Credentials credentials);
    ValidatableResponse deleteCourierById(String id);
    ValidatableResponse createOrder(OrderData orderData);
    Response getListOfOrders();
}

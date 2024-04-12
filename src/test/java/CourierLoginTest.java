import client.Courier;
import client.Credentials;
import client.ScooterServiceClient;
import client.ScooterServiceClientImpl;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;

public class CourierLoginTest {
    private static final RequestSpecification REQUEST_SPECIFICATION =
            new RequestSpecBuilder()
                    .log(LogDetail.ALL)
                    .addHeader("Content-type", "application/json")
                    .setBaseUri("http://qa-scooter.praktikum-services.ru/")
                    .build();
    private static final ResponseSpecification RESPONSE_SPECIFICATION =
            new ResponseSpecBuilder().log(LogDetail.ALL).build();
    private ScooterServiceClient client;
    private String createdCourierId;

    @Before
    public void setUp() {
        client = new ScooterServiceClientImpl(REQUEST_SPECIFICATION, RESPONSE_SPECIFICATION);
    }

    @After
    public void cleanUp() {
        if (createdCourierId != null) {
            client.deleteCourierById(createdCourierId);
        }
    }
    @Test
    public void loginCourier_success() {
        Courier courier = Courier.create("yruuifrfr", "1234", "Dasha");
        client.createCourier(courier);
        Credentials credentials = Credentials.fromCourier(courier);
        ValidatableResponse response = client.login(credentials);
        String courierId = response.extract().jsonPath().getString("id");

        response.assertThat().statusCode(200);
        assertThat(courierId, not(isEmptyString()));
    }

    @Test
    public void loginCourier_incorrectCredentials_returnsError() {
        Credentials credentials = new Credentials("incorrect_login", "incorrect_password");
        ValidatableResponse response = client.login(credentials);
        response.assertThat().statusCode(404);
    }

    @Test
    public void loginCourier_missingFields_returnsError() {
        Credentials credentials = new Credentials("yruuifrfr", "");
        ValidatableResponse response = client.login(credentials);
        response.assertThat().statusCode(400);
    }

    @Test
    public void loginCourier_nonExistingCourier_returnsError() {
        Credentials credentials = new Credentials("non_existing_login", "1234");
        ValidatableResponse response = client.login(credentials);
        response.assertThat().statusCode(404);
    }
}
import client.Courier;
import client.ScooterServiceClient;
import client.ScooterServiceClientImpl;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;


public class CourierTest {
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
    public void createCourier_success() {

        Courier courier = Courier.create("Dasha", "1234", "Dasha");
        ValidatableResponse response = client.createCourier(courier);
        createdCourierId = response.extract().jsonPath().getString("id");
        response.assertThat().statusCode(201).and().body("ok", equalTo(true));

    }
    @Test
    public void createCourier_duplicateLogin_returnsError() {
        Courier courier = Courier.create("yruifrfr", "1234", "Dasha");
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().statusCode(409);
    }
    @Test
    public void createCourier_missingField_returnsError() {
        Courier courier = Courier.create("yruifrfr", null, "Dasha");
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().statusCode(400);
    }
    @Test
    public void createCourier_existingLogin_returnsError() {
        Courier existingCourier = Courier.create("existing_login", "password", "Existing");
        client.createCourier(existingCourier);

        Courier duplicateCourier = Courier.create("existing_login", "password2", "Duplicate");
        ValidatableResponse response = client.createCourier(duplicateCourier);
        response.assertThat().statusCode(409);
    }

}
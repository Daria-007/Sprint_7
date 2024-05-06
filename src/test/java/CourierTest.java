import client.Courier;
import client.ScooterServiceClient;
import client.ScooterServiceClientImpl;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;


public class CourierTest extends BaseTest  {
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

    @Step("Create courier successfully")
    @Test
    public void createCourierSuccess() {
        Faker faker = new Faker();

        String username = faker.name().username();
        String password = faker.internet().password();
        String fullName = faker.name().fullName();

        Courier courier = Courier.create(username, password, fullName);
        ValidatableResponse response = client.createCourier(courier);
        createdCourierId = response.extract().jsonPath().getString("id");
        response.assertThat().statusCode(201).and().body("ok", equalTo(true));
    }

    @Step("Create courier with duplicate login")
    @Test
    public void createCourierDuplicateLoginReturnsError() {
        Faker faker = new Faker();

        String username = faker.name().username();
        String password = faker.internet().password();
        String fullName = faker.name().fullName();

        Courier courier = Courier.create(username, password, fullName);
        client.createCourier(courier);

        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().statusCode(409);
    }

    @Step("Create courier with missing field")
    @Test
    public void createCourierMissingFieldReturnsError() {
        Faker faker = new Faker();

        String username = faker.name().username();
        String fullName = faker.name().fullName();

        Courier courier = Courier.create(username, null, fullName);
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().statusCode(400);
    }

    @Step("Create courier with existing login")
    @Test
    public void createCourierExistingLoginReturnsError() {
        Faker faker = new Faker();

        String username = faker.name().username();
        String password = faker.internet().password();
        String fullName = faker.name().fullName();

        Courier existingCourier = Courier.create(username, password, fullName);
        client.createCourier(existingCourier);

        Courier duplicateCourier = Courier.create(username, "password2", "Duplicate");
        ValidatableResponse response = client.createCourier(duplicateCourier);
        response.assertThat().statusCode(409);
    }
}
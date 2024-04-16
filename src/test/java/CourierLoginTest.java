import client.Courier;
import client.Credentials;
import client.ScooterServiceClient;
import client.ScooterServiceClientImpl;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.github.javafaker.Faker;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;

public class CourierLoginTest extends BaseTest  {
    private ScooterServiceClient client;
    private String createdCourierId;
    private final Faker faker = new Faker();

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

    @Step("Login courier successfully")
    @Test
    public void loginCourierSuccess() {
        Courier courier = Courier.create(faker.name().username(), "1234", faker.name().fullName());
        client.createCourier(courier);
        Credentials credentials = Credentials.fromCourier(courier);
        ValidatableResponse response = client.login(credentials);
        String courierId = response.extract().jsonPath().getString("id");

        response.assertThat().statusCode(200);
        assertThat(courierId, not(isEmptyString()));
    }

    @Step("Login courier with incorrect credentials")
    @Test
    public void loginCourierIncorrectCredentialsReturnsError() {
        Credentials credentials = new Credentials(faker.name().username(), faker.internet().password());
        ValidatableResponse response = client.login(credentials);
        response.assertThat().statusCode(404);
    }

    @Step("Login courier with missing fields")
    @Test
    public void loginCourierMissingFieldsReturnsError() {
        Credentials credentials = new Credentials(faker.name().username(), "");
        ValidatableResponse response = client.login(credentials);
        response.assertThat().statusCode(400);
    }

    @Step("Login non-existing courier")
    @Test
    public void loginCourierNonExistingCourierReturnsError() {
        Credentials credentials = new Credentials(faker.name().username(), "1234");
        ValidatableResponse response = client.login(credentials);
        response.assertThat().statusCode(404);
    }
}
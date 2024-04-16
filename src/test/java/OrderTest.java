import client.OrderData;
import client.ScooterServiceClient;
import client.ScooterServiceClientImpl;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest extends BaseTest  {
    private ScooterServiceClient client;
    private String color;

    public OrderTest(String color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        client = new ScooterServiceClientImpl(REQUEST_SPECIFICATION, RESPONSE_SPECIFICATION);
    }
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "BLACK" },
                { "GREY" },
                { "BLACK,GREY" },
                { "" }
        });
    }

    @Step("Create order")
    @Test
    public void testOrderCreation() {
        OrderData orderData = new OrderData("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", color.isEmpty() ? new String[]{} : color.split(","));

        ValidatableResponse response = client.createOrder(orderData);
        response.statusCode(201);
        assertTrue(response.extract().body().asString().contains("track"));
    }
}
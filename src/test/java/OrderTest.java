import client.OrderData;
import client.ScooterServiceClient;
import client.ScooterServiceClientImpl;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest {
    private static final RequestSpecification REQUEST_SPECIFICATION =
            new RequestSpecBuilder()
                    .log(LogDetail.ALL)
                    .addHeader("Content-type", "application/json")
                    .setBaseUri("http://qa-scooter.praktikum-services.ru/")
                    .build();
    private static final ResponseSpecification RESPONSE_SPECIFICATION =
            new ResponseSpecBuilder().log(LogDetail.ALL).build();

    private ScooterServiceClient client;
    private OrderData orderData;

    public OrderTest(OrderData orderData) {
        this.orderData = orderData;
    }

    @Before
    public void setUp() {
        client = new ScooterServiceClientImpl(REQUEST_SPECIFICATION, RESPONSE_SPECIFICATION);
    }
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { new OrderData("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String[]{"BLACK"}) },
                { new OrderData("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String[]{"GREY"}) },
                { new OrderData("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String[]{"BLACK", "GREY"}) },
                { new OrderData("Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String[]{}) }
        });
    }

    @Test
    public void testOrderCreation() {
        ValidatableResponse response = client.createOrder(orderData);
        response.statusCode(201);
        assertTrue(response.extract().body().asString().contains("track"));
    }
    @Test
    public void testOrderList() {
        Response response = client.getOrders();
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.statusCode(200);
        assertTrue(response.getBody().asString().contains("orders"));
    }
}
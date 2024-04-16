import client.ScooterServiceClient;
import client.ScooterServiceClientImpl;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;

public class OrderListTest extends BaseTest {

    private ScooterServiceClient client;

    @Before
    public void setUp() {
        client = new ScooterServiceClientImpl(REQUEST_SPECIFICATION, RESPONSE_SPECIFICATION);
    }

    @Step("Waiting for {numberOfOrders} orders to be created")
    public List<Integer> waitForOrdersToBeCreated(int numberOfOrders) {
        List<Integer> createdOrderIds = new ArrayList<>();

        for (int i = 0; i < numberOfOrders; i++) {
        }
        return createdOrderIds;
    }

    @Step("Getting list of orders")
    public List<Integer> getListOfOrders() {
        Response response = client.getListOfOrders();
        return response.jsonPath().getList("track");
    }

    @Step("Get Order List")
    @Test
    public void testOrderList() {
        List<Integer> createdOrderIds = waitForOrdersToBeCreated(3);

        List<Integer> orderIds = getListOfOrders();

        for (Integer orderId : createdOrderIds) {
            assertTrue("Order " + orderId + " not found in the list", orderIds.contains(orderId));
        }
    }
}
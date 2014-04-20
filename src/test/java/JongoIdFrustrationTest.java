import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.fest.assertions.Assertions.assertThat;

public class JongoIdFrustrationTest {
    private MongoCollection orders;

    @Before
    public void init() throws UnknownHostException {
        Jongo jongo = new Jongo(new MongoClient().getDB("unitest"));
        orders = jongo.getCollection("order");
        orders.drop();
    }

    @Test
    public void should_update_product() {
        // First I save a order (this should be an "insert")
        Order order = new Order();
        order.setBuyer("foo");
        order.setPrice(42d);
        orders.save(order);
        String id = order.getId();
        // I check there is an id
        assertThat(order.getId()).isNotNull();
        // I change something in the order
        order.setBuyer("bar");
        // Second I save the same order
        orders.save(order);
        assertThat(order.getId()).isEqualTo(id);
        assertThat(order.getBuyer()).isEqualTo("bar");
        // this should fails with The _id field cannot be changed from {_id: ObjectId('535383a630049dea914bd068')} to {_id: \"535383a630049dea914bd068\"}
    }

}

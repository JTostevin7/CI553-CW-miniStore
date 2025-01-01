package catalogue;
import catalogue.Basket;
import catalogue.Product;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Basket class.
 */
public class BasketTest {

    private Basket basket;

    @Before
    public void setUp() {
        basket = new Basket(); //initialise basket before each test case
        
    }

    @Test
    public void testSetOrderNum() {
        basket.setOrderNum(123); 
        assertEquals(123, basket.getOrderNum()); //verifies the order number
    }

    @Test
    public void testAddProduct() {
        Product product = new Product("0001", "Test Product", 10.0, 2);
        basket.add(product);
        assertEquals(1, basket.size()); //check basket size
    }

    @Test
    public void testRemoveProduct() {
        Product product = new Product("0001", "Test Product", 10.0, 2);
        basket.add(product);
        basket.remove(product);
        assertEquals(0, basket.size()); //ensures the basket is empty
    }

    @Test
    public void testApplyDiscount() {
        Product product = new Product("0001", "Discount Product", 100.0, 1);
        basket.add(product);
        basket.applyDiscount(0.10); // Apply 10% discount
        assertEquals(90.0, product.getPrice(), 0.01);
    }

    @Test
    public void testGetDetails() {
        basket.setOrderNum(123);
        Product product = new Product("P001", "Test Product", 10.0, 2);
        basket.add(product);
        String details = basket.getDetails();
        assertTrue(details.contains("Order number: 123"));
        assertTrue(details.contains("Test Product"));
    }
}

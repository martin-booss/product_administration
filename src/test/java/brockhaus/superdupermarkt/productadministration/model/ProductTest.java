package brockhaus.superdupermarkt.productadministration.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
    }

    @Test
    void getTotalPrice() {
        product.setBasePrice(2.00);
        product.setQuality(10);
        assertEquals(3, product.getTotalPrice());
    }

    @Test
    void testIsNotExpired() {
        product.setDaysUntilExpiration(0);
        assertFalse(product.isExpired());
    }

    @Test
    void testIsExpired() {
        product.setDaysUntilExpiration(-1);
        assertTrue(product.isExpired());
    }

    @Test
    void hasToBeRemoved() {
        product.setDaysUntilExpiration(0);
        assertFalse(product.hasToBeRemoved());
    }

    @Test
    void getProductInNDays() {
        product = Product.builder()
                .name("Apfel")
                .quality(10)
                .daysUntilExpiration(2)
                .basePrice(0.50)
                .build();
        Product newProduct = product.getProductInNDays(1);

        assertEquals(product.getName(), newProduct.getName());
        assertEquals(product.getQuality(), newProduct.getQuality());
        assertEquals(1, newProduct.getDaysUntilExpiration());
        assertEquals(product.getBasePrice(), newProduct.getBasePrice());
    }
}
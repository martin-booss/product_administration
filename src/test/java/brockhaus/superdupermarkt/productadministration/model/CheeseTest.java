package brockhaus.superdupermarkt.productadministration.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheeseTest {

    private Cheese cheese;

    @BeforeEach
    void setUp() {
        cheese = new Cheese();
    }

    @Test
    void testHasToBeRemovedWithGoodQuality() {
        cheese.setQuality(30);
        assertFalse(cheese.hasToBeRemoved());
    }

    @Test
    void testHasToBeRemovedWithLowQuality() {
        cheese.setQuality(29);
        assertTrue(cheese.hasToBeRemoved());
    }

    @Test
    void getTotalPrice() {
        cheese.setQuality(30);
        cheese.setBasePrice(1);
        double expected = 5.50;
        assertEquals(expected, cheese.getTotalPrice());
    }

    @Test
    void getProductInNDays() {
        cheese = Cheese.builder()
                .name("Mozarella")
                .quality(30)
                .daysUntilExpiration(10)
                .basePrice(1)
                .build();
        Cheese newCheese = cheese.getProductInNDays(1);

        assertEquals(cheese.getName(), newCheese.getName());
        assertEquals(29, newCheese.getQuality());
        assertEquals(9, newCheese.getDaysUntilExpiration());
        assertEquals(cheese.getBasePrice(), newCheese.getBasePrice());
    }

}
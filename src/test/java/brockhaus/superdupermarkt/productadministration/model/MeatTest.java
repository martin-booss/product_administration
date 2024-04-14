package brockhaus.superdupermarkt.productadministration.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MeatTest {

    private Meat meat;

    @BeforeEach
    void setUp() {
        meat = new Meat();
    }

    @Test
    void testHasToBeRemovedWithGoodQuality() {
        meat.setQuality(40);
        assertFalse(meat.hasToBeRemoved());
    }

    @Test
    void testHasToBeRemovedWithLowQuality() {
        meat.setQuality(39);
        assertTrue(meat.hasToBeRemoved());
    }

    @Test
    void testGetTotalPriceWithExpirationDiscount() {
        meat.setQuality(50);
        meat.setBasePrice(5);
        meat.setDaysUntilExpiration(2);
        assertEquals(10.50, meat.getTotalPrice());
    }

    @Test
    void testGetTotalPriceWithOutExpirationDiscount() {
        meat.setQuality(50);
        meat.setBasePrice(5);
        meat.setDaysUntilExpiration(3);
        assertEquals(15, meat.getTotalPrice());
    }

    @Test
    void getProductInNDays() {
        meat = Meat.builder()
                .name("Schweinefilet")
                .quality(50)
                .daysUntilExpiration(8)
                .basePrice(10)
                .build();
        Meat newMeat = meat.getProductInNDays(1);

        assertEquals(meat.getName(), newMeat.getName());
        assertEquals(48, newMeat.getQuality());
        assertEquals(7, newMeat.getDaysUntilExpiration());
        assertEquals(meat.getBasePrice(), newMeat.getBasePrice());
    }
}
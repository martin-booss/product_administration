package brockhaus.superdupermarkt.productadministration.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WineTest {

    private Wine wine;

    @BeforeEach
    void setUp() {
        wine = new Wine();
    }

    @Test
    void getTotalPrice() {
        wine.setBasePrice(15.99);
        assertEquals(15.99, wine.getTotalPrice());
    }

    @Test
    void isExpired() {
        wine.setDaysUntilExpiration(-1);
        assertFalse(wine.isExpired());
    }

    @Test
    void testGetProductInNDaysWithLowerQuality() {
        wine = Wine.builder()
                .name("Rotwein")
                .quality(42)
                .daysUntilExpiration(10)
                .basePrice(12)
                .build();
        Wine newWine = wine.getProductInNDays(55);

        assertEquals(wine.getName(), newWine.getName());
        assertEquals(47, newWine.getQuality());
        assertEquals(10, newWine.getDaysUntilExpiration());
        assertEquals(wine.getBasePrice(), newWine.getBasePrice());
    }

    @Test
    void testGetProductInNDaysWithQualityThreshold() {
        wine = Wine.builder()
                .name("Weißwein")
                .quality(45)
                .daysUntilExpiration(10)
                .basePrice(12)
                .build();
        Wine newWine = wine.getProductInNDays(100);

        assertEquals(wine.getName(), newWine.getName());
        assertEquals(50, newWine.getQuality());
        assertEquals(10, newWine.getDaysUntilExpiration());
        assertEquals(wine.getBasePrice(), newWine.getBasePrice());
    }
}
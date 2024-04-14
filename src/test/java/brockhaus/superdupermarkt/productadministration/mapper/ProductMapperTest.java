package brockhaus.superdupermarkt.productadministration.mapper;

import brockhaus.superdupermarkt.productadministration.model.Cheese;
import brockhaus.superdupermarkt.productadministration.model.Meat;
import brockhaus.superdupermarkt.productadministration.model.Product;
import brockhaus.superdupermarkt.productadministration.model.Wine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
    }

    @Test
    void testMapProductToStringArrayWithOkProduct() {
        Product product = Product.builder()
                .name("Apfel")
                .quality(10)
                .daysUntilExpiration(2)
                .basePrice(0.50)
                .build();

        String[] result = productMapper.mapProductToStringArray(product);
        assertEquals("Apfel", result[0]);
        assertEquals("10", result[1]);
        assertEquals("2", result[2]);
        assertEquals("1,50", result[3]);
        assertEquals("OK", result[4]);
    }

    @Test
    void testMapProductToStringArrayWithNotOkProduct() {
        Product product = Product.builder()
                .name("Birne")
                .quality(10)
                .daysUntilExpiration(-1)
                .basePrice(0.50)
                .build();

        String[] result = productMapper.mapProductToStringArray(product);
        assertEquals("Birne", result[0]);
        assertEquals("10", result[1]);
        assertEquals("-1", result[2]);
        assertEquals("1,50", result[3]);
        assertEquals("NICHT OK", result[4]);
    }

    @Test
    void testMapResultSetToProductWithCorrectResultSet() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getString("productName")).thenReturn("Parmesan");
        Mockito.when(resultSet.getInt("quality")).thenReturn(30);
        Mockito.when(resultSet.getDate("expirationDate")).thenReturn(new Date(System.currentTimeMillis()));
        Mockito.when(resultSet.getDouble("basePrice")).thenReturn(2.00);
        Mockito.when(resultSet.getString("category")).thenReturn("Käse");

        Product product = productMapper.mapResultSetToProduct(resultSet);
        assertEquals("Parmesan", product.getName());
        assertEquals(30, product.getQuality());
        assertEquals(0, product.getDaysUntilExpiration());
        assertEquals(2.00, product.getBasePrice());
        assertTrue(product instanceof Cheese);
    }

    @Test
    void testMapResultSetToProductWithIncorrectResultSet() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getString("category")).thenThrow(new SQLException("Mock Exception"));

        Product product = productMapper.mapResultSetToProduct(resultSet);
        assertNull(product);
    }

    @Test
    void testMapStringArrayToProductWithCorrectArray() {
        String[] values = new String[5];
        values[0] = "Rind";
        values[1] = "40";
        values[2] = new SimpleDateFormat("dd.MM.yyyy").format(new Date(System.currentTimeMillis()));
        values[3] = "5.50";
        values[4] = "Fleisch";

        Product product = productMapper.mapStringArrayToProduct(values);
        assertEquals("Rind", product.getName());
        assertEquals(40, product.getQuality());
        assertEquals(0, product.getDaysUntilExpiration());
        assertEquals(5.50, product.getBasePrice());
        assertTrue(product instanceof Meat);
    }

    @Test
    void testMapStringArrayToProductWithWrongQuality() {
        String[] values = new String[5];
        values[0] = "Rind";
        values[1] = "Quality should be Integer";
        values[2] = new SimpleDateFormat("dd.MM.yyyy").format(new Date(System.currentTimeMillis()));
        values[3] = "5.50";
        values[4] = "Fleisch";

        Product product = productMapper.mapStringArrayToProduct(values);
        assertNull(product);
    }

    @Test
    void testMapStringArrayToProductWithWrongDate() {
        String[] values = new String[5];
        values[0] = "Rind";
        values[1] = "40";
        values[2] = new SimpleDateFormat("dd-MM-yyyy").format(new Date(System.currentTimeMillis()));
        values[3] = "5.50";
        values[4] = "Fleisch";

        Product product = productMapper.mapStringArrayToProduct(values);
        assertNull(product);
    }

    @Test
    void testMapStringArrayToProductWithWrongBasePrice() {
        String[] values = new String[5];
        values[0] = "Rind";
        values[1] = "40";
        values[2] = new SimpleDateFormat("dd.MM.yyyy").format(new Date(System.currentTimeMillis()));
        values[3] = "BasePrice should be Double";
        values[4] = "Fleisch";

        Product product = productMapper.mapStringArrayToProduct(values);
        assertNull(product);
    }

    @Test
    void getDaysUntilExpiration() {
        long inTwoDays = System.currentTimeMillis() + (2 * 24 * 60 * 60 * 1000);
        int daysUntilExpiration = productMapper.getDaysUntilExpiration(new Date(inTwoDays));
        assertEquals(2, daysUntilExpiration);
    }

    @Test
    void getProductFromCategoryWithCheese() {
        Product product = productMapper.getProductFromCategory("cheese");
        assertTrue(product instanceof Cheese);
    }
    @Test
    void getProductFromCategoryWithWine() {
        Product product = productMapper.getProductFromCategory("wine");
        assertTrue(product instanceof Wine);
    }
    @Test
    void getProductFromCategoryWithMeat() {
        Product product = productMapper.getProductFromCategory("meat");
        assertTrue(product instanceof Meat);
    }
    @Test
    void getProductFromCategoryWithDefault() {
        Product product = productMapper.getProductFromCategory("Generic category");
        assertEquals(Product.class, product.getClass());
    }

    @Test
    void mapProductsToProductsInNDays() {
        Product product = Product.builder()
                .name("Apfel")
                .quality(10)
                .daysUntilExpiration(7)
                .basePrice(0.50)
                .build();

        List<Product> futureProducts = productMapper.mapProductsToProductsInNDays(List.of(product), 3);
        assertEquals(4, futureProducts.get(0).getDaysUntilExpiration());
    }
}
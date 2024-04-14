package brockhaus.superdupermarkt.productadministration.reader;

import brockhaus.superdupermarkt.productadministration.model.Cheese;
import brockhaus.superdupermarkt.productadministration.model.Product;
import brockhaus.superdupermarkt.productadministration.model.Wine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CompositeProductReaderTest {

    @InjectMocks
    CompositeProductReader compositeProductReader;

    @Mock
    ProductCsvFileReader productCsvFileReader;
    @Mock
    ProductDatabaseReader productDatabaseReader;

    @Spy
    List<ProductReader> productReaders = new ArrayList<>();

    @BeforeEach
    void setUp() {
        List<Product> productList1 = List.of(new Product(), new Cheese());
        Mockito.when(productCsvFileReader.readProducts()).thenReturn(productList1);
        productReaders.add(productCsvFileReader);

        List<Product> productList2 = List.of(new Wine());
        Mockito.when(productDatabaseReader.readProducts()).thenReturn(productList2);
        productReaders.add(productDatabaseReader);
    }

    @Test
    void readProducts() {
        List<Product> result = compositeProductReader.readProducts();
        assertEquals(3, result.size());
    }
}
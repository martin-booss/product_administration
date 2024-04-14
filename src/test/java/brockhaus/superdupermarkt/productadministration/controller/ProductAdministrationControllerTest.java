package brockhaus.superdupermarkt.productadministration.controller;

import brockhaus.superdupermarkt.productadministration.model.Product;
import brockhaus.superdupermarkt.productadministration.reader.ProductReader;
import brockhaus.superdupermarkt.productadministration.writer.ProductWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductAdministrationControllerTest {

    @InjectMocks
    ProductAdministrationController productAdministrationController;

    @Mock
    ProductReader productReader;
    @Mock
    ProductWriter productWriter;

    @Test
    void testRunWithoutException() {
        List<Product> products = List.of(new Product(), new Product());
        Mockito.when(productReader.readProducts()).thenReturn(products);
        Mockito.doNothing().when(productWriter).writeProducts(products);
        assertDoesNotThrow(() -> productAdministrationController.run());
    }

    @Test
    void testRunWithException() {
        List<Product> products = List.of(new Product(), new Product());
        Mockito.when(productReader.readProducts()).thenReturn(products);
        Mockito.doThrow(new RuntimeException("Mock Exception")).when(productWriter).writeProducts(products);
        assertThrows(RuntimeException.class, () -> productAdministrationController.run());
    }
}
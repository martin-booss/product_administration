package brockhaus.superdupermarkt.productadministration.reader;

import com.opencsv.CSVReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "command.line.runner.enabled=false" )
class ProductCsvFileReaderTest {

    @Autowired
    ProductCsvFileReader productCsvFileReader;

    @Test
    void readProducts() {
        int totalProductCount = productCsvFileReader.readProducts().size();
        assertEquals(7, totalProductCount);
    }

    @Test
    void readProductsFromFile() {
        File fileToTest = new File("src/test/resources/in/products_1.csv");
        int productCount = productCsvFileReader.readProductsFromFile(fileToTest).size();
        assertEquals(3, productCount);
    }

    @Test
    void getCsvReaderFromFile() throws FileNotFoundException {
        File fileToTest = new File("src/test/resources/in/products_1.csv");
        CSVReader csvReader = productCsvFileReader.getCsvReaderFromFile(fileToTest);
        assertNotNull(csvReader);
    }
}
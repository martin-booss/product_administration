package brockhaus.superdupermarkt.productadministration.writer;

import brockhaus.superdupermarkt.productadministration.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "command.line.runner.enabled=false" )
class ProductCsvFileWriterTest {

    @Value("${numberOfDays}")
    private int numberOfDays;
    @Value("${outputDir}")
    String outputPath;

    @Autowired
    private ProductCsvFileWriter productCsvFileWriter;

    @AfterEach
    void tearDown() {
        File outputDir = new File(outputPath);
        if(outputDir.isDirectory()) {
            Arrays.stream(outputDir.listFiles())
                    .forEach(file -> file.delete());
        }
        outputDir.delete();
    }

    @Test
    void writeProducts() {
        Product product = Product.builder()
                .name("Apfel")
                .quality(10)
                .daysUntilExpiration(2)
                .basePrice(0.50)
                .build();
        List<Product> products = List.of(product);

        productCsvFileWriter.writeProducts(products);

        File outputDir = new File(outputPath);
        assertTrue(outputDir.isDirectory());
        int initialFileCount = outputDir.listFiles(file -> file.getName().startsWith("product_list_initial")).length;
        int predictionFileCount = outputDir.listFiles(file -> file.getName().startsWith("product_list_prediction")).length;
        assertEquals(1, initialFileCount);
        assertEquals(numberOfDays, predictionFileCount);
    }

    @Test
    void testWriteProductsToCsvFileIfPossible() {
        new File(outputPath).mkdir();

        Product product = Product.builder()
                .name("Apfel")
                .quality(10)
                .daysUntilExpiration(2)
                .basePrice(0.50)
                .build();
        List<Product> products = List.of(product);

        productCsvFileWriter.writeProductsToCsvFile(products, "test.csv");

        File testFile = new File(outputPath + "/test.csv");
        assertTrue(testFile.exists());
    }

    @Test
    void testWriteProductsToCsvFileIfNotPossible() {
        Product product = Product.builder()
                .name("Apfel")
                .quality(10)
                .daysUntilExpiration(2)
                .basePrice(0.50)
                .build();
        List<Product> products = List.of(product);

        productCsvFileWriter.writeProductsToCsvFile(products, "test.csv");

        File testFile = new File(outputPath + "/test.csv");
        assertFalse(testFile.exists());
    }

    @Test
    void createDirectoryIfNecessary() {
        productCsvFileWriter.createDirectoryIfNecessary(outputPath);
        File outputDir = new File(outputPath);
        assertTrue(outputDir.exists() && outputDir.isDirectory());
    }

    @Test
    void cleanDirectory() throws IOException {
        File file = new File(outputPath + "/product_list_initial_20240101.csv");
        new File(outputPath).mkdir();
        file.createNewFile();
        productCsvFileWriter.cleanDirectory(outputPath);
        assertFalse(file.exists());
    }

}
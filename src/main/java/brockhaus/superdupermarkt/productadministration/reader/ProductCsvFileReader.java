package brockhaus.superdupermarkt.productadministration.reader;

import brockhaus.superdupermarkt.productadministration.mapper.ProductMapper;
import brockhaus.superdupermarkt.productadministration.model.Product;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class ProductCsvFileReader implements ProductReader {

    private static final Logger logger = LogManager.getLogger(ProductCsvFileReader.class);

    @Value("${inputDir}")
    private String inputPath;

    @Autowired
    private ProductMapper productMapper;

    public List<Product> readProducts() {
        logger.info("Reading products from csv files ...");
        List<Product> products = new ArrayList<>();
        File inputDir = new File(inputPath);
        if (inputDir.exists() && inputDir.isDirectory()) {
            Arrays.stream(Objects.requireNonNull(inputDir.listFiles()))
                    .filter(file -> file.getName().matches("products.*\\.csv"))
                    .forEach(file -> products.addAll(readProductsFromFile(file)));
        } else {
            logger.warn("Input directory {} does not exist or is not a directory", inputPath);
        }
        return products;
    }

    public List<Product> readProductsFromFile(File file) {
        logger.info("Reading products from file {}", file.getName());
        List<Product> products = new ArrayList<>();
        try (CSVReader csvReader = getCsvReaderFromFile(file)) {
            csvReader.readAll().stream()
                    .map(values -> productMapper.mapStringArrayToProduct(values))
                    .filter(Objects::nonNull)
                    .forEach(products::add);
        } catch (Exception e) {
            logger.warn("Can't read CSV file {}", file.getName());
            logger.warn(e.getMessage(), e);
        }
        return products;
    }


    public CSVReader getCsvReaderFromFile(File file) throws FileNotFoundException {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();

        return new CSVReaderBuilder(new FileReader(file))
                .withSkipLines(1)
                .withCSVParser(parser)
                .build();
    }
}

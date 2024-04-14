package brockhaus.superdupermarkt.productadministration.controller;

import brockhaus.superdupermarkt.productadministration.model.Product;
import brockhaus.superdupermarkt.productadministration.reader.ProductCsvFileReader;
import brockhaus.superdupermarkt.productadministration.reader.ProductDatabaseReader;
import brockhaus.superdupermarkt.productadministration.reader.ProductReader;
import brockhaus.superdupermarkt.productadministration.writer.ProductWriter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductAdministrationController implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(ProductAdministrationController.class);

    @Autowired
    @Qualifier("compositeProductReader")
    private ProductReader productReader;
    @Autowired
    private ProductWriter productWriter;

    @Override
    public void run(String... args) throws Exception {
        try {
            logger.info("Reading products ...");
            List<Product> products = productReader.readProducts();
            logger.info("... reading products successful");
            logger.info("Writing products ...");
            productWriter.writeProducts(products);
            logger.info("... writing products successful");
        } catch (Exception e) {
            logger.error("Can't execute program");
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
}

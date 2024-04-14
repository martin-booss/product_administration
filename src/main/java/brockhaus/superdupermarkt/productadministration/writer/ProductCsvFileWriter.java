package brockhaus.superdupermarkt.productadministration.writer;

import brockhaus.superdupermarkt.productadministration.mapper.ProductMapper;
import brockhaus.superdupermarkt.productadministration.model.Product;
import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Component
public class ProductCsvFileWriter implements ProductWriter{

    private static final Logger logger = LogManager.getLogger(ProductCsvFileWriter.class);

    @Autowired
    private ProductMapper productMapper;

    @Value("${numberOfDays}")
    private int numberOfDays;
    @Value("${outputDir}")
    private String outputPath;
    private final String FILENAME_INITIAL = "product_list_initial_";
    private final String FILENAME_PREDICTION = "product_list_prediction_";
    private final String FILENAME_EXTENSION = ".csv";

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    private final String[] HEADER = {"Produkt","Qualität","Haltbarkeit in Tagen","Aktueller Preis in €","Status für Verkauf"};

    @Override
    public void writeProducts(List<Product> products) {
        logger.info("Writing products to csv files ...");
        createDirectoryIfNecessary(outputPath);
        cleanDirectory(outputPath);
        Calendar calendar = Calendar.getInstance();
        String dateString = simpleDateFormat.format(calendar.getTime());
        String outputFile = FILENAME_INITIAL + dateString + FILENAME_EXTENSION;
        writeProductsToCsvFile(products, outputFile);

        for(int i=1; i<=numberOfDays; i++) {
            calendar.add(Calendar.DATE, 1);
            dateString = simpleDateFormat.format(calendar.getTime());
            outputFile = FILENAME_PREDICTION + dateString + FILENAME_EXTENSION;
            List<Product> futureProducts = productMapper.mapProductsToProductsInNDays(products, i);
            writeProductsToCsvFile(futureProducts, outputFile);
        }
    }

    public void writeProductsToCsvFile(List<Product> products, String outputFile) {
        logger.info("Writing products to file {}", outputFile);
        String outputFilePath = String.join("/", outputPath, outputFile);
        try(CSVWriter csvWriter = new CSVWriter(new FileWriter(outputFilePath, false))) {
            csvWriter.writeNext(HEADER);
            List<String[]> allLines = products.stream()
                    .map(productMapper::mapProductToStringArray)
                    .toList();
            csvWriter.writeAll(allLines);
        } catch (IOException e) {
            logger.warn("Unable to write products to file {}", outputFile);
            logger.warn(e.getMessage(), e);
        }
    }

    public void createDirectoryIfNecessary(String dir) {
        File directory = new File(dir);
        if(!directory.exists() && !dir.isEmpty()) {
            if(!directory.mkdir()) {
                logger.error("Could not create directory {}", dir);
                throw new RuntimeException("Unable to create directory");
            }
        }
    }

    public void cleanDirectory(String dir) {
        File directory = new File(dir);
        File[] oldFiles = directory.listFiles(file -> {
            String filename = file.getName();
            return (filename.startsWith(FILENAME_PREDICTION) || filename.startsWith(FILENAME_INITIAL))
                    && filename.endsWith(FILENAME_EXTENSION) && !file.isDirectory();
        });
        if(oldFiles != null && oldFiles.length > 0) {
            logger.info("Deleting old files in {}", dir);
            Arrays.stream(oldFiles).forEach(File::delete);
        }
    }
}

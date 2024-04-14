package brockhaus.superdupermarkt.productadministration.reader;

import brockhaus.superdupermarkt.productadministration.mapper.ProductMapper;
import brockhaus.superdupermarkt.productadministration.model.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDatabaseReader implements ProductReader{

    private static final Logger logger = LogManager.getLogger(ProductDatabaseReader.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProductMapper productMapper;

    private static final String QUERY = "SELECT * FROM products;";

    @Override
    public List<Product> readProducts() {
        logger.info("Reading products from database ...");
        List<Product> products = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(QUERY)) {
            while (resultSet.next()) {
                Product product = productMapper.mapResultSetToProduct(resultSet);
                if(product != null) {
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            logger.warn("Can't read database");
            logger.warn(e.getMessage(), e);
        }
        return products;
    }

}

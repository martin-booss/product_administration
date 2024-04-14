package brockhaus.superdupermarkt.productadministration.mapper;

import brockhaus.superdupermarkt.productadministration.model.Cheese;
import brockhaus.superdupermarkt.productadministration.model.Meat;
import brockhaus.superdupermarkt.productadministration.model.Product;
import brockhaus.superdupermarkt.productadministration.model.Wine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ProductMapper {

    private static final SimpleDateFormat sfd = new SimpleDateFormat("dd.MM.yyyy");

    private static final Logger logger = LogManager.getLogger(ProductMapper.class);

    public String[] mapProductToStringArray(Product product) {
        String[] values = new String[5];
        values[0] = product.getName();
        values[1] = String.valueOf(product.getQuality());
        values[2] = String.valueOf(product.getDaysUntilExpiration());
        values[3] = String.format("%.2f", product.getTotalPrice());
        values[4] = product.hasToBeRemoved() ? "NICHT OK" : "OK";
        return values;
    }

    public Product mapResultSetToProduct(ResultSet resultSet) {
        try {
            String category = resultSet.getString("category");
            Product product = getProductFromCategory(category);

            product.setName(resultSet.getString("productName"));
            product.setQuality(resultSet.getInt("quality"));
            int daysUntilExpiration = getDaysUntilExpiration(resultSet.getDate("expirationDate"));
            product.setDaysUntilExpiration(daysUntilExpiration);
            product.setBasePrice(resultSet.getDouble("basePrice"));

            return product;
        } catch (Exception e) {
            logger.warn("ResultSet in unexpected format", e);
            return null;
        }
    }

    public Product mapStringArrayToProduct(String[] values) {
        try {
            String category = values[4].toUpperCase();
            Product product = getProductFromCategory(category);

            product.setName(values[0]);
            product.setQuality(Integer.parseInt(values[1]));
            int daysUntilExpiration = getDaysUntilExpiration(sfd.parse(values[2]));
            product.setDaysUntilExpiration(daysUntilExpiration);
            product.setBasePrice(Double.parseDouble(values[3]));

            return product;
        } catch (Exception e) {
            logger.warn("String array values in unexpected format", e);
            return null;
        }
    }

    public int getDaysUntilExpiration(Date expirationDate) {
        long millisUntilExpiration = expirationDate.getTime() - System.currentTimeMillis();
        double daysUntilExpiration = (double) millisUntilExpiration / (1000 * 60 * 60 * 24);
        return (int) Math.ceil(daysUntilExpiration);
    }

    public Product getProductFromCategory(String category) {
        return switch (category.toUpperCase()) {
            case "CHEESE", "KÄSE" -> new Cheese();
            case "WINE", "WEIN" -> new Wine();
            case "MEAT", "FLEISCH" -> new Meat();
            default -> new Product();
        };
    }

    public List<Product> mapProductsToProductsInNDays(List<Product> products, int n) {
        return products.stream()
                .map(product -> product.getProductInNDays(n))
                .toList();
    }

}

package brockhaus.superdupermarkt.productadministration.reader;

import brockhaus.superdupermarkt.productadministration.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompositeProductReader implements ProductReader{

    @Autowired
    List<ProductReader> productReaders;

    @Override
    public List<Product> readProducts() {
        List<Product> products = new ArrayList<>();
        productReaders.forEach(productReader -> {
            products.addAll(productReader.readProducts());
        });
        return products;
    }
}

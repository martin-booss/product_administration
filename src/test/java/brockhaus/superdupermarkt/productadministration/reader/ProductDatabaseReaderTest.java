package brockhaus.superdupermarkt.productadministration.reader;

import brockhaus.superdupermarkt.productadministration.mapper.ProductMapper;
import brockhaus.superdupermarkt.productadministration.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class ProductDatabaseReaderTest {

    @InjectMocks
    ProductDatabaseReader productDatabaseReader;

    @Mock
    ProductMapper productMapper;
    @Mock
    DataSource dataSource;

    @Test
    void testReadProductsWithCorrectConnection() throws SQLException{
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Statement statement = Mockito.mock(Statement.class);
        Connection connection = Mockito.mock(Connection.class);

        Mockito.when(resultSet.next()).thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        Mockito.when(statement.executeQuery(anyString())).thenReturn(resultSet);
        Mockito.when(connection.createStatement()).thenReturn(statement);
        Mockito.when(dataSource.getConnection()).thenReturn(connection);

        Mockito.when(productMapper.mapResultSetToProduct(any())).thenReturn(new Product());

        List<Product> products = productDatabaseReader.readProducts();
        assertEquals(2, products.size());
    }

    @Test void testReadProductsWithWrongConnection() throws SQLException {
        Mockito.when(dataSource.getConnection()).thenThrow(new SQLException("Mock Exception"));
        List<Product> products = productDatabaseReader.readProducts();
        assertEquals(0, products.size());
    }
}
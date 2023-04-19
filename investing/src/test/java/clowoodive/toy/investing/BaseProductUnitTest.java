package clowoodive.toy.investing;

import clowoodive.toy.investing.product.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Disabled
public class BaseProductUnitTest {

    protected ProductDto productDto1;

    protected ProductDto productDto2;

    @BeforeEach
    void setUp(){
        initProducts();
    }

    private void initProducts() {
        this.productDto1 = new ProductDto();
        this.productDto1.setProductId(12);
        this.productDto1.setTitle("first product");
        this.productDto1.setTotalInvestingAmount(50000);
        this.productDto1.setAccumInvestingAmount(20000);
        this.productDto1.setInvestingUserCount(3);
        this.productDto1.setOpenAt(LocalDateTime.now().minusDays(5).truncatedTo(ChronoUnit.MINUTES));
        this.productDto1.setCloseAt(LocalDateTime.now().plusDays(5).truncatedTo(ChronoUnit.MINUTES));

        this.productDto2 = new ProductDto();
        this.productDto2.setProductId(34);
        this.productDto2.setTitle("first product");
        this.productDto2.setTotalInvestingAmount(30000);
        this.productDto2.setAccumInvestingAmount(1000);
        this.productDto2.setInvestingUserCount(1);
        this.productDto2.setOpenAt(LocalDateTime.now().minusDays(10).truncatedTo(ChronoUnit.MINUTES));
        this.productDto2.setCloseAt(LocalDateTime.now().plusDays(10).truncatedTo(ChronoUnit.MINUTES));
    }
}

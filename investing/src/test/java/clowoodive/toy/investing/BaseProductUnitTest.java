package clowoodive.toy.investing;

import clowoodive.toy.investing.product.ProductDto;
import clowoodive.toy.investing.product.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Disabled
public class BaseProductUnitTest {

    protected ProductDto productDto1;

    protected ProductDto productDto2;

    protected ProductEntity productEntity1;

    protected ProductEntity productEntity2;

    @BeforeEach
    void init() {
        initProductDtos();
        initProductEntitys();

    }

    private void initProductDtos() {
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
        this.productDto2.setTitle("second product");
        this.productDto2.setTotalInvestingAmount(30000);
        this.productDto2.setAccumInvestingAmount(1000);
        this.productDto2.setInvestingUserCount(1);
        this.productDto2.setOpenAt(LocalDateTime.now().minusDays(10).truncatedTo(ChronoUnit.MINUTES));
        this.productDto2.setCloseAt(LocalDateTime.now().plusDays(10).truncatedTo(ChronoUnit.MINUTES));
    }

    private void initProductEntitys() {
        this.productEntity1 = new ProductEntity();
        this.productEntity1.setProduct_id(11);
        this.productEntity1.setTitle("Oil");
        this.productEntity1.setTotal_investing_amount(5000);
        this.productEntity1.setAccumulated_investing_amount(2000);
        this.productEntity1.setInvesting_user_count(3);
        this.productEntity1.setOpen_at(LocalDateTime.now().minusDays(5).truncatedTo(ChronoUnit.MINUTES));
        this.productEntity1.setClose_at(LocalDateTime.now().minusDays(5).truncatedTo(ChronoUnit.MINUTES));

        this.productEntity2 = new ProductEntity();
        this.productEntity2.setProduct_id(22);
        this.productEntity2.setTitle("Gas");
        this.productEntity2.setTotal_investing_amount(15000);
        this.productEntity2.setAccumulated_investing_amount(4000);
        this.productEntity2.setInvesting_user_count(6);
        this.productEntity2.setOpen_at(LocalDateTime.now().minusDays(7).truncatedTo(ChronoUnit.MINUTES));
        this.productEntity2.setClose_at(LocalDateTime.now().minusDays(7).truncatedTo(ChronoUnit.MINUTES));
    }

    protected boolean isSameObject(Object obj1, Object obj2) throws IllegalAccessException {
        Class<?> aClass = obj1.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();

        for (Field field : declaredFields) {
            field.setAccessible(true);
            var field1 = field.get(obj1);
            var field2 = field.get(obj2);

            if (!field1.equals(field2))
                return false;
        }

        return true;
    }
}

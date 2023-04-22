package clowoodive.toy.investing.mapper;

import clowoodive.toy.investing.BaseProductUnitTest;
import clowoodive.toy.investing.product.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("mysql")
@Sql({"classpath:db/mysql/schema.sql", "classpath:db/mysql/data.sql"})
class ProductDBMapperTest extends BaseProductUnitTest {

    @Autowired
    ProductDBMapper productDBMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("투자 상품_삽입")
    void insertOrUpdateProduct_insert() throws IllegalAccessException {
        // given
        this.productEntity1.setProduct_id(0);
        this.productEntity2.setProduct_id(0);

        // when
        int updated1 = productDBMapper.insertOrUpdateProduct(this.productEntity1);
        int updated2 = productDBMapper.insertOrUpdateProduct(this.productEntity2);

        // then
        then(updated1).isEqualTo(1);
        then(updated2).isEqualTo(1);
        ProductEntity insertProduct1 = productDBMapper.selectProductById(this.productEntity1.getProduct_id());
        ProductEntity insertProduct2 = productDBMapper.selectProductById(this.productEntity2.getProduct_id());
        then(this.productEntity1.getProduct_id()).isGreaterThan(0);
        then(this.productEntity2.getProduct_id()).isGreaterThan(0);
        then(isFieldValueSame(this.productEntity1, insertProduct1)).isTrue();
        then(isFieldValueSame(this.productEntity2, insertProduct2)).isTrue();
    }

    @Test
    @DisplayName("투자 상품_업데이트")
    void insertOrUpdateProduct_update() throws IllegalAccessException {
        // given
        ProductEntity selectProduct = productDBMapper.selectProductById(1);
        assertThat(selectProduct).isNotNull();
        selectProduct.setTitle(selectProduct.getTitle() + " inverse");
        selectProduct.setTotal_investing_amount(selectProduct.getTotal_investing_amount() + 1);
        selectProduct.setAccumulated_investing_amount(selectProduct.getAccumulated_investing_amount() + 1);
        selectProduct.setInvesting_user_count(selectProduct.getInvesting_user_count() + 1);
        selectProduct.setOpen_at(selectProduct.getOpen_at().minusDays(1));
        selectProduct.setClose_at(selectProduct.getClose_at().minusDays(1));

        // when
        int updated = productDBMapper.insertOrUpdateProduct(selectProduct);

        // then
        then(updated).isEqualTo(1);
        ProductEntity updateProduct = productDBMapper.selectProductById(selectProduct.getProduct_id());
        then(isFieldValueSame(selectProduct, updateProduct)).isTrue();
    }

    @Test
    void selectProductAll() {
        // given

        // when
        List<ProductEntity> productEntities = productDBMapper.selectProductAll();

        // then
        then(productEntities).hasSize(4);
    }

    @Test
    void selectProductById() throws IllegalAccessException {
        // given
        this.productEntity1.setProduct_id(0);
        int updated1 = productDBMapper.insertOrUpdateProduct(this.productEntity1);
        assertThat(updated1).isGreaterThan(0);

        // when
        ProductEntity insertProduct1 = productDBMapper.selectProductById(this.productEntity1.getProduct_id());

        // then
        then(this.productEntity1.getProduct_id()).isGreaterThan(0);
        then(insertProduct1).isNotNull();
        then(isFieldValueSame(this.productEntity1, insertProduct1)).isTrue();
    }

    @Test
    void deleteProductById() {
        // given
        int deleteProductId = 1;
        ProductEntity deleteProduct = productDBMapper.selectProductById(deleteProductId);
        assertThat(deleteProduct).isNotNull();

        // when
        int deleted = productDBMapper.deleteProductById(deleteProductId);

        // then
        then(deleted).isGreaterThan(0);
        ProductEntity selectProduct = productDBMapper.selectProductById(deleteProductId);
        then(selectProduct).isNull();
    }
}
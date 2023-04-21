package clowoodive.toy.investing.mapper;

import clowoodive.toy.investing.BaseProductUnitTest;
import clowoodive.toy.investing.product.ProductEntity;
import org.assertj.core.api.BDDAssertions;
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
        assertThat(updated1).isEqualTo(1);
        assertThat(updated2).isEqualTo(1);
        ProductEntity insertProduct1 = productDBMapper.selectProductById(this.productEntity1.getProduct_id());
        ProductEntity insertProduct2 = productDBMapper.selectProductById(this.productEntity2.getProduct_id());
        assertThat(this.productEntity1.getProduct_id()).isGreaterThan(0);
        assertThat(this.productEntity2.getProduct_id()).isGreaterThan(0);
        assertThat(isSameObject(this.productEntity1, insertProduct1)).isTrue();
        assertThat(isSameObject(this.productEntity2, insertProduct2)).isTrue();
    }

    @Test
    @DisplayName("투자 상품_업데이트")
    void insertOrUpdateProduct_update() throws IllegalAccessException {
        // given
        ProductEntity selectProduct1 = productDBMapper.selectProductById(1);
        selectProduct1.setTitle("tree");

        ProductEntity selectProduct2 = productDBMapper.selectProductById(2);


        // when
        productDBMapper.insertOrUpdateProduct(productEntity1);
        productDBMapper.insertOrUpdateProduct(productEntity2);

        // then
        ProductEntity insertProduct1 = productDBMapper.selectProductById(productEntity1.getProduct_id());
        ProductEntity insertProduct2 = productDBMapper.selectProductById(productEntity2.getProduct_id());
        assertThat(productEntity1.getProduct_id()).isGreaterThan(0);
        assertThat(productEntity2.getProduct_id()).isGreaterThan(0);
        assertThat(isSameObject(productEntity1, insertProduct1)).isTrue();
        assertThat(isSameObject(productEntity2, insertProduct2)).isTrue();
    }

    @Test
    void selectProductAll() {
        // given

        // when
        List<ProductEntity> productEntities = productDBMapper.selectProductAll();

        // then
        BDDAssertions.then(productEntities).hasSize(4);
    }

    @Test
    void selectProductById() {
    }

    @Test
    void selectProductMetaListByIdList() {
    }

    @Test
    void selectProductInvesting() {
    }

    @Test
    void selectNextProductId() {
    }

    @Test
    void updateProductInvesting() {
    }

    @Test
    void deleteProductById() {
    }
}
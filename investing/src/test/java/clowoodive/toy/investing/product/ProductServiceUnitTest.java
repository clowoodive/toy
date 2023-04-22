package clowoodive.toy.investing.product;

import clowoodive.toy.investing.BaseProductUnitTest;
import clowoodive.toy.investing.mapper.ProductDBMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

class ProductServiceUnitTest extends BaseProductUnitTest {

    @Test
    void getProducts() throws IllegalAccessException {
        // given
        ProductDBMapper mockProductMapper = Mockito.mock(ProductDBMapper.class);
        ProductService productService = new ProductService(mockProductMapper);
        List<ProductEntity> productEntitys = List.of(this.productEntity1, this.productEntity2);
        given(mockProductMapper.selectProductAll()).willReturn(productEntitys);

        // when
        List<ProductDto> productDtos = productService.getProducts();

        // then
        then(productDtos).hasSize(productEntitys.size());
        ProductDto productDto1 = productDtos.stream().filter(dto -> dto.getProductId() == this.productEntity1.getProduct_id()).findFirst().orElse(null);
        then(productDto1).isNotNull();
        then(isFieldValueSame(this.productEntity1, new ProductEntity(productDto1)));
        ProductDto productDto2 = productDtos.stream().filter(dto -> dto.getProductId() == this.productEntity2.getProduct_id()).findFirst().orElse(null);
        then(productDto2).isNotNull();
        then(isFieldValueSame(this.productEntity1, new ProductEntity(productDto2)));
        BDDMockito.then(mockProductMapper).should(times(1)).selectProductAll();
    }

    @Test
    void getProductById() {
    }

    @Test
    void saveProduct() {
    }

    @Test
    void deleteProduct() {
    }
}
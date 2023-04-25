package clowoodive.toy.investing.product;

import clowoodive.toy.investing.BaseProductUnitTest;
import clowoodive.toy.investing.mapper.ProductDBMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
        then(isFieldValueSame(this.productEntity1, new ProductEntity(productDto1))).isTrue();
        ProductDto productDto2 = productDtos.stream().filter(dto -> dto.getProductId() == this.productEntity2.getProduct_id()).findFirst().orElse(null);
        then(productDto2).isNotNull();
        then(isFieldValueSame(this.productEntity2, new ProductEntity(productDto2))).isTrue();
        BDDMockito.then(mockProductMapper).should(times(1)).selectProductAll();
    }

    @Test
    void getProductById() throws IllegalAccessException {
        // given
        ProductDBMapper mockProductMapper = Mockito.mock(ProductDBMapper.class);
        ProductService productService = new ProductService(mockProductMapper);
        given(mockProductMapper.selectProductById(anyInt())).willReturn(this.productEntity1);

        // when
        ProductDto productDto = productService.getProductById(this.productEntity1.getProduct_id());

        // then
        then(productDto).isNotNull();
        then(isFieldValueSame(new ProductDto(this.productEntity1), productDto)).isTrue();
        BDDMockito.then(mockProductMapper).should(times(1)).selectProductById(anyInt());
    }

    @Test
    void saveProduct() {
        // given
        ProductDBMapper mockProductMapper = Mockito.mock(ProductDBMapper.class);
        ProductService productService = new ProductService(mockProductMapper);
        given(mockProductMapper.insertOrUpdateProduct(any())).willReturn(1);

        // when
        int saved = productService.saveProduct(this.productDto1);

        // then
        then(saved).isEqualTo(1);
        BDDMockito.then(mockProductMapper).should(times(1)).insertOrUpdateProduct(any());
    }

    @Test
    void deleteProduct() {
        // given
        ProductDBMapper mockProductMapper = Mockito.mock(ProductDBMapper.class);
        ProductService productService = new ProductService(mockProductMapper);
        given(mockProductMapper.deleteProductById(anyInt())).willReturn(1);

        // when
        int deleted = productService.deleteProduct(this.productDto1);

        // then
        then(deleted).isEqualTo(1);
        BDDMockito.then(mockProductMapper).should(times(1)).deleteProductById(anyInt());
    }
}